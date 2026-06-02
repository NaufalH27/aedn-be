CREATE TABLE request (
    id UUID PRIMARY KEY,
    request_number_id VARCHAR(255) UNIQUE NOT NULL,
    user_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),

    product_id UUID NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(id),

    currency_code VARCHAR(5) NOT NULL,
    proposed_deadline TIMESTAMPTZ NOT NULL,
    proposed_price BIGINT,

    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,

    status VARCHAR(50) NOT NULL, -- pending proceed rejected accepted

    extra_info VARCHAR(255),
    created_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE orders (
    id UUID PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES request(id),

    deadline TIMESTAMPTZ NOT NULL,
    price BIGINT NOT NULL,
    currency_code VARCHAR(3),

    status VARCHAR(255) NOT NULL, -- pending_payment in_progress cancelled done
    paid_status VARCHAR(20) NOT NULL DEFAULT 'unpaid' CHECK (paid_status IN ('paid', 'unpaid', 'skipped')),
    paid_at TIMESTAMPTZ,

    rating INT CHECK (rating BETWEEN 1 AND 5),
    created_at TIMESTAMPTZ
);

CREATE TABLE orders_drawing_progress (
    id UUID PRIMARY KEY,
    src_url_key TEXT NOT NULL,
    position INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    orders_id UUID REFERENCES orders(id) ON DELETE CASCADE NOT NULL,
    created_at TIMESTAMPTZ,
    UNIQUE (orders_id, name, position)
)
