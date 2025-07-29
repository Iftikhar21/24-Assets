package com.example.application.controller;

import com.example.application.model.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.application.conn.connection.getConnection;

public class LocationController {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    private List<Location> listLocation;

    public LocationController() {
        conn = getConnection();
    }

    public List<Location> getListLocations() {
        listLocation = new ArrayList<>();
        try {
            ps = conn.prepareStatement("SELECT * FROM location");
            rs = ps.executeQuery();

            while (rs.next()) {
                Location location = new Location();
                location.setLocationID(rs.getInt("id_location"));
                location.setLocationName(rs.getString("name_location"));
                listLocation.add(location);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listLocation;
    }
}
