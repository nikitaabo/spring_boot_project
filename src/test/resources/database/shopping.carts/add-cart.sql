DELETE from users;

INSERT INTO users (id, email, password, first_name, last_name, shipping_address, is_deleted)
VALUES (1, 'testUser@test.com', 'password123', 'John', 'Doe', '123 Test St', false);

INSERT INTO shopping_carts (id, user_id)
VALUES (1, 1);
