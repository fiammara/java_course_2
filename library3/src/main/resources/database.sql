CREATE database IF NOT EXISTS library;
USE library;

CREATE TABLE IF NOT EXISTS books (
    book_id int not null auto_increment,
    authorName varchar(100) not null,
    title varchar(100) not null,
    copies int not null ,
    copiesAvailable int,
    PRIMARY KEY (book_id)
    );
CREATE TABLE IF NOT EXISTS users (
    user_id int not null auto_increment,
    userName varchar(100) not null,
    password varchar(100) not null,
    email varchar(100) not null,
    PRIMARY KEY (user_id)
    );

CREATE TABLE IF NOT EXISTS usersBooks (
    user_id INT (11),
    book_id INT (11),
    PRIMARY KEY (user_id, book_id),
    CONSTRAINT Constr_usersBooks_users_fk
    FOREIGN KEY (user_id) REFERENCES users (user_id)
    ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT Constr_usersBooks_books_fk
    FOREIGN KEY (book_id) REFERENCES books (book_id)
    ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=INNODB CHARACTER SET ascii COLLATE ascii_general_ci;

INSERT INTO books (authorName, title, copies, copiesAvailable) VALUES ('F. Scott Fitzgerald','The Great Gatsby', 1, 1);
INSERT INTO books (authorName, title, copies, copiesAvailable) VALUES ('Jane Austen','Pride and Prejudice', 3, 3);
INSERT INTO books (authorName, title, copies, copiesAvailable) VALUES ('Suzanne Collins','The Hunger Games', 2, 2);
INSERT INTO books (authorName, title, copies, copiesAvailable) VALUES ('Marcel Proust','In Search of Lost Time', 1, 1);
INSERT INTO books (authorName, title, copies, copiesAvailable) VALUES ('James Joyce','Ulysses', 5, 5);
INSERT INTO books (authorName, title, copies, copiesAvailable) VALUES ('Miguel de Cervantes','Don Quixote', 4, 4);
INSERT INTO books (authorName, title, copies, copiesAvailable) VALUES ('Gabriel Garcia Marquez','One Hundred Years of Solitude', 2, 2);
INSERT INTO books (authorName, title, copies, copiesAvailable) VALUES ('Herman Melville','Moby Dick', 6, 6);
INSERT INTO books (authorName, title, copies, copiesAvailable) VALUES ('Leo Tolstoy','War and Peace', 4, 4);
INSERT INTO books (authorName, title, copies, copiesAvailable) VALUES ('William Shakespeare','Hamlet', 1, 1);
INSERT INTO books (authorName, title, copies, copiesAvailable) VALUES ('Stephenie Meyer','Twilight', 0, 0);
INSERT INTO books (authorName, title, copies, copiesAvailable) VALUES ('John Green','The Fault in Our Stars', 1, 1);
INSERT INTO books (authorName, title, copies, copiesAvailable) VALUES ('George Orwell','1984', 1, 1);
INSERT INTO books (authorName, title, copies, copiesAvailable) VALUES ('Veronica Roth','Divergent', 2, 2);