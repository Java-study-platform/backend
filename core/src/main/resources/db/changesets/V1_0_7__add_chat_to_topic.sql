-- liquibase formatted sql

-- changeset shuml:1718336617106-4
ALTER TABLE chats
    DROP CONSTRAINT fk_chats_on_task;

-- changeset shuml:1718336617106-1
ALTER TABLE chats
    ADD topic_id UUID;

-- changeset shuml:1718336617106-2
ALTER TABLE chats
    ADD CONSTRAINT uc_chats_topic UNIQUE (topic_id);

-- changeset shuml:1718336617106-3
ALTER TABLE chats
    ADD CONSTRAINT FK_CHATS_ON_TOPIC FOREIGN KEY (topic_id) REFERENCES topics (id);

-- changeset shuml:1718336617106-6
ALTER TABLE chats
    DROP COLUMN task_id;

