CREATE TABLE IF NOT EXISTS quantity_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_email VARCHAR(255),
    quantity_type VARCHAR(50),
    input_value DOUBLE,
    input_unit VARCHAR(50),
    target_unit VARCHAR(50),
    result_value DOUBLE
);