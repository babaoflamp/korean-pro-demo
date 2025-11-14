package com.mk.api.role.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRoleAuthorId is a Querydsl query type for RoleAuthorId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QRoleAuthorId extends BeanPath<RoleAuthorId> {

    private static final long serialVersionUID = -1797681819L;

    public static final QRoleAuthorId roleAuthorId = new QRoleAuthorId("roleAuthorId");

    public final StringPath authorId = createString("authorId");

    public final StringPath roleId = createString("roleId");

    public QRoleAuthorId(String variable) {
        super(RoleAuthorId.class, forVariable(variable));
    }

    public QRoleAuthorId(Path<? extends RoleAuthorId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRoleAuthorId(PathMetadata metadata) {
        super(RoleAuthorId.class, metadata);
    }

}

