CREATE TABLE solutions (
                       id UUID PRIMARY KEY,
                       solution_code VARCHAR(100000) NOT NULL,
                       username VARCHAR(255) NOT NULL,
                       task_id UUID NOT NULL,
                       test_index BIGINT NOT NULL,
                       status VARCHAR(255) NOT NULL,
                       user_code_answer VARCHAR(10000)
);

ALTER TABLE "solutions" OWNER TO postgres;