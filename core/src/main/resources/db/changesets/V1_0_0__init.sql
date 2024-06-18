-- liquibase formatted sql

-- changeset shuml:1717415748938-1
CREATE TABLE categories
(
    id            UUID                        NOT NULL,
    name          VARCHAR(255)                NOT NULL,
    description   VARCHAR(255)                NOT NULL,
    author_login  VARCHAR(255)                NOT NULL,
    create_time   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

-- changeset shuml:1717415748938-2
CREATE TABLE tasks
(
    id                UUID                        NOT NULL,
    name              VARCHAR(255)                NOT NULL,
    description       VARCHAR(255)                NOT NULL,
    experience_amount BIGINT                      NOT NULL,
    topic_id          UUID                        NOT NULL,
    author_login      VARCHAR(255)                NOT NULL,
    create_time       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified_date     TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_tasks PRIMARY KEY (id)
);

-- changeset shuml:1717415748938-3
CREATE TABLE topics
(
    id            UUID                        NOT NULL,
    name          VARCHAR(255)                NOT NULL,
    category_id   UUID                        NOT NULL,
    author_login  VARCHAR(255)                NOT NULL,
    create_time   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_topics PRIMARY KEY (id)
);

-- changeset shuml:1717415748938-4
ALTER TABLE tasks
    ADD CONSTRAINT FK_TASKS_ON_TOPIC FOREIGN KEY (topic_id) REFERENCES topics (id);

-- changeset shuml:1717415748938-5
ALTER TABLE topics
    ADD CONSTRAINT FK_TOPICS_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES categories (id);

