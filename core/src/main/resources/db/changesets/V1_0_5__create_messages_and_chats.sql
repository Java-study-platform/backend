-- liquibase formatted sql

-- changeset shuml:1718030914745-1
CREATE TABLE chats
(
    id      UUID NOT NULL,
    task_id UUID,
    CONSTRAINT pk_chats PRIMARY KEY (id)
);

-- changeset shuml:1718030914745-2
CREATE TABLE messages
(
    id           UUID                        NOT NULL,
    content      VARCHAR(255)                NOT NULL,
    sender_login VARCHAR(255)                NOT NULL,
    parent_id    UUID,
    chat_id      UUID                        NOT NULL,
    sent_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_messages PRIMARY KEY (id)
);

-- changeset shuml:1718030914745-3
ALTER TABLE chats
    ADD CONSTRAINT uc_chats_task UNIQUE (task_id);

-- changeset shuml:1718030914745-4
ALTER TABLE chats
    ADD CONSTRAINT FK_CHATS_ON_TASK FOREIGN KEY (task_id) REFERENCES tasks (id);

-- changeset shuml:1718030914745-5
ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_CHAT FOREIGN KEY (chat_id) REFERENCES chats (id);

-- changeset shuml:1718030914745-6
ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_PARENT FOREIGN KEY (parent_id) REFERENCES messages (id);

