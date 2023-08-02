package com.example.library3.repository;

import com.example.library3.Library3Application;
import com.example.library3.model.Book;
import com.example.library3.model.User;

import java.sql.*;
import java.util.*;


public class BookRepository {
    DatabaseConnection databaseConnection;
    Connection conn;

    public BookRepository() {
        databaseConnection = new DatabaseConnection();
        conn = databaseConnection.getConnection();
    }


    public void save(Book book) throws SQLException {
        String query = "INSERT INTO books (authorName, title, copies, copiesAvailable) VALUES (?,?, ?,?)";

        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, book.getAuthorName());
        preparedStatement.setString(2, book.getTitle());
        preparedStatement.setInt(3, book.getCopies());
        preparedStatement.setInt(4, book.getCopiesAvailable());

        preparedStatement.executeUpdate();

    }

    public boolean deleteBookById(Long bookId) throws SQLException {
        boolean result = false;
        String query = "DELETE FROM books WHERE book_id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setLong(1, bookId);
        if (preparedStatement.executeUpdate() > 0) {
            result = true;
        }
        return result;
    }

    public Long findBookByAuthorAndTitle(Book book) throws SQLException {
        long bookId = 0L;
        String query = "select * from books where title = ? and authorName = ?";

        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, book.getTitle());
        preparedStatement.setString(2, book.getAuthorName());

        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            bookId = rs.getLong(1);

        }
        return bookId;
    }

    public void addBookToUser(Long bookId) throws SQLException {

        String query = "INSERT INTO usersbooks (user_id, book_id) VALUES (?,?)";

        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setLong(1, Library3Application.getCurrent().getId());
        preparedStatement.setLong(2, bookId);

        preparedStatement.executeUpdate();
    }

    public boolean removeUsersBookById(Long bookId) throws SQLException {
        boolean result = false;
        try (PreparedStatement preparedStatement = conn.prepareStatement(
                "DELETE FROM usersbooks WHERE user_id = ? and book_id = ?")
        ) {
            preparedStatement.setLong(1, Library3Application.getCurrent().getId());
            preparedStatement.setLong(2, bookId);
            if (preparedStatement.executeUpdate() > 0) {
                result = true;
            }
        }
        return result;
    }

    public boolean findBookIdByUserId(Long userId, Long bookId) throws SQLException {
        boolean result = false;
        String query = "select * from usersbooks where user_id = ? and book_id=?";

        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setLong(1, userId);
        preparedStatement.setLong(2, bookId);

        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            result = true;
        }

        return result;
    }

    public int getCopiesAvailableByBookId(Long id) throws SQLException {

        int result = 0;
        String query = "select copiesAvailable from books where book_id = ?";

        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setLong(1, id);

        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {
            result = rs.getInt(1);
        }

        return result;
    }

    public Book findBookById(Long id) throws SQLException {
        Book book = null;
        String query = "select * from books where book_id = ?";

        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setLong(1, id);

        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            book = ExtractBookFromResultSet(rs);
        }
        return book;
    }

    public boolean updateBookCopies(Long bookById, int quantity) throws SQLException {
        boolean result = false;
        try (PreparedStatement preparedStatement = conn.prepareStatement(
                "update books set copiesAvailable= ? where book_id = ?")
        ) {
            preparedStatement.setInt(1, (findBookById(bookById).getCopiesAvailable()) + quantity);
            preparedStatement.setLong(2, bookById);
            if (preparedStatement.executeUpdate() > 0) {
                result = true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public List<Book> findAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "select * from books";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            books.add(ExtractBookFromResultSet(rs));
            while (rs.next()) {
                books.add(ExtractBookFromResultSet(rs));
            }
        }
        return books;
    }

    private Book ExtractBookFromResultSet(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setBook_id(rs.getLong(1));
        book.setAuthorName(rs.getString(2));
        book.setTitle(rs.getString(3));
        book.setCopies(rs.getInt(4));
        book.setCopiesAvailable(rs.getInt(5));

        return book;
    }

    public List<Book> findBookByAuthor(String author) throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "select * from books where authorName = ?";
        return getBooks(author, books, query);
    }

    private List<Book> getBooks(String author, List<Book> books, String query) throws SQLException {

        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, author);

        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            books.add(ExtractBookFromResultSet(rs));
            while (rs.next()) {
                books.add(ExtractBookFromResultSet(rs));
            }
        }
        return books;
    }

    public List<Book> findBookByTitle(String title) throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "select * from books where title = ?";

        return getBooks(title, books, query);
    }

    public Map<User, Set<Book>> borrowingStats() throws SQLException {

        String query = "SELECT users.user_id, users.userName, books.book_id, books.title, books.authorName FROM users " +
                "INNER JOIN usersbooks ON users.user_id=usersbooks.user_id " +
                "INNER JOIN books ON books.book_id=usersbooks.book_id";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();

        if (!rs.next()) {
            System.out.println("System error!");
        }
        Book book1 = new Book();


        book1.setBook_id(rs.getLong(3));
        book1.setTitle(rs.getString(4));
        book1.setAuthorName(rs.getString(5));

        User u1 = new User();
        u1.setId(rs.getLong(1));
        u1.setUserName(rs.getString(2));
        Set<Book> setOfB = new HashSet<>();
        setOfB.add(book1);
        Map<User, Set<Book>> usersBooks = new HashMap<>();
        usersBooks.put(u1, setOfB);

        while (rs.next()) {

            Book book = new Book();
            book.setBook_id(rs.getLong(3));
            book.setTitle(rs.getString(4));
            book.setAuthorName(rs.getString(5));

            for (User us : usersBooks.keySet()) {
                Long userid = rs.getLong(1);
                if (!Objects.equals(us.getId(), userid)) {
                    User u = new User();
                    u.setId(rs.getLong(1));
                    u.setUserName(rs.getString(2));
                    Set<Book> setOfBook = new HashSet<>();
                    setOfBook.add(book);
                    usersBooks.put(u, setOfBook);
                } else {
                    Set<Book> usrbookset = usersBooks.get(us);
                    usrbookset.add(book);
                    usersBooks.put(us, usrbookset);
                }
            }

        }
        return usersBooks;
    }

    public Set<Book> userStats(Long user_id) throws SQLException {
        Set<Book> setOfB = new HashSet<>();
        String query = "SELECT books.book_id, books.title, books.authorName FROM users " +
                "INNER JOIN usersbooks ON users.user_id=usersbooks.user_id " +
                "INNER JOIN books ON books.book_id=usersbooks.book_id where users.user_id=?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setLong(1, user_id);
        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {

            Book book1 = new Book();
            book1.setBook_id(rs.getLong(1));
            book1.setTitle(rs.getString(2));
            book1.setAuthorName(rs.getString(3));
            setOfB.add(book1);
            while (rs.next()) {

                Book book = new Book();
                book.setBook_id(rs.getLong(1));
                book.setTitle(rs.getString(2));
                book.setAuthorName(rs.getString(3));
                setOfB.add(book);
            }
        }
        return setOfB;
    }
}

