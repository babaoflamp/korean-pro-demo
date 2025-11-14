package com.mk.api.author.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAuthor is a Querydsl query type for Author
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAuthor extends EntityPathBase<Author> {

    private static final long serialVersionUID = 1885867433L;

    public static final QAuthor author = new QAuthor("author");

    public final StringPath authorId = createString("authorId");

    public final StringPath authorNm = createString("authorNm");

    public final EnumPath<com.mk.api.author.value.AuthorType> authorType = createEnum("authorType", com.mk.api.author.value.AuthorType.class);

    public final StringPath expln = createString("expln");

    public final EnumPath<com.mk.api.author.value.HttpMethod> httpMethod = createEnum("httpMethod", com.mk.api.author.value.HttpMethod.class);

    public final StringPath pattern = createString("pattern");

    public QAuthor(String variable) {
        super(Author.class, forVariable(variable));
    }

    public QAuthor(Path<? extends Author> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAuthor(PathMetadata metadata) {
        super(Author.class, metadata);
    }

}

