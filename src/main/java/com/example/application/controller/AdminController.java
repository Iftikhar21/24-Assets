package com.example.application.controller;

import com.example.application.model.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static com.example.application.conn.connection.getConnection;

public class AdminController {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public AdminController() {
        this.conn = getConnection();
    }

    public Admin login(String pin) {
        try {
            ps = conn.prepareStatement("SELECT * FROM admin WHERE pin = ?");
            ps.setString(1, pin);
            rs = ps.executeQuery();

            if (rs.next()) {
                Admin admin = new Admin();
                admin.setPin(rs.getString("pin"));
                admin.setName(rs.getString("name"));
                return admin;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
