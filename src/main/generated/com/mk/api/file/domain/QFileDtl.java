package com.mk.api.file.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFileDtl is a Querydsl query type for FileDtl
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFileDtl extends EntityPathBase<FileDtl> {

    private static final long serialVersionUID = -344813423L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFileDtl fileDtl = new QFileDtl("fileDtl");

    public final EnumPath<com.mk.api.file.value.DelYn> delYn = createEnum("delYn", com.mk.api.file.value.DelYn.class);

    public final QFile file;

    public final NumberPath<Long> fileDtlSeq = createNumber("fileDtlSeq", Long.class);

    public final StringPath fileExtsn = createString("fileExtsn");

    public final NumberPath<Long> fileSize = createNumber("fileSize", Long.class);

    public final NumberPath<Integer> fileSn = createNumber("fileSn", Integer.class);

    public final StringPath fileStrePath = createString("fileStrePath");

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSysId = createNumber("mdfrSysId", Long.class);

    public final StringPath orignlFileNm = createString("orignlFileNm");

    public final DateTimePath<java.time.LocalDateTime> regDt = createDateTime("regDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> rgtrSysId = createNumber("rgtrSysId", Long.class);

    public final StringPath streFileNm = createString("streFileNm");

    public QFileDtl(String variable) {
        this(FileDtl.class, forVariable(variable), INITS);
    }

    public QFileDtl(Path<? extends FileDtl> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFileDtl(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFileDtl(PathMetadata metadata, PathInits inits) {
        this(FileDtl.class, metadata, inits);
    }

    public QFileDtl(Class<? extends FileDtl> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.file = inits.isInitialized("file") ? new QFile(forProperty("file")) : null;
    }

}

