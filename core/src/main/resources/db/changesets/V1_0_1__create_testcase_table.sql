CREATE TABLE test_cases
(
    id              UUID         NOT NULL,
    index           bigint       NOT NULL,
    expected_input  VARCHAR(1000000) NOT NULL,
    expected_output VARCHAR(1000000) NOT NULL,
    task_id         UUID         NOT NULL,
    CONSTRAINT pk_test_cases PRIMARY KEY (id)
);

ALTER TABLE test_cases
    ADD CONSTRAINT fk_test_cases_on_task FOREIGN KEY (task_id) REFERENCES tasks (id);