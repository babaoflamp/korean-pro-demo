package com.mk.api.eval.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEvalQuestion is a Querydsl query type for EvalQuestion
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEvalQuestion extends EntityPathBase<EvalQuestion> {

    private static final long serialVersionUID = 43655313L;

    public static final QEvalQuestion evalQuestion = new QEvalQuestion("evalQuestion");

    public final NumberPath<Long> evalId = createNumber("evalId", Long.class);

    public final StringPath field = createString("field");

    public final NumberPath<Long> fileId = createNumber("fileId", Long.class);

    public final ListPath<EvalOption, QEvalOption> options = this.<EvalOption, QEvalOption>createList("options", EvalOption.class, QEvalOption.class, PathInits.DIRECT2);

    public final NumberPath<Long> order = createNumber("order", Long.class);

    public final StringPath question = createString("question");

    public QEvalQuestion(String variable) {
        super(EvalQuestion.class, forVariable(variable));
    }

    public QEvalQuestion(Path<? extends EvalQuestion> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEvalQuestion(PathMetadata metadata) {
        super(EvalQuestion.class, metadata);
    }

}

