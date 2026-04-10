CREATE TABLE product (
  id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  price BIGINT NOT NULL,
  currency_code CHAR(3) NOT NULL,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  quantity int NOT NULL,
  created_at TIMESTAMPTZ NOT NULL,
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  url_slug VARCHAR(255)
);

CREATE TABLE product_picture (
  id UUID PRIMARY KEY,
  product_id BIGINT NOT NULL REFERENCES product(id) ON DELETE CASCADE,
  url TEXT NOT NULL,
  position INT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL,
  UNIQUE (product_id, position)
);
