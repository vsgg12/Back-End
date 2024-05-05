package com.example.mdmggreal.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1992841862L;

    public static final QMember member = new QMember("member1");

    public final NumberPath<Integer> age = createNumber("age", Integer.class);

    public final StringPath authentication = createString("authentication");

    public final DateTimePath<java.util.Date> createDateTime = createDateTime("createDateTime", java.util.Date.class);

    public final StringPath email = createString("email");

    public final StringPath gender = createString("gender");

    public final StringPath memberId = createString("memberId");

    public final StringPath mobileNumber = createString("mobileNumber");

    public final DateTimePath<java.util.Date> modifyDateTime = createDateTime("modifyDateTime", java.util.Date.class);

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final StringPath profileImage = createString("profileImage");

    public final StringPath tier = createString("tier");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

