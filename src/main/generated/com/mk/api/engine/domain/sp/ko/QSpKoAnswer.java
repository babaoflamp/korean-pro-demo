package com.mk.api.engine.domain.sp.ko;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSpKoAnswer is a Querydsl query type for SpKoAnswer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSpKoAnswer extends EntityPathBase<SpKoAnswer> {

    private static final long serialVersionUID = -848057587L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSpKoAnswer spKoAnswer = new QSpKoAnswer("spKoAnswer");

    public final NumberPath<Long> answerId = createNumber("answerId", Long.class);

    public final SimplePath<com.fasterxml.jackson.databind.JsonNode> result = createSimple("result", com.fasterxml.jackson.databind.JsonNode.class);

    public final QSpKoQuestion spKoQuestion;

    public final DateTimePath<java.time.LocalDateTime> submittedAt = createDateTime("submittedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> sysId = createNumber("sysId", Long.class);

    public QSpKoAnswer(String variable) {
        this(SpKoAnswer.class, forVariable(variable), INITS);
    }

    public QSpKoAnswer(Path<? extends SpKoAnswer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSpKoAnswer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSpKoAnswer(PathMetadata metadata, PathInits inits) {
        this(SpKoAnswer.class, metadata, inits);
    }

    public QSpKoAnswer(Class<? extends SpKoAnswer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.spKoQuestion = inits.isInitialized("spKoQuestion") ? new QSpKoQuestion(forProperty("spKoQuestion")) : null;
    }

}

