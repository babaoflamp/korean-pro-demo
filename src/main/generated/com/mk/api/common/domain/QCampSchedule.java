package com.mk.api.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCampSchedule is a Querydsl query type for CampSchedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCampSchedule extends EntityPathBase<CampSchedule> {

    private static final long serialVersionUID = 155211318L;

    public static final QCampSchedule campSchedule = new QCampSchedule("campSchedule");

    public final NumberPath<Long> regionId = createNumber("regionId", Long.class);

    public final NumberPath<Long> scheduleId = createNumber("scheduleId", Long.class);

    public QCampSchedule(String variable) {
        super(CampSchedule.class, forVariable(variable));
    }

    public QCampSchedule(Path<? extends CampSchedule> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCampSchedule(PathMetadata metadata) {
        super(CampSchedule.class, metadata);
    }

}

