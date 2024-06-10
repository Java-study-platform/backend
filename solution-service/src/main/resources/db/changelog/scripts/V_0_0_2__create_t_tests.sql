CREATE TABLE tests
(
    id UUID NOT NULL ,
    test_index BIGINT NOT NULL ,
    test_input VARCHAR(100000) NOT NULL ,
    test_output VARCHAR(100000) ,
    test_time timestamp NOT NULL,
    status VARCHAR(255) NOT NULL,
    solution_id UUID NOT NULL ,
    CONSTRAINT pk_tests PRIMARY KEY (id)
);

ALTER TABLE tests
    ADD CONSTRAINT fk_tests_on_solution FOREIGN KEY (solution_id) REFERENCES solutions (id);

ALTER TABLE "tests" OWNER TO postgres;