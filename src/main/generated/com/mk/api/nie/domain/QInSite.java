package com.mk.api.nie.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QInSite is a Querydsl query type for InSite
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInSite extends EntityPathBase<InSite> {

    private static final long serialVersionUID = -283593125L;

    public static final QInSite inSite = new QInSite("inSite");

    public final NumberPath<Long> articleId = createNumber("articleId", Long.class);

    public final NumberPath<Long> fileId = createNumber("fileId", Long.class);

    public final NumberPath<Long> insiteId = createNumber("insiteId", Long.class);

    public final StringPath link = createString("link");

    public QInSite(String variable) {
        super(InSite.class, forVariable(variable));
    }

    public QInSite(Path<? extends InSite> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInSite(PathMetadata metadata) {
        super(InSite.class, metadata);
    }

}

