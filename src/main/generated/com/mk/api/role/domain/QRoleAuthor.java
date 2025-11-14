package com.mk.api.role.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRoleAuthor is a Querydsl query type for RoleAuthor
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRoleAuthor extends EntityPathBase<RoleAuthor> {

    private static final long serialVersionUID = -891255126L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRoleAuthor roleAuthor = new QRoleAuthor("roleAuthor");

    public final QRoleAuthorId roleAuthorId;

    public QRoleAuthor(String variable) {
        this(RoleAuthor.class, forVariable(variable), INITS);
    }

    public QRoleAuthor(Path<? extends RoleAuthor> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRoleAuthor(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRoleAuthor(PathMetadata metadata, PathInits inits) {
        this(RoleAuthor.class, metadata, inits);
    }

    public QRoleAuthor(Class<? extends RoleAuthor> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.roleAuthorId = inits.isInitialized("roleAuthorId") ? new QRoleAuthorId(forProperty("roleAuthorId")) : null;
    }

}

