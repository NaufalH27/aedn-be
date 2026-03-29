CREATE TABLE example (
    id UUID PRIMARY KEY,
    example_string VARCHAR(255) NOT NULL,
    example_integer INTEGER,
    example_float REAL
);

INSERT INTO example (id, example_string, example_integer, example_float) VALUES (
    '550e8400-e29b-41d4-a716-446655440000',
    'hello world',
    42,
    3.14
);
