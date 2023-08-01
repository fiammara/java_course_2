package com.example.library3.service;


import com.example.library3.Library3Application;
import com.example.library3.model.User;
import com.example.library3.repository.UserRepository;
import java.sql.SQLException;


public class UserService {

    private final UserRepository userRepository;
    public UserService() {
        this.userRepository = new UserRepository();
    }
    public String registerUser(User user) throws SQLException {
        String message;

        if (userRepository.save(user)) {
            message = "User registered successfully!";
        }
        else {
            message = "Unexpected error registering user.";}
        return message;
    }

    public String loginUser(User user)  {
        String message;
        try {
            User userObj = userRepository.findUserByNameAndPassword(user);
            if(userObj != null) {
            Library3Application.setCurrent(userRepository.findUserByNameAndPassword(user));
                message = user.getUserName() +  " logged in successfully!";
             }
            else {
                message = "Incorrect username or password. Please try again";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

    public int checkUserBooks(Long id) throws SQLException {
        return userRepository.countBooksByUserId(id);
    }
}
