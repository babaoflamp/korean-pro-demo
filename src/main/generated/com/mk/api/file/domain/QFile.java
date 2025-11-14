package com.mk.api.file.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFile is a Querydsl query type for File
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFile extends EntityPathBase<File> {

    private static final long serialVersionUID = -35621557L;

    public static final QFile file = new QFile("file");

    public final ListPath<FileDtl, QFileDtl> fileDtls = this.<FileDtl, QFileDtl>createList("fileDtls", FileDtl.class, QFileDtl.class, PathInits.DIRECT2);

    public final NumberPath<Long> fileSeq = createNumber("fileSeq", Long.class);

    public final DateTimePath<java.time.LocalDateTime> regDt = createDateTime("regDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> rgtrSysId = createNumber("rgtrSysId", Long.class);

    public QFile(String variable) {
        super(File.class, forVariable(variable));
    }

    public QFile(Path<? extends File> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFile(PathMetadata metadata) {
        super(File.class, metadata);
    }

}

