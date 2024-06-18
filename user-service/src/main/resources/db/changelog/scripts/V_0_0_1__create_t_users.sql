CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       keycloak_id VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       experience BIGINT NOT NULL ,
                       username VARCHAR(255) NOT NULL,
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL
);

CREATE TABLE user_roles (
                            user_id UUID NOT NULL,
                            roles VARCHAR(255) NOT NULL,
                            CONSTRAINT fk_user_roles_user_id FOREIGN KEY (user_id) REFERENCES users(id)
);
ALTER TABLE "users" OWNER TO postgres;
ALTER TABLE "user_roles" OWNER TO postgres;