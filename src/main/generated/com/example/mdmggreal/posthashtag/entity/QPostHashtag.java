package com.example.mdmggreal.posthashtag.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostHashtag is a Querydsl query type for PostHashtag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostHashtag extends EntityPathBase<PostHashtag> {

    private static final long serialVersionUID = 1583511982L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostHashtag postHashtag = new QPostHashtag("postHashtag");

    public final com.example.mdmggreal.hashtag.entity.QHashtag hashtag;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.mdmggreal.post.entity.QPost post;

    public QPostHashtag(String variable) {
        this(PostHashtag.class, forVariable(variable), INITS);
    }

    public QPostHashtag(Path<? extends PostHashtag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostHashtag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostHashtag(PathMetadata metadata, PathInits inits) {
        this(PostHashtag.class, metadata, inits);
    }

    public QPostHashtag(Class<? extends PostHashtag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.hashtag = inits.isInitialized("hashtag") ? new com.example.mdmggreal.hashtag.entity.QHashtag(forProperty("hashtag")) : null;
        this.post = inits.isInitialized("post") ? new com.example.mdmggreal.post.entity.QPost(forProperty("post"), inits.get("post")) : null;
    }

}

