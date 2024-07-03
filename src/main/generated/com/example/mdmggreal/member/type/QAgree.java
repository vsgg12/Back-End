package com.example.mdmggreal.member.type;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAgree is a Querydsl query videoType for Agree
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QAgree extends BeanPath<Agree> {

    private static final long serialVersionUID = 967559809L;

    public static final QAgree agree = new QAgree("agree");

    public final BooleanPath agreeAge = createBoolean("agreeAge");

    public final BooleanPath agreePrivacy = createBoolean("agreePrivacy");

    public final BooleanPath agreePromotion = createBoolean("agreePromotion");

    public final BooleanPath agreeTerms = createBoolean("agreeTerms");

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

