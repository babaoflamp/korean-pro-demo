package com.mk.api.log.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWebLog is a Querydsl query type for WebLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWebLog extends EntityPathBase<WebLog> {

    private static final long serialVersionUID = 1649568601L;

    public static final QWebLog webLog = new QWebLog("webLog");

    public final NumberPath<Long> logSeq = createNumber("logSeq", Long.class);

    public final DatePath<java.time.LocalDate> occrrncDe = createDate("occrrncDe", java.time.LocalDate.class);

    public final TimePath<java.time.LocalTime> occrrncTime = createTime("occrrncTime", java.time.LocalTime.class);

    public final NumberPath<Long> rqesterId = createNumber("rqesterId", Long.class);

    public final StringPath rqesterIp = createString("rqesterIp");

    public final StringPath url = createString("url");

    public QWebLog(String variable) {
        super(WebLog.class, forVariable(variable));
    }

    public QWebLog(Path<? extends WebLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWebLog(PathMetadata metadata) {
        super(WebLog.class, metadata);
    }

}

