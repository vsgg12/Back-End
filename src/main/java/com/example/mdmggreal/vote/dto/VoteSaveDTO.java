package com.example.mdmggreal.vote.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteSaveDTO {

    private Long ingameInfoId;
    private Long ratio;
}
