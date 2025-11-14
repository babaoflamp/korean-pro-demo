package com.mk.api.log.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLoginLog is a Querydsl query type for LoginLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLoginLog extends EntityPathBase<LoginLog> {

    private static final long serialVersionUID = -1143573276L;

    public static final QLoginLog loginLog = new QLoginLog("loginLog");

    public final StringPath conectBrowser = createString("conectBrowser");

    public final NumberPath<Long> conectId = createNumber("conectId", Long.class);

    public final StringPath conectIp = createString("conectIp");

    public final StringPath conectOs = createString("conectOs");

    public final EnumPath<com.mk.api.log.value.ConnectionStatus> conectSttusCd = createEnum("conectSttusCd", com.mk.api.log.value.ConnectionStatus.class);

    public final EnumPath<com.mk.api.log.value.ErrorStatus> errorCd = createEnum("errorCd", com.mk.api.log.value.ErrorStatus.class);

    public final DateTimePath<java.time.LocalDateTime> logDt = createDateTime("logDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> logSeq = createNumber("logSeq", Long.class);

    public QLoginLog(String variable) {
        super(LoginLog.class, forVariable(variable));
    }

    public QLoginLog(Path<? extends LoginLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLoginLog(PathMetadata metadata) {
        super(LoginLog.class, metadata);
    }

}

