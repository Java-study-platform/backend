ALTER TABLE tasks
    ADD COLUMN time_limit BIGINT NOT NULL DEFAULT 1000;

UPDATE tasks
SET time_limit = 1000;

INSERT INTO tasks (id, name, description, experience_amount, topic_id, author_login, create_time, modified_date,
                   time_limit)
VALUES ('cc9aa3e0-26d9-11ef-9e35-0800200c9a66', 'Sort array', 'You need to sort array', 200,
        'accecbaa-b53f-4083-811d-c152a1444b75', 'author', '2024-06-08 10:20:00', NULL, 1000),
       ('d0fdfe00-26d9-11ef-9e35-0800200c9a66', 'Palindrome', 'You need to find palindrome', 200,
        '845f68a5-8ae5-48b7-a834-abd906199ed3', 'author', '2024-06-08 10:20:00', NULL, 1000);

INSERT INTO test_cases (id, index, expected_input, expected_output, task_id)
VALUES ('d2c3b8bf-b6e8-4db9-993a-b9a34c932e5d', 1, E '3\nabc\ncbc\naba', E 'NO\nYES\nYES',
        'd0fdfe00-26d9-11ef-9e35-0800200c9a66'),
       ('b75835bd-bb1c-4def-b77a-51ac1cd8b2ec', 1, E '3\n3\n5 4 3\n3\n3 2 1\n2\n2 1', E '3 4 5\n1 2 3\n1 2',
        'cc9aa3e0-26d9-11ef-9e35-0800200c9a66');

DELETE
FROM test_cases
WHERE id = 'b616c035-3e96-4eec-80e6-6e84da3b1d3f';

INSERT INTO test_cases (id, index, expected_input, expected_output, task_id)
VALUES ('b616c035-3e96-4eec-80e6-6e84da3b1d3f', 1, E '5\n15', E '20', 'c844ad85-f607-414f-af2a-6250a1f488ca');