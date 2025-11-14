package com.mk.api.nie.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBehindStory is a Querydsl query type for BehindStory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBehindStory extends EntityPathBase<BehindStory> {

    private static final long serialVersionUID = 866587884L;

    public static final QBehindStory behindStory = new QBehindStory("behindStory");

    public final NumberPath<Long> articleId = createNumber("articleId", Long.class);

    public final NumberPath<Long> behindId = createNumber("behindId", Long.class);

    public final StringPath content = createString("content");

    public final StringPath title = createString("title");

    public QBehindStory(String variable) {
        super(BehindStory.class, forVariable(variable));
    }

    public QBehindStory(Path<? extends BehindStory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBehindStory(PathMetadata metadata) {
        super(BehindStory.class, metadata);
    }

}

