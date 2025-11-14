package com.mk.api.eval.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEvalAnswer is a Querydsl query type for EvalAnswer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEvalAnswer extends EntityPathBase<EvalAnswer> {

    private static final long serialVersionUID = -1688644823L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEvalAnswer evalAnswer = new QEvalAnswer("evalAnswer");

    public final NumberPath<Long> answerId = createNumber("answerId", Long.class);

    public final QEvalOption evalOption;

    public final QEvalQuestion evalQuestion;

    public final DateTimePath<java.time.LocalDateTime> submittedAt = createDateTime("submittedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> sysId = createNumber("sysId", Long.class);

    public QEvalAnswer(String variable) {
        this(EvalAnswer.class, forVariable(variable), INITS);
    }

    public QEvalAnswer(Path<? extends EvalAnswer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEvalAnswer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEvalAnswer(PathMetadata metadata, PathInits inits) {
        this(EvalAnswer.class, metadata, inits);
    }

    public QEvalAnswer(Class<? extends EvalAnswer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.evalOption = inits.isInitialized("evalOption") ? new QEvalOption(forProperty("evalOption"), inits.get("evalOption")) : null;
        this.evalQuestion = inits.isInitialized("evalQuestion") ? new QEvalQuestion(forProperty("evalQuestion")) : null;
    }

}

