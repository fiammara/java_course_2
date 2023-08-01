package com.example.library3.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    String dbURL = "jdbc:mysql://localhost:3306/library";
    String username = "root";
    String password = "root";
    Connection conn;
  Connection getConnection(){

       {
           try {
               conn = DriverManager.getConnection(dbURL, username, password);
           } catch (SQLException e) {
               throw new RuntimeException(e);
           }
       }
      return conn;
  }
}
