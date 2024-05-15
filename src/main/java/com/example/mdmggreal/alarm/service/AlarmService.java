package com.example.mdmggreal.alarm.service;

import com.example.mdmggreal.alarm.entity.Alarm;
import com.example.mdmggreal.alarm.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;

    public void saveAlarm(Alarm alarm) {
        alarmRepository.save(alarm);
    }

    public List<Alarm> getAlarmListByToken(String token) {

        return alarmRepository.findByToken(token);
    }

}
