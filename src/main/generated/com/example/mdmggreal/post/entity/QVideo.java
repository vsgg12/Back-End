package com.example.mdmggreal.post.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QVideo is a Querydsl query videoType for Video
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QVideo extends BeanPath<Video> {

    private static final long serialVersionUID = 226272499L;

    public static final QVideo video = new QVideo("video");

    public final EnumPath<com.example.mdmggreal.post.entity.type.VideoType> type = createEnum("type", com.example.mdmggreal.post.entity.type.VideoType.class);

    public final StringPath url = createString("url");

    public QVideo(String variable) {
        super(Video.class, forVariable(variable));
    }

    public QVideo(Path<? extends Video> path) {
        super(path.getType(), path.getMetadata());
    }

    public QVideo(PathMetadata metadata) {
        super(Video.class, metadata);
    }

}

