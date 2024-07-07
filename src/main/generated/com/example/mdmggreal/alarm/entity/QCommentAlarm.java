package com.example.mdmggreal.alarm.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommentAlarm is a Querydsl query type for CommentAlarm
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommentAlarm extends EntityPathBase<CommentAlarm> {

    private static final long serialVersionUID = 362273589L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommentAlarm commentAlarm = new QCommentAlarm("commentAlarm");

    public final com.example.mdmggreal.global.entity.QBaseEntity _super = new com.example.mdmggreal.global.entity.QBaseEntity(this);

    public final StringPath alarmContents = createString("alarmContents");

    public final com.example.mdmggreal.comment.entity.QComment comment;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDateTime = _super.createdDateTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isRead = createBoolean("isRead");

    public final com.example.mdmggreal.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDateTime = _super.modifyDateTime;

    public QCommentAlarm(String variable) {
        this(CommentAlarm.class, forVariable(variable), INITS);
    }

    public QCommentAlarm(Path<? extends CommentAlarm> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommentAlarm(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommentAlarm(PathMetadata metadata, PathInits inits) {
        this(CommentAlarm.class, metadata, inits);
    }

    public QCommentAlarm(Class<? extends CommentAlarm> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.comment = inits.isInitialized("comment") ? new com.example.mdmggreal.comment.entity.QComment(forProperty("comment"), inits.get("comment")) : null;
        this.member = inits.isInitialized("member") ? new com.example.mdmggreal.member.entity.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

