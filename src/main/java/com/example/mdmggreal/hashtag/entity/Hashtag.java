package com.example.mdmggreal.hashtag.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hashtag {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;

    public static Hashtag from(String name) {
        return Hashtag.builder()
                .name(name)
                .build();
    }
}
