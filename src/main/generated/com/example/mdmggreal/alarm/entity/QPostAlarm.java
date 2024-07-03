package com.example.mdmggreal.alarm.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostAlarm is a Querydsl query videoType for PostAlarm
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostAlarm extends EntityPathBase<PostAlarm> {

    private static final long serialVersionUID = 1041789038L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostAlarm postAlarm = new QPostAlarm("postAlarm");

    public final com.example.mdmggreal.global.entity.QBaseEntity _super = new com.example.mdmggreal.global.entity.QBaseEntity(this);

    public final StringPath alarmContents = createString("alarmContents");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDateTime = _super.createdDateTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isRead = createBoolean("isRead");

    public final com.example.mdmggreal.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDateTime = _super.modifyDateTime;

    public final com.example.mdmggreal.post.entity.QPost post;

    public QPostAlarm(String variable) {
        this(PostAlarm.class, forVariable(variable), INITS);
    }

    public QPostAlarm(Path<? extends PostAlarm> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostAlarm(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostAlarm(PathMetadata metadata, PathInits inits) {
        this(PostAlarm.class, metadata, inits);
    }

    public QPostAlarm(Class<? extends PostAlarm> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.mdmggreal.member.entity.QMember(forProperty("member"), inits.get("member")) : null;
        this.post = inits.isInitialized("post") ? new com.example.mdmggreal.post.entity.QPost(forProperty("post"), inits.get("post")) : null;
    }

}

