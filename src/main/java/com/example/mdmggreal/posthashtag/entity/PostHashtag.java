package com.example.mdmggreal.posthashtag.entity;

import com.example.mdmggreal.hashtag.entity.Hashtag;
import com.example.mdmggreal.post.entity.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostHashtag {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_hashtag_id")
    private Long id;

    @ManyToOne
    private Post post;

    @ManyToOne
    private Hashtag hashtag;

    public static PostHashtag of(Post post, Hashtag hashtag) {
        return PostHashtag.builder()
                .post(post)
                .hashtag(hashtag)
                .build();
    }

}
