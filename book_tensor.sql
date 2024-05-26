CREATE TABLE book_tensor (
    id SERIAL PRIMARY KEY,
    tensor_data FLOAT[825]
);

INSERT INTO book_tensor (tensor_data)
VALUES ('{1.0, 2.0, 3.0, ..., 825.0}');