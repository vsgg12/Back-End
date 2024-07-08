package com.example.mdmggreal.member.entity;

import com.example.mdmggreal.global.entity.type.BooleanEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Agree {
    @Column(nullable = false)
    @Enumerated(STRING)
    private BooleanEnum agreeAge;

    @Column(nullable = false)
    @Enumerated(STRING)
    private BooleanEnum agreeTerms;

    @Column(nullable = false)
    @Enumerated(STRING)
    private BooleanEnum agreePrivacy;

    @Column(nullable = false)
    @Enumerated(STRING)
    private BooleanEnum agreePromotion;
}
