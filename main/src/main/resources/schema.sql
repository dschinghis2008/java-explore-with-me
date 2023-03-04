--drop table if exists users, categories, events, events_compilations, requests, compilations, comments cascade ;

CREATE TABLE IF NOT EXISTS users
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name    VARCHAR(100) NOT NULL,
    email   VARCHAR(100) NOT NULL,
            UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name    VARCHAR(100) NOT NULL ,
    UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS events
(
    id                  BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation          VARCHAR(2000) NOT NULL ,
    title               VARCHAR(120) NOT NULL ,
    description         VARCHAR(7000) NOT NULL ,
    created             TIMESTAMP WITHOUT TIME ZONE NOT NULL ,
    dt                  TIMESTAMP WITHOUT TIME ZONE NOT NULL ,
    event_lat           REAL NOT NULL ,
    event_lon           REAL NOT NULL ,
    paid                BOOLEAN NOT NULL ,
    participant_max     INT NOT NULL ,
    published_dt        TIMESTAMP WITHOUT TIME ZONE,
    request_moderation  BOOLEAN,
    state               VARCHAR(100),
    initiator_id        BIGINT
                        CONSTRAINT events_user
                        REFERENCES users
                        ON UPDATE CASCADE ON DELETE CASCADE,
    category_id         BIGINT
                        CONSTRAINT events_category
                        REFERENCES categories
                        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS compilations
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title   VARCHAR(500) NOT NULL,
    pinned  BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS events_compilations
(
    compilation_id  BIGINT
                    CONSTRAINT events_compilations_comp_id
                    REFERENCES compilations
                    ON UPDATE CASCADE ON DELETE CASCADE,
    event_id        BIGINT
                    CONSTRAINT events_compilations_event_id
                    REFERENCES events
                    ON UPDATE CASCADE ON DELETE CASCADE,
                    PRIMARY KEY (compilation_id, event_id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id              BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    created         TIMESTAMP WITHOUT TIME ZONE NOT NULL ,
    event_id        BIGINT
                    CONSTRAINT request_event
                    REFERENCES events
                    ON UPDATE CASCADE ON DELETE CASCADE,
    requester_id    BIGINT
                    CONSTRAINT request_user
                    REFERENCES users
                    ON UPDATE CASCADE ON DELETE CASCADE,
    status          VARCHAR(10) NOT NULL,
                    UNIQUE (event_id, requester_id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id                  BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text                VARCHAR(1000) NOT NULL,
    author_id           BIGINT
                        CONSTRAINT comments_user
                        REFERENCES users
                        ON UPDATE CASCADE ON DELETE CASCADE,
    event_id            BIGINT
                        CONSTRAINT comments_event
                        REFERENCES events
                        ON UPDATE CASCADE ON DELETE CASCADE,
    created             TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    visible             BOOLEAN NOT NULL
);