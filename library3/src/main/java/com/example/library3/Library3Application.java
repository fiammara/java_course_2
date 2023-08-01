package com.example.library3;

import com.example.library3.controller.MenuController;
import com.example.library3.model.User;

import java.util.Scanner;


public class Library3Application {
    private static User current = null;

    public static void main(String[] args) {

        if (current == null) {
            displayMenu1();
        } else {
            displayMenu2();
        }
    }

    public static User getCurrent() {
        return current;
    }

    public static void setCurrent(User current) {
        Library3Application.current = current;
    }

    public static void displayMenu1() {
        System.out.println("""
                ---- The library ----
                 1. Register
                 2. Log in
                          
                     """
        );
        Scanner scanner = new Scanner(System.in);
        String userChoice = scanner.nextLine();

        handleUserChoice(userChoice);

    }

    public static void displayMenu2() {
        System.out.println("""
                ---- The library ----
                 3. Add book
                 4. Remove book   
                 5. Borrow a book
                 6. Return a book
                 7. See all books in the library
                 8. Find books by author
                 9. Find books by title
                10. Find book by author and title
                11. Show my borrowed books        
                12. See who borrowed books    
                13. Logout
                 
                     """
        );

        Scanner scanner = new Scanner(System.in);
        String userChoice = scanner.nextLine();

        handleUserChoice(userChoice);

    }

    public static void setUser(User user) {

        current = user;
    }

    private static void handleUserChoice(String userChoice) {
        MenuController menuController = new MenuController();

        switch (userChoice) {
            case "1" -> menuController.registerNewUser();
            case "2" -> menuController.loginUser();
            case "3" -> menuController.addBookToTheLibrary();
            case "4" -> menuController.removeBookFromTheLibrary();
            case "5" -> menuController.borrowABook();
            case "6" -> menuController.returnABook();
            case "7" -> menuController.seeAllBooksInThisLibrary();
            case "8" -> menuController.findBooksByAuthor();
            case "9" -> menuController.findBooksByTitle();
            case "10" -> menuController.findABookByAuthorAndTitle();
            case "11" -> menuController.seeMyBorrowedBooks();
            case "12" -> menuController.findAllUsersBorrowedBooks();
            case "13" -> menuController.logout();
            default -> {
            }
        }
        if (current == null) {
            displayMenu1();
        } else {
            displayMenu2();
        }
    }
}
