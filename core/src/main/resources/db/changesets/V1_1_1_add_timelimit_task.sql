INSERT INTO tasks (id, name, description, experience_amount, topic_id, author_login, create_time, modified_date)
VALUES ('ef6e392e-317c-4d37-8fb0-851b9460e3aa', 'Вывод n-ого простого числа на Java',
        'Эта тестирующая система предназначена для проверки правильности выполнения задачи по Java, в которой требуется вывести n-ое простое число. Число n предоставляется системой. Система включает в себя набор тестов, которые проверяют корректность алгоритма генерации простых чисел и его производительность при различных значениях n.',
        1000,
        'accecbaa-b53f-4083-811d-c152a1444b75',
        'author',
        '2024-06-19 20:08:00',
        '2024-06-19 20:08:00');

INSERT INTO test_cases (id, index, expected_input, expected_output, task_id)
VALUES ('46fd320b-806f-489e-9298-edf3f1e6ecb2', 1, '7', '17', 'ef6e392e-317c-4d37-8fb0-851b9460e3aa'),
       ('b616c035-3e96-4eec-80e6-6e84da3b1d3f', 2, '15000', '163841', 'ef6e392e-317c-4d37-8fb0-851b9460e3aa');