package com.mk.api.nie.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QViewHistory is a Querydsl query type for ViewHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QViewHistory extends EntityPathBase<ViewHistory> {

    private static final long serialVersionUID = -934521024L;

    public static final QViewHistory viewHistory = new QViewHistory("viewHistory");

    public final NumberPath<Long> articleId = createNumber("articleId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> cmpltdAt = createDateTime("cmpltdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> historyId = createNumber("historyId", Long.class);

    public final NumberPath<Long> sysId = createNumber("sysId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> viewedAt = createDateTime("viewedAt", java.time.LocalDateTime.class);

    public QViewHistory(String variable) {
        super(ViewHistory.class, forVariable(variable));
    }

    public QViewHistory(Path<? extends ViewHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QViewHistory(PathMetadata metadata) {
        super(ViewHistory.class, metadata);
    }

}

