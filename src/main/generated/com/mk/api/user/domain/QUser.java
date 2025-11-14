package com.mk.api.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1482923159L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final EnumPath<com.mk.api.user.value.ApprovalYn> approvalYn = createEnum("approvalYn", com.mk.api.user.value.ApprovalYn.class);

    public final EnumPath<com.mk.api.user.value.DelYn> delYn = createEnum("delYn", com.mk.api.user.value.DelYn.class);

    public final StringPath email = createString("email");

    public final EnumPath<com.mk.api.user.value.FieldType> field = createEnum("field", com.mk.api.user.value.FieldType.class);

    public final EnumPath<com.mk.api.user.value.Grade> grade = createEnum("grade", com.mk.api.user.value.Grade.class);

    public final StringPath name = createString("name");

    public final StringPath phone = createString("phone");

    public final StringPath pwdEnc = createString("pwdEnc");

    public final com.mk.api.common.domain.QRegion region;

    public final StringPath school = createString("school");

    public final NumberPath<Long> sysId = createNumber("sysId", Long.class);

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.region = inits.isInitialized("region") ? new com.mk.api.common.domain.QRegion(forProperty("region")) : null;
    }

}

