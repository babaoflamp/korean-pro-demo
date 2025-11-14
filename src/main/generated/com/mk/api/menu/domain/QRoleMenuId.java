package com.mk.api.menu.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRoleMenuId is a Querydsl query type for RoleMenuId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QRoleMenuId extends BeanPath<RoleMenuId> {

    private static final long serialVersionUID = 1774859426L;

    public static final QRoleMenuId roleMenuId = new QRoleMenuId("roleMenuId");

    public final NumberPath<Long> menuNo = createNumber("menuNo", Long.class);

    public final StringPath roleId = createString("roleId");

    public QRoleMenuId(String variable) {
        super(RoleMenuId.class, forVariable(variable));
    }

    public QRoleMenuId(Path<? extends RoleMenuId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRoleMenuId(PathMetadata metadata) {
        super(RoleMenuId.class, metadata);
    }

}

