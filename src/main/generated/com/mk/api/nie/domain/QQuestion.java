package com.mk.api.nie.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QQuestion is a Querydsl query type for Question
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestion extends EntityPathBase<Question> {

    private static final long serialVersionUID = 1549653109L;

    public static final QQuestion question1 = new QQuestion("question1");

    public final NumberPath<Long> articleId = createNumber("articleId", Long.class);

    public final NumberPath<Integer> fileId = createNumber("fileId", Integer.class);

    public final EnumPath<com.mk.api.nie.value.MltplStat> mltplStat = createEnum("mltplStat", com.mk.api.nie.value.MltplStat.class);

    public final NumberPath<Integer> order = createNumber("order", Integer.class);

    public final StringPath question = createString("question");

    public final NumberPath<Long> testId = createNumber("testId", Long.class);

    public QQuestion(String variable) {
        super(Question.class, forVariable(variable));
    }

    public QQuestion(Path<? extends Question> path) {
        super(path.getType(), path.getMetadata());
    }

    public QQuestion(PathMetadata metadata) {
        super(Question.class, metadata);
    }

}

