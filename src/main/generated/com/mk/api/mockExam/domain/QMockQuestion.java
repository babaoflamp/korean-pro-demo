package com.mk.api.mockExam.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMockQuestion is a Querydsl query type for MockQuestion
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMockQuestion extends EntityPathBase<MockQuestion> {

    private static final long serialVersionUID = -825856084L;

    public static final QMockQuestion mockQuestion = new QMockQuestion("mockQuestion");

    public final StringPath field = createString("field");

    public final NumberPath<Long> fileId = createNumber("fileId", Long.class);

    public final NumberPath<Long> mockId = createNumber("mockId", Long.class);

    public final ListPath<MockOption, QMockOption> options = this.<MockOption, QMockOption>createList("options", MockOption.class, QMockOption.class, PathInits.DIRECT2);

    public final NumberPath<Long> order = createNumber("order", Long.class);

    public final StringPath question = createString("question");

    public QMockQuestion(String variable) {
        super(MockQuestion.class, forVariable(variable));
    }

    public QMockQuestion(Path<? extends MockQuestion> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMockQuestion(PathMetadata metadata) {
        super(MockQuestion.class, metadata);
    }

}

