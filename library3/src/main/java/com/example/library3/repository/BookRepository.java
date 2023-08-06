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
        try (PreparedStatement preparedStatement = conn.prepareStatement(
                "DELETE FROM usersbooks WHERE user_id = ? AND book_id = ?")
        ) {
            preparedStatement.setLong(1, Library3Application.getCurrentUser().getId());
            preparedStatement.setLong(2, bookId);
            if (preparedStatement.executeUpdate() > 0) {
                result = true;
            }
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
        try (PreparedStatement preparedStatement = conn.prepareStatement(
                "UPDATE books SET copiesAvailable = ? WHERE book_id = ?")
        ) {
            preparedStatement.setInt(1, (findBookById(bookById).getCopiesAvailable()) + quantityToAdd);
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

    private Book ExtractBookFromResultSet1(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setBook_id(rs.getLong(1));
        book.setTitle(rs.getString(2));
        book.setAuthorName(rs.getString(3));

        return book;
    }

    public List<Book> findBookByAuthor(String author) throws SQLException {
        List<Book> books = new ArrayList<>();
        String authorForQuery = "%"+author+"%";
        String query = "SELECT * FROM books WHERE books.authorName LIKE ? ";
        return getBooks(authorForQuery, books, query);
    }

    private List<Book> getBooks(String detail, List<Book> books, String query) throws SQLException {

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

    public List<Book> findBookByTitle(String title) throws SQLException {
        List<Book> books = new ArrayList<>();
        String titleForQuery = "%"+title+"%";
        String query = "SELECT * FROM books WHERE books.title LIKE ? ";

        return getBooks(titleForQuery, books, query);
    }

    public Map<User, Set<Book>> getBooksByTheirUsers() throws SQLException {

        String query = "SELECT books.book_id, books.title, books.authorName, users.user_id, users.userName FROM users " +
                "INNER JOIN usersbooks ON users.user_id=usersbooks.user_id " +
                "INNER JOIN books ON books.book_id=usersbooks.book_id";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();

        if (!rs.next()) {
            System.out.println("System error!");
        }

        User user = new User();
        user.setId(rs.getLong(4));
        user.setUserName(rs.getString(5));

        Set<Book> SetOfBooks = new HashSet<>();
        SetOfBooks.add(ExtractBookFromResultSet1(rs));
        Map<User, Set<Book>> usersBooks = new HashMap<>();
        usersBooks.put(user, SetOfBooks);

        while (rs.next()) {

            Book book = ExtractBookFromResultSet1(rs);

            for (User us : usersBooks.keySet()) {
                Long userId = rs.getLong(4);
                if (!Objects.equals(us.getId(), userId)) {
                    User u = new User();
                    u.setId(rs.getLong(4));
                    u.setUserName(rs.getString(5));
                    Set<Book> setOfBooks = new HashSet<>();
                    setOfBooks.add(book);
                    usersBooks.put(u, setOfBooks);
                } else {
                    Set<Book> usersBookSet = usersBooks.get(us);
                    usersBookSet.add(book);
                    usersBooks.put(us, usersBookSet);
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
            setOfBooks.add(ExtractBookFromResultSet1(rs));
            while (rs.next()) {
                setOfBooks.add(ExtractBookFromResultSet1(rs));
            }
        }
        return setOfBooks;
    }
}

