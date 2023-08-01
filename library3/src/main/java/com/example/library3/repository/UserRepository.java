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

    public  boolean save (User user) throws SQLException {
        boolean result = false;
        String query = "INSERT INTO users (userName, password, email) VALUES (?,?, ?)";

        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, user.getUserName());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, user.getEmail());

        if (preparedStatement.executeUpdate()>0) {
            result = true;
        }

        return result;
    }
    public  User findUserByNameAndPassword (User user) throws SQLException {
        User userFound = null;
        String query = "select * from users where userName = ? and password = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);

        preparedStatement.setString(1, user.getUserName());
        preparedStatement.setString(2, user.getPassword());

        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {

       User us = new User();
       us.setId( rs.getObject(1, long.class));
       us.setUserName((String) rs.getObject(2));
       us.setPassword((String) rs.getObject(3));
       userFound = us;
            Library3Application.setUser(us);
        }
        return userFound;
    }

    public int countBooksByUserId(Long id) throws SQLException {

        String query = "SELECT COUNT(*) FROM usersbooks where user_id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setLong(1, id);


        ResultSet rs = preparedStatement.executeQuery();
        if (!rs.next()) {
            System.out.println("0 books");

        }
        return rs.getInt(1);
    }
}
