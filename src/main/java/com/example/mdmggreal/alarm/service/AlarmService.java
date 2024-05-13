package com.example.mdmggreal.alarm.service;

import com.example.mdmggreal.alarm.entity.Alarm;
import com.example.mdmggreal.alarm.repository.AlarmRepository;
import com.example.mdmggreal.sse.controller.SSEController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private SSEController sseController;
    private AlarmRepository alarmRepository;

    public void saveAlarm(Alarm alarm) {
        alarmRepository.save(alarm);
    }

    public void sendNotification(String message) {
        // SSE 컨트롤러를 호출하여 알람 발송
        sseController.sendNotification(message);
    }

}
