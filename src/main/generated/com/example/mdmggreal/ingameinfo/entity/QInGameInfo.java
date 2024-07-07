package com.example.mdmggreal.ingameinfo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInGameInfo is a Querydsl query type for InGameInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInGameInfo extends EntityPathBase<InGameInfo> {

    private static final long serialVersionUID = -631294414L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInGameInfo inGameInfo = new QInGameInfo("inGameInfo");

    public final com.example.mdmggreal.global.entity.QBaseEntity _super = new com.example.mdmggreal.global.entity.QBaseEntity(this);

    public final StringPath championName = createString("championName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDateTime = _super.createdDateTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDateTime = _super.modifyDateTime;

    public final EnumPath<com.example.mdmggreal.ingameinfo.type.Position> position = createEnum("position", com.example.mdmggreal.ingameinfo.type.Position.class);

    public final com.example.mdmggreal.post.entity.QPost post;

    public final EnumPath<com.example.mdmggreal.ingameinfo.type.Tier> tier = createEnum("tier", com.example.mdmggreal.ingameinfo.type.Tier.class);

    public final NumberPath<Long> totalRatio = createNumber("totalRatio", Long.class);

    public QInGameInfo(String variable) {
        this(InGameInfo.class, forVariable(variable), INITS);
    }

    public QInGameInfo(Path<? extends InGameInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInGameInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInGameInfo(PathMetadata metadata, PathInits inits) {
        this(InGameInfo.class, metadata, inits);
    }

    public QInGameInfo(Class<? extends InGameInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new com.example.mdmggreal.post.entity.QPost(forProperty("post"), inits.get("post")) : null;
    }

}

