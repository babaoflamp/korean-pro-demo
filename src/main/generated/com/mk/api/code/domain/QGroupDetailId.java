package com.mk.api.code.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGroupDetailId is a Querydsl query type for GroupDetailId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QGroupDetailId extends BeanPath<GroupDetailId> {

    private static final long serialVersionUID = 559562283L;

    public static final QGroupDetailId groupDetailId = new QGroupDetailId("groupDetailId");

    public final StringPath cd = createString("cd");

    public final StringPath groupCd = createString("groupCd");

    public QGroupDetailId(String variable) {
        super(GroupDetailId.class, forVariable(variable));
    }

    public QGroupDetailId(Path<? extends GroupDetailId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGroupDetailId(PathMetadata metadata) {
        super(GroupDetailId.class, metadata);
    }

}

