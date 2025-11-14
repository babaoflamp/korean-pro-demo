package com.mk.api.mockExam.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMockAnswer is a Querydsl query type for MockAnswer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMockAnswer extends EntityPathBase<MockAnswer> {

    private static final long serialVersionUID = 1626647812L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMockAnswer mockAnswer = new QMockAnswer("mockAnswer");

    public final NumberPath<Long> answerId = createNumber("answerId", Long.class);

    public final QMockOption mockOption;

    public final QMockQuestion mockQuestion;

    public final DateTimePath<java.time.LocalDateTime> submittedAt = createDateTime("submittedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> sysId = createNumber("sysId", Long.class);

    public QMockAnswer(String variable) {
        this(MockAnswer.class, forVariable(variable), INITS);
    }

    public QMockAnswer(Path<? extends MockAnswer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMockAnswer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMockAnswer(PathMetadata metadata, PathInits inits) {
        this(MockAnswer.class, metadata, inits);
    }

    public QMockAnswer(Class<? extends MockAnswer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.mockOption = inits.isInitialized("mockOption") ? new QMockOption(forProperty("mockOption"), inits.get("mockOption")) : null;
        this.mockQuestion = inits.isInitialized("mockQuestion") ? new QMockQuestion(forProperty("mockQuestion")) : null;
    }

}

