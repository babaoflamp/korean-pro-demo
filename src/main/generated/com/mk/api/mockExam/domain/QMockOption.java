package com.mk.api.mockExam.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMockOption is a Querydsl query type for MockOption
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMockOption extends EntityPathBase<MockOption> {

    private static final long serialVersionUID = 2029319611L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMockOption mockOption = new QMockOption("mockOption");

    public final EnumPath<com.mk.api.mockExam.value.AnswerYn> answerYn = createEnum("answerYn", com.mk.api.mockExam.value.AnswerYn.class);

    public final QMockQuestion mockQuestion;

    public final StringPath option = createString("option");

    public final NumberPath<Long> optionId = createNumber("optionId", Long.class);

    public final NumberPath<Long> order = createNumber("order", Long.class);

    public QMockOption(String variable) {
        this(MockOption.class, forVariable(variable), INITS);
    }

    public QMockOption(Path<? extends MockOption> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMockOption(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMockOption(PathMetadata metadata, PathInits inits) {
        this(MockOption.class, metadata, inits);
    }

    public QMockOption(Class<? extends MockOption> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.mockQuestion = inits.isInitialized("mockQuestion") ? new QMockQuestion(forProperty("mockQuestion")) : null;
    }

}

