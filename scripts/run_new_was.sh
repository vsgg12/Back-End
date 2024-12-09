#!/bin/bash

# =============================================================================
# run_new_was.sh
#
# 설명:
#   이 스크립트는 제로 다운타임 배포를 구현하기 위해 두 개의 WAS(Web Application Server)
#   인스턴스(포트 8081과 8082)를 번갈아가며 실행합니다. 새로운 인스턴스를 시작하고
#   정상적으로 실행되는 것을 확인한 후, Nginx가 이를 프록시하도록 설정을 업데이트합니다.
#   이후 기존에 실행 중이던 인스턴스를 강제로 종료합니다.
#
# 사용 방법:
#   스크립트에 실행 권한을 부여한 후 실행합니다.
#   예: chmod +x run_new_was.sh && ./run_new_was.sh
# =============================================================================

# =============================================================================
# 변수 설정
# =============================================================================

PROJECT_ROOT="/home/ec2-user/mdmgg" # 프로젝트 루트 디렉토리
JAR_FILE="$PROJECT_ROOT/build/libs/mdmgg-Real-0.0.1-SNAPSHOT.jar" # 실행할 JAR 파일 경로

SERVICE_URL_FILE="/home/ec2-user/service_url.inc" # 현재 Nginx가 프록시하고 있는 WAS 포트를 지정하는 파일
NOHUP_OUT="/home/ec2-user/nohup.out" # WAS 인스턴스 로그 파일
LOG_FILE="/home/ec2-user/deploy.log" # 배포 스크립트 로그 파일

HEALTH_CHECK_URL="/api/test" # WAS 헬스 체크를 위한 API 엔드포인트
HEALTH_CHECK_RETRIES=10 # 헬스 체크 최대 시도 횟수
HEALTH_CHECK_INTERVAL=15 # 헬스 체크 시도 간격 (초)

# =============================================================================
# 로그 함수 정의
# =============================================================================

log() {
    echo "$(date '+%Y-%m-%d %H:%M:%S') - $1" | tee -a "$LOG_FILE"
}

# =============================================================================
# 초기 설정 확인
# =============================================================================

# 서비스 URL 파일 존재 여부 확인
if [ ! -f "$SERVICE_URL_FILE" ]; then
    log "ERROR: $SERVICE_URL_FILE 파일이 존재하지 않습니다."
    exit 1
fi

# 현재 활성화된 WAS 포트 확인
CURRENT_PORT=$(grep -Po '[0-9]+' "$SERVICE_URL_FILE" | tail -1)

if [ -z "$CURRENT_PORT" ]; then
    log "ERROR: 현재 활성화된 포트를 확인할 수 없습니다."
    exit 1
fi

log "현재 활성화된 WAS 포트: $CURRENT_PORT"

# =============================================================================
# 타겟 포트 결정 (8081 ↔ 8082)
# =============================================================================

if [ "$CURRENT_PORT" -eq 8081 ]; then
    TARGET_PORT=8082
elif [ "$CURRENT_PORT" -eq 8082 ]; then
    TARGET_PORT=8081
else
    log "ERROR: 현재 포트($CURRENT_PORT)이 8081 또는 8082가 아닙니다."
    exit 1
fi

log "타겟 포트 설정: $TARGET_PORT"

# =============================================================================
# 새로운 WAS 인스턴스 시작
# =============================================================================

log "새로운 WAS 인스턴스($TARGET_PORT) 시작 중..."
nohup java -jar -Dserver.port="$TARGET_PORT" "$JAR_FILE" > "$NOHUP_OUT" 2>&1 &
NEW_PID=$!
log "새로운 WAS 인스턴스 PID: $NEW_PID (포트: $TARGET_PORT)"

# =============================================================================
# 헬스 체크 수행
# =============================================================================

log "새로운 WAS 인스턴스($TARGET_PORT) 헬스 체크 시작..."

HEALTH_CHECK_SUCCESS=false

for RETRY_COUNT in $(seq 1 $HEALTH_CHECK_RETRIES); do
    log "헬스 체크 시도 #$RETRY_COUNT..."
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" "http://127.0.0.1:$TARGET_PORT$HEALTH_CHECK_URL")

    if [ "$RESPONSE_CODE" -eq 200 ]; then
        log "헬스 체크 성공: WAS 인스턴스($TARGET_PORT)가 정상적으로 실행 중입니다."
        HEALTH_CHECK_SUCCESS=true
        break
    else
        log "헬스 체크 실패: HTTP 상태 코드 $RESPONSE_CODE. $HEALTH_CHECK_INTERVAL초 후 재시도."
        sleep "$HEALTH_CHECK_INTERVAL"
    fi
done

if [ "$HEALTH_CHECK_SUCCESS" != true ]; then
    log "ERROR: 헬스 체크가 $HEALTH_CHECK_RETRIES번 모두 실패했습니다."
    log "새로운 WAS 인스턴스 PID $NEW_PID를 종료합니다."
    sudo kill -9 "$NEW_PID"
    exit 1
fi

# =============================================================================
# Nginx 설정 업데이트 및 리로드
# =============================================================================

log "service_url.inc 파일을 새로운 WAS 포트($TARGET_PORT)로 업데이트 중..."
echo "set \$service_url http://127.0.0.1:$TARGET_PORT;" > "$SERVICE_URL_FILE"
log "service_url.inc 파일 업데이트 완료."

log "Nginx 설정 리로드 중..."
sudo service nginx reload

if [ $? -eq 0 ]; then
    log "Nginx 리로드 성공: 이제 Nginx가 포트 $TARGET_PORT를 프록시하고 있습니다."
else
    log "ERROR: Nginx 리로드에 실패했습니다."
    log "새로운 WAS 인스턴스 PID $NEW_PID를 종료합니다."
    sudo kill -9 "$NEW_PID"
    exit 1
fi

# =============================================================================
# 기존 WAS 인스턴스 종료
# =============================================================================

log "기존 WAS 인스턴스($CURRENT_PORT) 종료 중..."
OLD_PID=$(lsof -ti TCP:"$CURRENT_PORT")

if [ -n "$OLD_PID" ]; then
    log "기존 WAS PID: $OLD_PID. 종료 시도 중..."
    sudo kill -15 "$OLD_PID"
    sleep 10

    # 프로세스가 종료되었는지 확인
    if kill -0 "$OLD_PID" 2>/dev/null; then
        log "WARN: PID $OLD_PID가 아직 종료되지 않았습니다. 강제 종료 시도."
        sudo kill -9 "$OLD_PID"
    fi
    log "기존 WAS 인스턴스 PID $OLD_PID 종료 완료."
else
    log "WARN: 포트 $CURRENT_PORT에서 실행 중인 WAS 인스턴스가 없습니다."
fi

log "배포 스크립트 완료."

exit 0
