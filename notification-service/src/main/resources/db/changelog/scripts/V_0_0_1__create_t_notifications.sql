CREATE TABLE notifications(
    id UUID PRIMARY KEY ,
    title VARCHAR(255) NOT NULL ,
    content VARCHAR(10000) NOT NULL ,
    user_email VARCHAR(255) NOT NULL ,
    create_time timestamp NOT NULL ,
    is_read boolean NOT NULL
);