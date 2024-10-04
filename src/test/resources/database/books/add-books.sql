DELETE FROM books;

INSERT INTO books (id, title, author, isbn, price, description, cover_image)
VALUES
    (1, 'Book 1', 'Author 1', '1111111111', 11, 'Description for Book 1', 'cover_image_1.jpg'),
    (2, 'Book 2', 'Author 2', '2222222222', 16, 'Description for Book 2', 'cover_image_2.jpg'),
    (3, 'Book 3', 'Author 3', '3333333333', 21, 'Description for Book 3', 'cover_image_3.jpg');
