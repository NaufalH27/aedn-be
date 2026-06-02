ALTER TABLE category
DROP CONSTRAINT category_name_user_id_key,
ADD CONSTRAINT category_name_key UNIQUE (name),
DROP CONSTRAINT category_user_id_fkey,
DROP COLUMN user_id;
