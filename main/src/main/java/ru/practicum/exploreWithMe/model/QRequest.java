package ru.practicum.exploreWithMe.model;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathInits;
import java.time.LocalDateTime;

public class QRequest extends EntityPathBase<Request> {
    private static final long serialVersionUID = 66861078L;
    private static final PathInits INITS;
    public static final QRequest request;
    public final DateTimePath<LocalDateTime> created;
    public final QEvent event;
    public final NumberPath<Long> id;
    public final QUser requester;
    public final EnumPath<Status> status;

    public QRequest(String variable) {
        this(Request.class, PathMetadataFactory.forVariable(variable), INITS);
    }

    public QRequest(Path<? extends Request> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRequest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRequest(PathMetadata metadata, PathInits inits) {
        this(Request.class, metadata, inits);
    }

    public QRequest(Class<? extends Request> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.created = this.createDateTime("created", LocalDateTime.class);
        this.id = this.createNumber("id", Long.class);
        this.status = this.createEnum("status", Status.class);
        this.event = inits.isInitialized("event") ? new QEvent(this.forProperty("event"), inits.get("event")) : null;
        this.requester = inits.isInitialized("requester") ? new QUser(this.forProperty("requester")) : null;
    }

    static {
        INITS = PathInits.DIRECT2;
        request = new QRequest("request");
    }
}

