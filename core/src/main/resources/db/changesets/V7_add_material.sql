-- liquibase formatted sql

-- changeset shuml:1718296040393-1
ALTER TABLE topics
    ADD COLUMN material VARCHAR(15000) DEFAULT '' NOT NULL;

UPDATE topics
SET material = 'default value';

ALTER TABLE topics
    ALTER COLUMN material DROP DEFAULT;




