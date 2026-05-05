CREATE TABLE payment_method (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE request (
    id UUID PRIMARY KEY,
    request_number_id VARCHAR(255) UNIQUE NOT NULL,
    user_id UUID NOT NULL,
    product_title VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id),
    product_id UUID NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(id),
    currency_code VARCHAR(5),
    proposed_deadline TIMESTAMPTZ NOT NULL,
    proposed_price BIGINT,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    status VARCHAR(50), -- pending proposed rejected
    extra_info VARCHAR(255),
    created_at TIMESTAMPTZ
);

CREATE TABLE orders (
    request_id UUID PRIMARY KEY,
    FOREIGN KEY (request_id) REFERENCES request(id),
    payment_method_id INT,
    FOREIGN KEY (payment_method_id) REFERENCES payment_method(id),
    email VARCHAR(255),
    status VARCHAR(255), -- pending, cancelled, processing, done
    deadline TIMESTAMPTZ NOT NULL,
    product_title VARCHAR(255),
    price BIGINT,
    currency_code VARCHAR(3),
    isPaid boolean,
    paid_at TIMESTAMPTZ,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    created_at TIMESTAMPTZ 
);
