package com.example.mdmggreal.member.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateProfileImageRequest {
    private String profileUrl;

    @Setter
    private Long memberId;

}
