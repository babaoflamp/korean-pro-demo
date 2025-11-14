package com.mk.api.log.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSysLog is a Querydsl query type for SysLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSysLog extends EntityPathBase<SysLog> {

    private static final long serialVersionUID = 1554028864L;

    public static final QSysLog sysLog = new QSysLog("sysLog");

    public final StringPath errorCd = createString("errorCd");

    public final EnumPath<com.mk.api.log.value.ErrorStatus> errorYn = createEnum("errorYn", com.mk.api.log.value.ErrorStatus.class);

    public final NumberPath<Long> logSeq = createNumber("logSeq", Long.class);

    public final StringPath methodNm = createString("methodNm");

    public final DatePath<java.time.LocalDate> occrrncDe = createDate("occrrncDe", java.time.LocalDate.class);

    public final TimePath<java.time.LocalTime> occrrncTime = createTime("occrrncTime", java.time.LocalTime.class);

    public final EnumPath<com.mk.api.log.value.ProcessSection> processSeCd = createEnum("processSeCd", com.mk.api.log.value.ProcessSection.class);

    public final NumberPath<Double> processTime = createNumber("processTime", Double.class);

    public final NumberPath<Long> rqesterId = createNumber("rqesterId", Long.class);

    public final StringPath rqesterIp = createString("rqesterIp");

    public final StringPath svcNm = createString("svcNm");

    public final StringPath trgetMenuNm = createString("trgetMenuNm");

    public QSysLog(String variable) {
        super(SysLog.class, forVariable(variable));
    }

    public QSysLog(Path<? extends SysLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysLog(PathMetadata metadata) {
        super(SysLog.class, metadata);
    }

}

