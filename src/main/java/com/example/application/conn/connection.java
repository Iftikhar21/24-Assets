package com.example.application.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connection {
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection != null)
            return connection;
        else {
            String dbUrl = "jdbc:mysql://localhost:3306/"
                    + "24asets?user=root&password=";
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(dbUrl);
                System.out.println("Koneksi sukses");
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println("Koneksi gagal : "+e);
            }
            return connection;
        }
    }

    public static void main(String args[]) {
        getConnection();
    }
}
