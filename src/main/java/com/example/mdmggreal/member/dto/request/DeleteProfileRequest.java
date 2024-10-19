package com.example.mdmggreal.member.dto.request;

import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Getter
public class DeleteProfileRequest {
    @Setter
    private Long memberId;
}
