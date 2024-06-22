INSERT INTO tasks (id, name, description, experience_amount, topic_id, author_login, create_time, modified_date,
                   time_limit)
VALUES ('ef6e392e-317c-4d37-8fb0-851b9460e3aa', 'Вывод n-ого простого числа на Java',
        'Эта тестирующая система предназначена для проверки правильности выполнения задачи по Java, в которой требуется вывести n-ое простое число. Число n предоставляется системой. Система включает в себя набор тестов, которые проверяют корректность алгоритма генерации простых чисел и его производительность при различных значениях n.',
        1000,
        'accecbaa-b53f-4083-811d-c152a1444b75',
        'author',
        '2024-06-19 20:08:00',
        '2024-06-19 20:08:00',
        1000);

INSERT INTO test_cases (id, index, expected_input, expected_output, task_id)
VALUES ('46fd320b-806f-489e-9298-edf3f1e6ecb2', 1, '7', '17', 'ef6e392e-317c-4d37-8fb0-851b9460e3aa'),
       ('ba4a4c51-9d98-43c5-a37d-edb5e5f38483', 2, '15000', '163841', 'ef6e392e-317c-4d37-8fb0-851b9460e3aa'),
       ('f8fec873-3ab1-40c1-ad3a-4e73979b9486', 3, '5000000', '86028121', 'ef6e392e-317c-4d37-8fb0-851b9460e3aa');