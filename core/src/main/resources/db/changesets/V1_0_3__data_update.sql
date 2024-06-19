DELETE
FROM test_cases
WHERE id = 'b616c035-3e96-4eec-80e6-6e84da3b1d3f';

INSERT INTO test_cases (id, index, expected_input, expected_output, task_id)
VALUES ('b616c035-3e96-4eec-80e6-6e84da3b1d3f', 1, E'5\n15', E'20\n', 'c844ad85-f607-414f-af2a-6250a1f488ca');