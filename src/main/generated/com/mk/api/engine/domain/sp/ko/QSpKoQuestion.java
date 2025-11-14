package com.mk.api.engine.domain.sp.ko;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSpKoQuestion is a Querydsl query type for SpKoQuestion
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSpKoQuestion extends EntityPathBase<SpKoQuestion> {

    private static final long serialVersionUID = 394137461L;

    public static final QSpKoQuestion spKoQuestion = new QSpKoQuestion("spKoQuestion");

    public final StringPath fst = createString("fst");

    public final NumberPath<Long> koId = createNumber("koId", Long.class);

    public final NumberPath<Integer> order = createNumber("order", Integer.class);

    public final StringPath sentence = createString("sentence");

    public final StringPath syllLtrs = createString("syllLtrs");

    public final StringPath syllPhns = createString("syllPhns");

    public QSpKoQuestion(String variable) {
        super(SpKoQuestion.class, forVariable(variable));
    }

    public QSpKoQuestion(Path<? extends SpKoQuestion> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSpKoQuestion(PathMetadata metadata) {
        super(SpKoQuestion.class, metadata);
    }

}

