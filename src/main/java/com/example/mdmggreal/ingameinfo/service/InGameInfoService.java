package com.example.mdmggreal.ingameinfo.service;

import com.example.mdmggreal.ingameinfo.dto.response.InGameInfoResponse;
import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import com.example.mdmggreal.ingameinfo.repository.InGameInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class InGameInfoService {

    private final InGameInfoRepository inGameInfoRepository;

    public List<InGameInfoResponse> getInGameInfo(Long postId) {
        List<InGameInfo> inGameInfos = inGameInfoRepository.findByPostId(postId);
        return inGameInfos.stream()
                .map(InGameInfoResponse::of)
                .collect(Collectors.toList());
    }
}
