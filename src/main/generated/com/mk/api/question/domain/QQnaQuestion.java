package com.mk.api.question.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QQnaQuestion is a Querydsl query type for QnaQuestion
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQnaQuestion extends EntityPathBase<QnaQuestion> {

    private static final long serialVersionUID = 1157086225L;

    public static final QQnaQuestion qnaQuestion = new QQnaQuestion("qnaQuestion");

    public final StringPath answerYn = createString("answerYn");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> fileId = createNumber("fileId", Long.class);

    public final NumberPath<Long> qnaId = createNumber("qnaId", Long.class);

    public final StringPath quesCd = createString("quesCd");

    public final NumberPath<Long> sysId = createNumber("sysId", Long.class);

    public final StringPath title = createString("title");

    public QQnaQuestion(String variable) {
        super(QnaQuestion.class, forVariable(variable));
    }

    public QQnaQuestion(Path<? extends QnaQuestion> path) {
        super(path.getType(), path.getMetadata());
    }

    public QQnaQuestion(PathMetadata metadata) {
        super(QnaQuestion.class, metadata);
    }

}

