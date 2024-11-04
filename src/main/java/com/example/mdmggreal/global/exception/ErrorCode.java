package com.example.mdmggreal.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // 유저
    SIGNUP_REQUIRED(HttpStatus.CONFLICT, "회원가입이 필요합니다."),
    INVALID_USER_ID(HttpStatus.NOT_FOUND, "존재하지 않는 아이디입니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 유저입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    INVALID_TOKEN(HttpStatus.NOT_FOUND, "유효하지 않은 토큰입니다."),
    NO_PERMISSION(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_JWT_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "변조된 토큰입니다."),
    JWT_CLAIMS_EMPTY(HttpStatus.BAD_REQUEST, "토큰 정보가 없습니다."),

    // 이미지
    NOT_FOUND_IMAGE(HttpStatus.BAD_REQUEST, "이미지가 존재하지 않습니다."),

    // 게시글
    INVALID_POST(HttpStatus.BAD_REQUEST, "존재하지 않는 게시글 입니다."),
    VIDEO_REQUIRED(HttpStatus.BAD_REQUEST, "영상링크 또는 파일을 첨부해주세요."),
    VIDEO_LINK_REQUIRED(HttpStatus.BAD_REQUEST, "영상링크를 첨부해주세요."),
    VIDEO_FILE_REQUIRED(HttpStatus.BAD_REQUEST, "영상파일을 첨부해주세요."),
    INVALID_END_DATE(HttpStatus.BAD_REQUEST,"판결 종료날짜는 오늘부터 최소 1일 후, 최대 30일 후로 설정할 수 있습니다."),
    NO_PERMISSION_TO_DELETE_POST(HttpStatus.UNAUTHORIZED, "게시글 삭제는 게시글 생성자만 가능합니다."),
    NO_PERMISSION_TO_UPDATE_POST(HttpStatus.UNAUTHORIZED, "게시글 수정는 게시글 생성자만 가능합니다."),

    // 투표
    VOTE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 투표한 게시글 입니다."),
    CANNOT_VOTE_OWN_POST(HttpStatus.BAD_REQUEST, "본인의 게시글에 투표할 수 없습니다."),
    CANNOT_VOTE_TO_FINISHED_POST(HttpStatus.BAD_REQUEST, "종료된 게시글에는 투표할 수 없습니다."),
    VOTES_TOTAL_VALUE_MUST_BE_TEN(HttpStatus.BAD_REQUEST, "모든 투표값의 합은 10이어야합니다."),
    ALL_IN_GAME_INFO_VOTE_REQUIRED(HttpStatus.BAD_REQUEST, "게시글의 모든 투표 항목에 투표해주세요."),
    NO_PERMISSION_TO_VIEW_RESULT(HttpStatus.UNAUTHORIZED, "투표 결과는 글 작성자와 투표 참여자만 조회할 수 있습니다."),

    // 댓글
    INVALID_COMMENT(HttpStatus.BAD_REQUEST, "부모 댓글이 없습니다."),
    COMMENT_NOT_EXISTS(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다"),
    INVALID_PARENT_ID(HttpStatus.BAD_REQUEST, "postId에 맞는 parentId인지 확인해주세요."),
    NO_PERMISSION_TO_DELETE_COMMENT(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),

    // 해시태그
    HASHTAGNAME_NOT_MATCH(HttpStatus.BAD_REQUEST, "해시태그 이름이 일치하지 않습니다."),

    // 알람
    INVALID_ALARM(HttpStatus.BAD_REQUEST, "존재하지 않는 알람입니다."),
    NOT_MATCH_IN_GAME_INFO(HttpStatus.BAD_REQUEST, "게시글에 해당하는 인게임 정보가 아닙니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
