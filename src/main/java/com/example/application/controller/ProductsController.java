package com.example.application.controller;

import com.example.application.model.Products;
import com.example.application.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.application.conn.connection.getConnection;

public class ProductsController {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public ProductsController() {
        conn = getConnection();
    }

    public List<Products> getListProducts(String productName) {
        List<Products> listProducts = new ArrayList<>();
        try {
            ps = conn.prepareStatement(
                    "SELECT p.id_product, p.product_name, p.stock, p.id_category, c.category_name " +
                            "FROM products p " +
                            "JOIN category c ON p.id_category = c.id_category " +
                            "WHERE p.product_name LIKE ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE
            );
            ps.setString(1, productName + "%");
            rs = ps.executeQuery();
            rs.beforeFirst();

            while (rs.next()) {
                Products products = new Products();
                products.setProductID(rs.getInt("id_product"));
                products.setCategoryID(rs.getInt("id_category"));
                products.setProductName(rs.getString("product_name"));
                products.setStock(rs.getInt("stock"));
                products.setCategoryName(rs.getString("category_name")); // dari tabel category

                listProducts.add(products);
            }
        } catch (SQLException e) {
            System.out.println("Error : " + e);
        }
        return listProducts;
    }

    public Products getProductByID(int productID) {
        Products product = null;
        try {
            ps = conn.prepareStatement(
                    "SELECT p.id_product, p.product_name, p.stock, p.id_category, c.category_name " +
                            "FROM products p " +
                            "JOIN category c ON p.id_category = c.id_category " +
                            "WHERE p.id_product = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE
            );
            ps.setInt(1, productID);
            rs = ps.executeQuery();
            rs.beforeFirst();

            if (rs.next()) {
                product = new Products();
                product.setProductID(rs.getInt("id_product"));
                product.setCategoryID(rs.getInt("id_category"));
                product.setProductName(rs.getString("product_name"));
                product.setStock(rs.getInt("stock"));
                product.setCategoryName(rs.getString("category_name"));
            }
        } catch (SQLException e) {
            System.out.println("Error : " + e);
        }
        return product;
    }

    public static void main(String[] args) {
        ProductsController controller = new ProductsController();

        // Ambil semua produk (tanpa filter nama)
        List<Products> productsList = controller.getListProducts("");

        // Cek jika tidak ada data
        if (productsList.isEmpty()) {
            System.out.println("Tidak ada produk ditemukan.");
        } else {
            System.out.println("Daftar Produk:");
            for (Products p : productsList) {
                System.out.println("ID: " + p.getProductID());
                System.out.println("Nama Produk: " + p.getProductName());
                System.out.println("ID Kategori: " + p.getCategoryID());
                System.out.println("Kategori: " + p.getCategoryName());
                System.out.println("Stok: " + p.getStock());
                System.out.println("-------------------------");
            }
        }
    }

}
