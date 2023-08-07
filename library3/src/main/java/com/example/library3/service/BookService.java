package com.example.library3.service;

import com.example.library3.Library3Application;
import com.example.library3.model.Book;
import com.example.library3.model.User;
import com.example.library3.repository.BookRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class BookService {
    private final BookRepository bookRepository;

    public BookService() {
        this.bookRepository = new BookRepository();
    }


    public String addBookToTheLibrary(Book book) {
        String message;
        try {
            bookRepository.saveBook(book);
            message = "Book added successfully";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

    public String removeBookById(Long bookId) {
        String message;
        try {
            if (bookRepository.deleteBookById(bookId)) {
                message = "Book removed successfully.";
            } else {
                message = "Book was not found.";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

    public String findBookByAuthorAndTitle(Book details) {
        String message;

        try {
            Long result = bookRepository.findBookByAuthorAndTitle(details);
            if (result != 0L) {
                message = "We have this book. Id: " + result;
            } else {
                message = "The book was not found.";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return message;
    }

    public String borrowBook(Book bookDetails) {

        String message = "";
        try {
            Long bookId = bookRepository.findBookByAuthorAndTitle(bookDetails);
            if (bookAvailabilityChecker(bookDetails)) {
                bookRepository.addBookToUser(bookId);
                bookRepository.updateBookCopies(bookId, -1);
                message = "Book borrowed successfully.";
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

    public boolean bookAvailabilityChecker(Book details) throws SQLException {
        boolean result = true;

        Long bookIdCheck = bookRepository.findBookByAuthorAndTitle(details);
        if (bookIdCheck != 0L) {
            if (bookRepository.getCopiesAvailableByBookId(bookIdCheck) < 1) {
                System.out.println("Copies of this book are currently unavailable.");
                result = false;
            }
            boolean userAccCheck = bookRepository.findBookIdByUserId(Library3Application.getCurrentUser().getId(), bookIdCheck);

            if (userAccCheck) {
                System.out.println("You already have a copy of this book.");
                result = false;
            }
        } else {
            System.out.println("Book was not found by author or title.");
            result = false;
        }

        return result;
    }

    public String returnBookById(Long bookId) {
        String message;
        try {
            boolean result1 = bookRepository.removeUsersBookById(bookId);
            boolean result2 = bookRepository.updateBookCopies(bookId, 1);
            if (result1 && result2) {
                message = "Book returned successfully.";
            } else {
                message = "Error: Book could not be returned.";
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

    public List<Book> findAllBooks() throws SQLException {
        return bookRepository.findAllBooks();
    }

    public List<Book> findBookByAuthor(String author) throws SQLException {
        return bookRepository.findBookByAuthor(author);
    }

    public List<Book> findBookByTitle(String title) throws SQLException {
        return bookRepository.findBookByTitle(title);
    }

    public Map<User, Set<Book>> getBorrowingStats() throws SQLException {
        return bookRepository.getBooksByTheirUsers();
    }

    public Set<Book> findBooksByUser(Long id) throws SQLException {
        return bookRepository.getUsersBooks(id);
    }
}
