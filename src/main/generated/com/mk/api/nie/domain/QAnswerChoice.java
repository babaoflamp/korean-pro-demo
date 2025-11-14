package com.mk.api.nie.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAnswerChoice is a Querydsl query type for AnswerChoice
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAnswerChoice extends EntityPathBase<AnswerChoice> {

    private static final long serialVersionUID = 842005518L;

    public static final QAnswerChoice answerChoice = new QAnswerChoice("answerChoice");

    public final StringPath answer = createString("answer");

    public final StringPath correctStat = createString("correctStat");

    public final NumberPath<Long> mltplId = createNumber("mltplId", Long.class);

    public final NumberPath<Long> testId = createNumber("testId", Long.class);

    public QAnswerChoice(String variable) {
        super(AnswerChoice.class, forVariable(variable));
    }

    public QAnswerChoice(Path<? extends AnswerChoice> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAnswerChoice(PathMetadata metadata) {
        super(AnswerChoice.class, metadata);
    }

}

