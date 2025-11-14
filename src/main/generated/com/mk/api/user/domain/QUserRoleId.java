package com.mk.api.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserRoleId is a Querydsl query type for UserRoleId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QUserRoleId extends BeanPath<UserRoleId> {

    private static final long serialVersionUID = -802119430L;

    public static final QUserRoleId userRoleId = new QUserRoleId("userRoleId");

    public final StringPath roleId = createString("roleId");

    public final NumberPath<Long> sysId = createNumber("sysId", Long.class);

    public QUserRoleId(String variable) {
        super(UserRoleId.class, forVariable(variable));
    }

    public QUserRoleId(Path<? extends UserRoleId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserRoleId(PathMetadata metadata) {
        super(UserRoleId.class, metadata);
    }

}

