package com.example.library3.repository;


import com.example.library3.Library3Application;
import com.example.library3.model.User;

import java.sql.*;

public class UserRepository {
    DatabaseConnection databaseConnection;
    Connection conn;

    public UserRepository() {
        databaseConnection = new DatabaseConnection();
        conn = databaseConnection.getConnection();
    }

    public boolean save(User user) throws SQLException {
        boolean result = false;
        String query = "INSERT INTO users (userName, password, email) VALUES (?,?, ?)";

        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, user.getUserName());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, user.getEmail());

        if (preparedStatement.executeUpdate() > 0) {
            result = true;
        }

        return result;
    }

    public User findUserByNameAndPassword(User user) throws SQLException {
        User userFound = null;
        String query = "SELECT * FROM users WHERE userName = ? AND password = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);

        preparedStatement.setString(1, user.getUserName());
        preparedStatement.setString(2, user.getPassword());

        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {

            User user3 = new User();
            user3.setId(rs.getObject(1, long.class));
            user3.setUserName((String) rs.getObject(2));
            user3.setPassword((String) rs.getObject(3));

            userFound = user3;
            Library3Application.setCurrentUser(user3);
        }
        return userFound;
    }

    public int countBooksByUserId(Long id) throws SQLException {

        String query = "SELECT COUNT(*) FROM usersbooks WHERE user_id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setLong(1, id);

        ResultSet rs = preparedStatement.executeQuery();
        if (!rs.next()) {
            System.out.println("0 books");
        }
        return rs.getInt(1);
    }
}
