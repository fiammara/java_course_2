package com.example.library3.controller;

import com.example.library3.Library3Application;
import com.example.library3.model.Book;
import com.example.library3.model.User;
import com.example.library3.service.BookService;
import com.example.library3.service.UserService;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;


public class MenuController {

    private final Scanner scanner = new Scanner(System.in);
    private final BookService bookService;
    private final UserService userService;

    public MenuController() {
        this.bookService = new BookService();
        this.userService = new UserService();
    }

    public void removeBookFromTheLibrary() {

        try {
            Long bookId = Long.valueOf(this.getInfo("Please enter and id of the book to remove: "));
            String result = bookService.removeBookById(bookId);
            displayMessage(result);
        } catch (Exception exception) {
            displayMessage("Error: " + exception.getMessage());
            this.displayMessage(exception.getMessage());
        } finally {
            if (this.getInfo("Do you want to try again? (yes / no):").equals("yes")) this.removeBookFromTheLibrary();
        }
    }


    public void addBookToTheLibrary() {

        try {
            Book book = this.collectBookInfo();
            String result = bookService.addBookToTheLibrary(book);
            displayMessage(result);
        } catch (Exception exception) {
            displayMessage("Error: " + exception.getMessage());
        } finally {
            if (this.getInfo("Do you want to add another book? (yes / no):").equals("yes")) this.addBookToTheLibrary();
        }
    }

    private Book collectBookInfo() {
        Book book = new Book();
        book.setAuthorName(getInfo("Please enter book author: "));
        book.setTitle(getInfo("Please enter book title: "));
        book.setCopies(Integer.parseInt(this.getInfo("Please enter book quantity: ")));
        book.setCopiesAvailable(book.getCopies());

        return book;
    }

    private Book collectBookInfo1() {
        Book book = new Book();
        book.setAuthorName(getInfo("Please enter book author: "));
        book.setTitle(getInfo("Please enter book title: "));

        return book;
    }

    public String getInfo(String message) {
        System.out.println(message);
        return scanner.nextLine();
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void registerNewUser() {

        try {
            User user = this.collectNewUserInfo();
            String result = userService.registerUser(user);
            displayMessage(result);
        } catch (Exception exception) {
            displayMessage("Error: " + exception.getMessage());
            if (this.getInfo("Do you want to try register again? (yes / no):").equals("yes")) this.registerNewUser();
        }
    }

    private User collectNewUserInfo() {
        User user = new User();
        user.setUserName(getInfo("Please enter your name: "));
        user.setPassword((getInfo("Please enter your password: ")));
        user.setEmail(getInfo("Please enter your email: "));

        return user;
    }

    private User collectLoginInfo() {
        User user = new User();
        String userName = getInfo("Please enter your name: ");
        String password = getInfo("Please enter your password: ");
        user.setUserName(userName);
        user.setPassword(password);
        return user;
    }

    public void loginUser() {

        try {
            User user = this.collectLoginInfo();
            String result = userService.loginUser(user);

            displayMessage(result);
        } catch (Exception exception) {
            displayMessage("Error: " + exception.getMessage());
            if (this.getInfo("Do you want to login as another user? (yes / no):").equals("yes")) this.loginUser();
        }
    }

    public void borrowABook() {

        User user = Library3Application.getCurrent();
        try {
            if (userService.checkUserBooks(user.getId()) > 4) {
                System.out.println("You already have more than 4 books");
            } else {
                System.out.println("You can borrow a book! Please write a book author or title: ");
                try {
                    Book book = this.collectBookInfo1();
                    String result = bookService.borrowBook(book);
                    displayMessage(result);
                } catch (Exception exception) {
                    displayMessage("Error: " + exception.getMessage());
                } finally {
                    if (this.getInfo("Do you want to try again? (yes / no):").equals("yes")) this.borrowABook();
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void returnABook() {

        try {
            Long bookId = Long.valueOf(this.getInfo("Please enter book id to return: "));
            String result = bookService.returnBookById(bookId);
            displayMessage(result);

        } catch (Exception exception) {
            this.displayMessage(exception.getMessage());
        }
    }

    public void findABookByAuthorAndTitle() {

        try {
            Book book = this.collectBookInfo1();
            String result = bookService.findBookByAuthorAndTitle(book);
            displayMessage(result);
        } catch (Exception exception) {
            displayMessage("Error: " + exception.getMessage());
        } finally {
            if (this.getInfo("Do you want to search for another book? (yes / no):").equals("yes"))
                this.findABookByAuthorAndTitle();
        }

    }

    public void seeAllBooksInThisLibrary() {

        try {

            List<Book> books = bookService.findAllBooks();

            String output;
            StringBuilder sb = new StringBuilder();
            sb.append("<html><table cellspacing=10>");
            sb.append("<tr>");
            sb.append("<td>").append("BOOK ID").append("</td>");
            sb.append("<td>").append("AUTHOR").append("</td>");
            sb.append("<td>").append("TITLE").append("</td>");
            sb.append("<td>").append("COPIES").append("</td>");
            sb.append("<td>").append("COPIES AVAILABLE").append("</td>");
            sb.append("</tr>");


            sb.append("<tr>");
            sb.append("<td>").append("___").append("</td>");
            sb.append("<td>").append("___").append("</td>");
            sb.append("<td>").append("___").append("</td>");
            sb.append("<td>").append("___").append("</td>");
            sb.append("<td>").append("___").append("</td>");
            sb.append("</tr>");
            for (Book book : books) {
                sb.append("<tr>");

                sb.append("<td>").append(book.getBook_id()).append("</td>");
                sb.append("<td>").append(book.getAuthorName()).append("</td>");
                sb.append("<td>").append(book.getTitle()).append("</td>");
                sb.append("<td>").append(book.getCopies()).append("</td>");
                sb.append("<td>").append(book.getCopiesAvailable()).append("</td>");

            }
            sb.append("</tr></table></html>");
            output = sb.toString();
            JFrame myFrame = new JFrame();
            myFrame.setVisible(true);
            JOptionPane.showMessageDialog(myFrame, output);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void findBooksByAuthor() {
        try {
            String author = this.getInfo("Please enter the author:");

            try {

                List<Book> books = bookService.findBookByAuthor(author);

                String output;
                StringBuilder sb = new StringBuilder();
                sb.append("<html><table cellspacing=10>");
                sb.append("<tr>");
                sb.append("<td>").append("BOOK ID").append("</td>");
                sb.append("<td>").append("AUTHOR").append("</td>");
                sb.append("<td>").append("TITLE").append("</td>");
                sb.append("<td>").append("COPIES").append("</td>");
                sb.append("<td>").append("COPIES AVAILABLE").append("</td>");
                sb.append("</tr>");

                sb.append("<tr>");
                sb.append("<td>").append("___").append("</td>");
                sb.append("<td>").append("___").append("</td>");
                sb.append("<td>").append("___").append("</td>");
                sb.append("<td>").append("___").append("</td>");
                sb.append("<td>").append("___").append("</td>");
                sb.append("</tr>");
                for (Book book : books) {
                    sb.append("<tr>");

                    sb.append("<td>").append(book.getBook_id()).append("</td>");
                    sb.append("<td>").append(book.getAuthorName()).append("</td>");
                    sb.append("<td>").append(book.getTitle()).append("</td>");
                    sb.append("<td>").append(book.getCopies()).append("</td>");
                    sb.append("<td>").append(book.getCopiesAvailable()).append("</td>");

                }
                sb.append("</tr></table></html>");
                output = sb.toString();
                JFrame myFrame = new JFrame();
                myFrame.setVisible(true);
                JOptionPane.showMessageDialog(myFrame, output);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (Exception exception) {
            displayMessage("Error: " + exception.getMessage());
        } finally {
            if (this.getInfo("Do you want to search for another book? (yes / no):").equals("yes"))
                this.findBooksByAuthor();
        }
    }

    public void findBooksByTitle() {
        try {
            String title = this.getInfo("Please enter the book title:");

            try {

                List<Book> books = bookService.findBookByTitle(title);

                String output;
                StringBuilder sb = new StringBuilder();
                sb.append("<html><table cellspacing=10>");
                sb.append("<tr>");
                sb.append("<td>").append("BOOK ID").append("</td>");
                sb.append("<td>").append("AUTHOR").append("</td>");
                sb.append("<td>").append("TITLE").append("</td>");
                sb.append("<td>").append("COPIES").append("</td>");
                sb.append("<td>").append("COPIES AVAILABLE").append("</td>");
                sb.append("</tr>");


                sb.append("<tr>");
                sb.append("<td>").append("___").append("</td>");
                sb.append("<td>").append("___").append("</td>");
                sb.append("<td>").append("___").append("</td>");
                sb.append("<td>").append("___").append("</td>");
                sb.append("<td>").append("___").append("</td>");
                sb.append("</tr>");
                for (Book book : books) {
                    sb.append("<tr>");

                    sb.append("<td>").append(book.getBook_id()).append("</td>");
                    sb.append("<td>").append(book.getAuthorName()).append("</td>");
                    sb.append("<td>").append(book.getTitle()).append("</td>");
                    sb.append("<td>").append(book.getCopies()).append("</td>");
                    sb.append("<td>").append(book.getCopiesAvailable()).append("</td>");

                }
                sb.append("</tr></table></html>");
                output = sb.toString();
                JFrame myFrame = new JFrame();
                myFrame.setVisible(true);
                JOptionPane.showMessageDialog(myFrame, output);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (Exception exception) {
            displayMessage("Error: " + exception.getMessage());
        } finally {
            if (this.getInfo("Do you want to search for another book? (yes / no):").equals("yes"))
                this.findBooksByTitle();
        }
    }


    public void findAllUsersBorrowedBooks() {

        try {

            Map<User, Set<Book>> booksBorrowed = bookService.getBorrowingStats();

            String output;
            StringBuilder sb = new StringBuilder();
            sb.append("<html><table cellspacing=10>");
            sb.append("<tr>");
            sb.append("<td>").append("USER:").append("</td>");
            sb.append("<td>").append("HOW MANY BOOKS:").append("</td>");
            sb.append("<td>").append("BOOKS BORROWED:").append("</td>");

            sb.append("</tr>");

            sb.append("<tr>");
            sb.append("<td>").append("___").append("</td>");
            sb.append("<td>").append("___").append("</td>");
            sb.append("<td>").append("___").append("</td>");
            sb.append("</tr>");

            for (var entry : booksBorrowed.entrySet()) {
                sb.append("<tr>");
                sb.append("<td>").append(entry.getKey().getUserName()).append("</td>");
                sb.append("<td>").append(entry.getValue().size()).append("</td>");
                for (Book b : entry.getValue()) {
                    sb.append("<td>").append(b).append("</td>");
                }
            }

            sb.append("</tr></table></html>");
            output = sb.toString();
            JFrame myFrame = new JFrame();
            myFrame.setVisible(true);
            JOptionPane.showMessageDialog(myFrame, output);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void logout() {
        Library3Application.setUser(null);
    }

    public void seeMyBorrowedBooks() {
        try {
            Set<Book> books = bookService.findBooksByUser(Library3Application.getCurrent().getId());
            if (!books.isEmpty()) {
                String output;
                StringBuilder sb = new StringBuilder();
                sb.append("<html><table cellspacing=10>");
                sb.append("<tr>");
                sb.append("<td>").append("BOOK ID:").append("</td>");
                sb.append("<td>").append("AUTHOR:").append("</td>");
                sb.append("<td>").append("TITLE:").append("</td>");

                sb.append("</tr>");

                sb.append("<tr>");
                sb.append("<td>").append("___").append("</td>");
                sb.append("<td>").append("___").append("</td>");
                sb.append("<td>").append("___").append("</td>");
                sb.append("</tr>");

                for (Book entry : books) {
                    sb.append("<tr>");
                    sb.append("<td>").append(entry.getBook_id()).append("</td>");
                    sb.append("<td>").append(entry.getAuthorName()).append("</td>");
                    sb.append("<td>").append(entry.getTitle()).append("</td>");

                }

                sb.append("</tr></table></html>");
                output = sb.toString();
                JFrame myFrame = new JFrame();
                myFrame.setVisible(true);
                JOptionPane.showMessageDialog(myFrame, output);
            } else {
                System.out.println("You don`t have books borrowed.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
