package com.mk.api.code.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCodeDetail is a Querydsl query type for CodeDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCodeDetail extends EntityPathBase<CodeDetail> {

    private static final long serialVersionUID = 551670622L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCodeDetail codeDetail = new QCodeDetail("codeDetail");

    public final StringPath cdNm = createString("cdNm");

    public final QCodeGroup codeGroup;

    public final StringPath expln = createString("expln");

    public final QGroupDetailId id;

    public final StringPath ordr = createString("ordr");

    public final EnumPath<com.mk.api.code.value.UseYn> useYn = createEnum("useYn", com.mk.api.code.value.UseYn.class);

    public QCodeDetail(String variable) {
        this(CodeDetail.class, forVariable(variable), INITS);
    }

    public QCodeDetail(Path<? extends CodeDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCodeDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCodeDetail(PathMetadata metadata, PathInits inits) {
        this(CodeDetail.class, metadata, inits);
    }

    public QCodeDetail(Class<? extends CodeDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.codeGroup = inits.isInitialized("codeGroup") ? new QCodeGroup(forProperty("codeGroup")) : null;
        this.id = inits.isInitialized("id") ? new QGroupDetailId(forProperty("id")) : null;
    }

}

