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


    public void saveBook(Book book) throws SQLException {
        String query = "INSERT INTO books (authorName, title, copies, copiesAvailable) VALUES (?,?,?,?)";

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
        String query = "SELECT * FROM books WHERE title = ? AND authorName = ?";

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
        preparedStatement.setLong(1, Library3Application.getCurrentUser().getId());
        preparedStatement.setLong(2, bookId);

        preparedStatement.executeUpdate();
    }

    public boolean removeUsersBookById(Long bookId) throws SQLException {
        boolean result = false;
        String query = "DELETE FROM usersbooks WHERE user_id = ? AND book_id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);

        preparedStatement.setLong(1, Library3Application.getCurrentUser().getId());
        preparedStatement.setLong(2, bookId);
        if (preparedStatement.executeUpdate() > 0) {
            result = true;
        }

        return result;
    }

    public boolean findBookIdByUserId(Long userId, Long bookId) throws SQLException {
        boolean result = false;
        String query = "SELECT * FROM usersbooks WHERE user_id = ? AND book_id = ?";

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
        String query = "SELECT copiesAvailable FROM books WHERE book_id = ?";

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
        String query = "SELECT * FROM books WHERE book_id = ?";

        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setLong(1, id);

        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            book = ExtractBookFromResultSet(rs);
        }
        return book;
    }

    public boolean updateBookCopies(Long bookById, int quantityToAdd) throws SQLException {
        boolean result = false;
        String query = "UPDATE books SET copiesAvailable = ? WHERE book_id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, (findBookById(bookById).getCopiesAvailable()) + quantityToAdd);
        preparedStatement.setLong(2, bookById);
        if (preparedStatement.executeUpdate() > 0) {
            result = true;
        }
        return result;
    }

    public List<Book> findAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books";
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

    private Book extractBookFromResultSet1(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setBook_id(rs.getLong(1));
        book.setTitle(rs.getString(2));
        book.setAuthorName(rs.getString(3));

        return book;
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong(4));
        user.setUserName(rs.getString(5));

        return user;
    }

    public List<Book> findBookByAuthor(String author) throws SQLException {

        String query = "SELECT * FROM books WHERE books.authorName LIKE ? ";
        return getBooks("%" + author + "%", query);
    }

    public List<Book> findBookByTitle(String title) throws SQLException {

        String query = "SELECT * FROM books WHERE books.title LIKE ? ";
        return getBooks("%" + title + "%", query);
    }

    private List<Book> getBooks(String detail, String query) throws SQLException {
        List<Book> books = new ArrayList<>();
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, detail);

        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            books.add(ExtractBookFromResultSet(rs));
            while (rs.next()) {
                books.add(ExtractBookFromResultSet(rs));
            }
        }
        return books;
    }

    public Map<User, Set<Book>> getBooksByTheirUsers() throws SQLException {

        String query = "SELECT books.book_id, books.title, books.authorName, users.user_id, users.userName FROM users " +
                "INNER JOIN usersbooks ON users.user_id = usersbooks.user_id " +
                "INNER JOIN books ON books.book_id = usersbooks.book_id";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();

        Map<User, Set<Book>> usersBooks = new HashMap<>();

        if (rs.next()) {
            User user = extractUserFromResultSet(rs);
            Book book = extractBookFromResultSet1(rs);

            Set<Book> SetOfBooks = new HashSet<>();
            SetOfBooks.add(book);

            usersBooks.put(user, SetOfBooks);

            while (rs.next()) {
                User user1 = extractUserFromResultSet(rs);
                Book book1 = extractBookFromResultSet1(rs);

                if (usersBooks.containsKey(user1)) {
                    usersBooks.get(user1).add(book1);
                    usersBooks.put(user1, usersBooks.get(user1));

                } else {
                    Set<Book> SetOfBooks1 = new HashSet<>();
                    SetOfBooks1.add(book1);
                    usersBooks.put(user1, SetOfBooks1);
                }
            }
        }
            return usersBooks;
        }


    public Set<Book> getUsersBooks(Long userId) throws SQLException {
        Set<Book> setOfBooks = new HashSet<>();

        String query = "SELECT books.book_id, books.title, books.authorName FROM users " +
                "INNER JOIN usersbooks ON users.user_id = usersbooks.user_id " +
                "INNER JOIN books ON books.book_id = usersbooks.book_id WHERE users.user_id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setLong(1, userId);
        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {
            setOfBooks.add(extractBookFromResultSet1(rs));
            while (rs.next()) {
                setOfBooks.add(extractBookFromResultSet1(rs));
            }
        }
        return setOfBooks;
    }
}

