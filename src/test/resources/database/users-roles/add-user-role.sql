INSERT INTO users_roles (user_id, role_id)
VALUES ((SELECT id FROM users WHERE email='testUser@test.com'),
        (SELECT id FROM roles WHERE name='ROLE_USER'));