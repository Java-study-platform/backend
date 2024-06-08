-- SQL file containing example data

-- categories
-- changeset shuml:1717415748938-6
INSERT INTO categories (id, name, description, author_login, create_time, modified_date)
VALUES
    ('9e5415b5-17c0-4d80-bea5-2b94391fe500', 'Java Core', 'Это база', 'author', '2024-06-08 10:00:00', '2024-06-08 10:00:00'),
    ('4c280dba-52a8-4906-bc05-ff9ce20cc3a5', 'Java Collections', 'Это тоже база', 'author', '2024-06-08 10:05:00', '2024-06-08 10:05:00');

-- topics
-- changeset shuml:1717415748938-7
INSERT INTO topics (id, name, category_id, author_login, create_time, modified_date)
VALUES
    ('845f68a5-8ae5-48b7-a834-abd906199ed3', 'Data Types', '9e5415b5-17c0-4d80-bea5-2b94391fe500', 'author', '2024-06-08 10:10:00', '2024-06-08 10:10:00'),
    ('accecbaa-b53f-4083-811d-c152a1444b75', 'Java Basic Syntax', '9e5415b5-17c0-4d80-bea5-2b94391fe500', 'author', '2024-06-08 10:15:00', '2024-06-08 10:15:00'),
    ('06a053a4-aef6-4fb0-a6c7-85957f51b609', 'Sets', '4c280dba-52a8-4906-bc05-ff9ce20cc3a5', 'author', '2024-06-08 10:20:00', '2024-06-08 10:20:00');

-- tasks
-- changeset shuml:1717415748938-8
INSERT INTO tasks (id, name, description, experience_amount, topic_id, author_login, create_time, modified_date)
VALUES
    ('c844ad85-f607-414f-af2a-6250a1f488ca', 'Sum two numbers', 'Sum to numbers', 200, '845f68a5-8ae5-48b7-a834-abd906199ed3', 'author', '2024-06-08 10:30:00', NULL);

-- test_cases
-- changeset shuml:1717415748938-9
INSERT INTO test_cases (id, index, expected_input, expected_output, task_id)
VALUES
    ('b616c035-3e96-4eec-80e6-6e84da3b1d3f', 1, '5 15', '20', 'c844ad85-f607-414f-af2a-6250a1f488ca');