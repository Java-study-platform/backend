-- liquibase formatted sql

-- changeset shuml:1718296040393-1
ALTER TABLE topics
    ADD material VARCHAR(15000);

-- changeset shuml:1718296040393-2
ALTER TABLE topics
    ALTER COLUMN material SET NOT NULL;

