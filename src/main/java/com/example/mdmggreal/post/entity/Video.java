package com.example.mdmggreal.post.entity;

import com.example.mdmggreal.post.entity.type.VideoType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Video {
    private String url;
    @Enumerated(STRING)
    private VideoType type;

    public static Video of(String url, VideoType type) {
        return Video.builder()
                .url(url)
                .type(type)
                .build();
    }
}
