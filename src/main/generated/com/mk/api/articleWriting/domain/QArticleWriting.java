package com.mk.api.articleWriting.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QArticleWriting is a Querydsl query type for ArticleWriting
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QArticleWriting extends EntityPathBase<ArticleWriting> {

    private static final long serialVersionUID = -1780495009L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QArticleWriting articleWriting = new QArticleWriting("articleWriting");

    public final EnumPath<com.mk.api.articleWriting.value.ArticleType> articleType = createEnum("articleType", com.mk.api.articleWriting.value.ArticleType.class);

    public final EnumPath<com.mk.api.articleWriting.value.Composition> composition = createEnum("composition", com.mk.api.articleWriting.value.Composition.class);

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final EnumPath<com.mk.api.articleWriting.value.DelYn> delYn = createEnum("delYn", com.mk.api.articleWriting.value.DelYn.class);

    public final EnumPath<com.mk.api.articleWriting.value.Material> material = createEnum("material", com.mk.api.articleWriting.value.Material.class);

    public final StringPath title = createString("title");

    public final EnumPath<com.mk.api.articleWriting.value.Topic> topic = createEnum("topic", com.mk.api.articleWriting.value.Topic.class);

    public final com.mk.api.user.domain.QUser user;

    public final NumberPath<Long> writingId = createNumber("writingId", Long.class);

    public QArticleWriting(String variable) {
        this(ArticleWriting.class, forVariable(variable), INITS);
    }

    public QArticleWriting(Path<? extends ArticleWriting> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QArticleWriting(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QArticleWriting(PathMetadata metadata, PathInits inits) {
        this(ArticleWriting.class, metadata, inits);
    }

    public QArticleWriting(Class<? extends ArticleWriting> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.mk.api.user.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

