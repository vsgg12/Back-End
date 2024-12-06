package com.example.mdmggreal.member.service;

import com.example.mdmggreal.member.repository.NicknameUpdateHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class NicknameUpdateHistoryService {

    NicknameUpdateHistoryRepository repository;

    public boolean isNicknameChangeAllowed(Long memberId) {
        LocalDateTime createdDateTime = repository.findCreatedDateTimeByMemberId(memberId);

        return true;
    }
}
