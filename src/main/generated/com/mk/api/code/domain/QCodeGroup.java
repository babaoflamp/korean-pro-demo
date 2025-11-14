package com.mk.api.code.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCodeGroup is a Querydsl query type for CodeGroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCodeGroup extends EntityPathBase<CodeGroup> {

    private static final long serialVersionUID = -1087429166L;

    public static final QCodeGroup codeGroup = new QCodeGroup("codeGroup");

    public final StringPath cdNm = createString("cdNm");

    public final StringPath expln = createString("expln");

    public final StringPath groupCd = createString("groupCd");

    public final EnumPath<com.mk.api.code.value.UseYn> useYn = createEnum("useYn", com.mk.api.code.value.UseYn.class);

    public QCodeGroup(String variable) {
        super(CodeGroup.class, forVariable(variable));
    }

    public QCodeGroup(Path<? extends CodeGroup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCodeGroup(PathMetadata metadata) {
        super(CodeGroup.class, metadata);
    }

}

