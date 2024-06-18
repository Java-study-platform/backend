-- liquibase formatted sql

-- changeset shuml:1718361918042-1
ALTER TABLE messages
    ALTER COLUMN content TYPE VARCHAR(6000) USING (content::VARCHAR(6000));

