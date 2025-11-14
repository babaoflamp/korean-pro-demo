package com.mk.api.menu.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMenu is a Querydsl query type for Menu
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMenu extends EntityPathBase<Menu> {

    private static final long serialVersionUID = 1834940561L;

    public static final QMenu menu = new QMenu("menu");

    public final StringPath expln = createString("expln");

    public final NumberPath<Integer> menuLv = createNumber("menuLv", Integer.class);

    public final StringPath menuNm = createString("menuNm");

    public final NumberPath<Long> menuNo = createNumber("menuNo", Long.class);

    public final NumberPath<Integer> ordr = createNumber("ordr", Integer.class);

    public final NumberPath<Long> parentMenuNo = createNumber("parentMenuNo", Long.class);

    public final StringPath url = createString("url");

    public QMenu(String variable) {
        super(Menu.class, forVariable(variable));
    }

    public QMenu(Path<? extends Menu> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMenu(PathMetadata metadata) {
        super(Menu.class, metadata);
    }

}

