package com.example.application.controller;

import com.example.application.model.Asset;
import com.example.application.model.Location;
import com.example.application.model.Products;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static com.example.application.conn.connection.getConnection;

public class AssetController {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    private List<Asset> listAsset;

    public AssetController() {
        this.conn = getConnection();
    }

    public List<Asset> getListAsset() {
        try {
            listAsset = new ArrayList<>();
            ps = conn.prepareStatement("SELECT * FROM peminjaman");
            rs = ps.executeQuery();
            while (rs.next()) {
                Asset asset = new Asset();

                Products product = new Products();
                product.setProductID(rs.getInt("id_product"));
                ProductsController productController = new ProductsController();
                product = productController.getProductByID(product.getProductID());
                asset.setProducts(new Products[] { product });

                Location location = new Location();
                location.setLocationID(rs.getInt("id_lokasi"));
                asset.setLocation(location);

                asset.setTakeTime(rs.getDate("waktu_peminjaman").toLocalDate().atStartOfDay());
                asset.setReturnTime(rs.getDate("waktu_pengembalian").toLocalDate().atStartOfDay());
                asset.setPin(rs.getString("pin_code"));
                asset.setNote(rs.getString("note"));
                asset.setRole(rs.getString("role_peminjam"));
                asset.setName(rs.getString("nama_peminjam"));
                asset.setStatus(rs.getString("status"));
                asset.setClassName(rs.getString("kelas"));
                asset.setQuantity(rs.getInt("qty"));
                listAsset.add(asset);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listAsset;
    }

    public boolean InsertAsset(Asset assets) {
        String query = "INSERT INTO peminjaman (id_product, id_lokasi, " +
            "waktu_peminjaman, waktu_pengembalian, pin_code, note, role_peminjam, nama_peminjam, kelas, qty) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            conn.setAutoCommit(false);

            for (int i = 0; i < assets.getProducts().length; i++) {
                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setInt(1, assets.getProducts()[i].getProductID());
                    ps.setInt(2, assets.getLocation().getLocationID());
                    ps.setDate(3, Date.valueOf(assets.getTakeTime().toLocalDate()));
                    ps.setDate(4, Date.valueOf(assets.getReturnTime().toLocalDate()));
                    ps.setString(5, assets.getPin());
                    ps.setString(6, assets.getNote());
                    ps.setString(7, assets.getRole());
                    ps.setString(8, assets.getName());
                    ps.setString(9, assets.getClassName());
                    ps.setInt(10, assets.getProducts()[i].getQuantity());

                    int rowsInserted = ps.executeUpdate();
                    if (rowsInserted == 0) {
                        throw new SQLException("Gagal insert untuk product ID: " + assets.getProducts()[i].getProductID());
                    }

                    System.out.println("Inserted: " + assets.getProducts()[i].getProductID());
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Terjadi error, rollback semua insert");
            e.printStackTrace();

            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback juga gagal:");
                rollbackEx.printStackTrace();
            }

            return false;

        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
