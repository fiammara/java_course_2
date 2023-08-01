CREATE database IF NOT EXISTS library;
USE library;

CREATE TABLE IF NOT EXISTS books (
    book_id int not null auto_increment,
    authorName varchar(100) not null,
    title varchar(100) not null,
    copies int not null ,
    copiesAvailable int,
    primary key(book_id)
    );
CREATE TABLE IF NOT EXISTS users (
    user_id int not null auto_increment,
    userName varchar(100) not null,
    password varchar(100) not null,
    email varchar(100) not null,
    primary key(user_id)
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
    ) ENGINE=INNODB CHARACTER SET ascii COLLATE ascii_general_ci