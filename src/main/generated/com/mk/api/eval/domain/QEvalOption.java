package com.mk.api.eval.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEvalOption is a Querydsl query type for EvalOption
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEvalOption extends EntityPathBase<EvalOption> {

    private static final long serialVersionUID = -1285973024L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEvalOption evalOption = new QEvalOption("evalOption");

    public final QEvalQuestion evalQuestion;

    public final StringPath option = createString("option");

    public final NumberPath<Long> optionId = createNumber("optionId", Long.class);

    public final NumberPath<Long> order = createNumber("order", Long.class);

    public QEvalOption(String variable) {
        this(EvalOption.class, forVariable(variable), INITS);
    }

    public QEvalOption(Path<? extends EvalOption> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEvalOption(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEvalOption(PathMetadata metadata, PathInits inits) {
        this(EvalOption.class, metadata, inits);
    }

    public QEvalOption(Class<? extends EvalOption> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.evalQuestion = inits.isInitialized("evalQuestion") ? new QEvalQuestion(forProperty("evalQuestion")) : null;
    }

}

