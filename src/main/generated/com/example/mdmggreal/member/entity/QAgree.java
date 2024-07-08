package com.example.mdmggreal.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAgree is a Querydsl query type for Agree
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QAgree extends BeanPath<Agree> {

    private static final long serialVersionUID = 1557657130L;

    public static final QAgree agree = new QAgree("agree");

    public final EnumPath<com.example.mdmggreal.global.entity.type.BooleanEnum> agreeAge = createEnum("agreeAge", com.example.mdmggreal.global.entity.type.BooleanEnum.class);

    public final EnumPath<com.example.mdmggreal.global.entity.type.BooleanEnum> agreePrivacy = createEnum("agreePrivacy", com.example.mdmggreal.global.entity.type.BooleanEnum.class);

    public final EnumPath<com.example.mdmggreal.global.entity.type.BooleanEnum> agreePromotion = createEnum("agreePromotion", com.example.mdmggreal.global.entity.type.BooleanEnum.class);

    public final EnumPath<com.example.mdmggreal.global.entity.type.BooleanEnum> agreeTerms = createEnum("agreeTerms", com.example.mdmggreal.global.entity.type.BooleanEnum.class);

    public QAgree(String variable) {
        super(Agree.class, forVariable(variable));
    }

    public QAgree(Path<? extends Agree> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAgree(PathMetadata metadata) {
        super(Agree.class, metadata);
    }

}

