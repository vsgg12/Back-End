package com.example.mdmggreal.post.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = -1378346712L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final com.example.mdmggreal.global.entity.QBaseEntity _super = new com.example.mdmggreal.global.entity.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDateTime = _super.createdDateTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.example.mdmggreal.ingameinfo.entity.InGameInfo, com.example.mdmggreal.ingameinfo.entity.QInGameInfo> inGameInfos = this.<com.example.mdmggreal.ingameinfo.entity.InGameInfo, com.example.mdmggreal.ingameinfo.entity.QInGameInfo>createList("inGameInfos", com.example.mdmggreal.ingameinfo.entity.InGameInfo.class, com.example.mdmggreal.ingameinfo.entity.QInGameInfo.class, PathInits.DIRECT2);

    public final com.example.mdmggreal.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDateTime = _super.modifyDateTime;

    public final EnumPath<com.example.mdmggreal.post.entity.type.PostStatus> status = createEnum("status", com.example.mdmggreal.post.entity.type.PostStatus.class);

    public final StringPath thumbnailURL = createString("thumbnailURL");

    public final StringPath title = createString("title");

    public final QVideo video;

    public final NumberPath<Long> viewCount = createNumber("viewCount", Long.class);

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.mdmggreal.member.entity.QMember(forProperty("member"), inits.get("member")) : null;
        this.video = inits.isInitialized("video") ? new QVideo(forProperty("video")) : null;
    }

}

