-- liquibase formatted sql

-- changeset shuml:1718284503479-1
CREATE TABLE message_reactions
(
    message_id     UUID     NOT NULL,
    reaction_count INTEGER,
    reaction_type  SMALLINT NOT NULL,
    CONSTRAINT pk_message_reactions PRIMARY KEY (message_id, reaction_type)
);

-- changeset shuml:1718284503479-2
CREATE TABLE reaction_types
(
    reaction_id   UUID NOT NULL,
    reaction_type VARCHAR(255)
);

-- changeset shuml:1718284503479-3
CREATE TABLE reactions
(
    id           UUID         NOT NULL,
    message_id   UUID         NOT NULL,
    author_login VARCHAR(255) NOT NULL,
    CONSTRAINT pk_reactions PRIMARY KEY (id)
);

-- changeset shuml:1718284503479-4
ALTER TABLE messages
    ADD event_type VARCHAR(255);

-- changeset shuml:1718284503479-5
ALTER TABLE reactions
    ADD CONSTRAINT FK_REACTIONS_ON_MESSAGE FOREIGN KEY (message_id) REFERENCES messages (id);

-- changeset shuml:1718284503479-6
ALTER TABLE message_reactions
    ADD CONSTRAINT fk_message_reactions_on_message FOREIGN KEY (message_id) REFERENCES messages (id);

-- changeset shuml:1718284503479-7
ALTER TABLE reaction_types
    ADD CONSTRAINT fk_reaction_types_on_reaction FOREIGN KEY (reaction_id) REFERENCES reactions (id);

