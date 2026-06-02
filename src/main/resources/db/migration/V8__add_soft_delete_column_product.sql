ALTER TABLE product
ADD COLUMN is_deleted BOOLEAN DEFAULT FALSE,
DROP CONSTRAINT product_user_id_fkey;

ALTER TABLE product
ADD CONSTRAINT product_user_id_fkey
FOREIGN KEY (user_id)
REFERENCES users(id);

ALTER TABLE category 
ADD COLUMN is_deleted BOOLEAN DEFAULT FALSE;

ALTER TABLE product
ADD COLUMN deleted_at TIMESTAMPTZ;
