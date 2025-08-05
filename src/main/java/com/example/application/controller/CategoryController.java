package com.example.application.controller;

import com.example.application.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.application.conn.connection.getConnection;

public class CategoryController {

    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public CategoryController() {
        this.conn = getConnection();
    }

    public List<Category> getListCategory() {
        List<Category> listCategory = new ArrayList<>();
        try {
            String sql = "SELECT * FROM category";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Category category = new Category();
                category.setCategoryID(rs.getInt("id_category"));
                category.setCategoryName(rs.getString("category_name"));

                listCategory.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listCategory;
    }
}
