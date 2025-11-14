package com.mk.api.login.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QVerification is a Querydsl query type for Verification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVerification extends EntityPathBase<Verification> {

    private static final long serialVersionUID = -757819191L;

    public static final QVerification verification = new QVerification("verification");

    public final StringPath code = createString("code");

    public final NumberPath<Long> codeId = createNumber("codeId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final DateTimePath<java.time.LocalDateTime> expiresAt = createDateTime("expiresAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> sysId = createNumber("sysId", Long.class);

    public QVerification(String variable) {
        super(Verification.class, forVariable(variable));
    }

    public QVerification(Path<? extends Verification> path) {
        super(path.getType(), path.getMetadata());
    }

    public QVerification(PathMetadata metadata) {
        super(Verification.class, metadata);
    }

}

