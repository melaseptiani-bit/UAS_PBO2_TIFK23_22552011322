package jafafx.kasir.transportasi;

import java.sql.*;
import java.util.*;

public class KendaraanOperations {
    private Connection conn;

    public KendaraanOperations() throws SQLException {
    conn = DatabaseConnection.getConnection();
    }
    public List<Kendaraan> getAll() throws SQLException {
        List<Kendaraan> list = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM kendaraan");
        while (rs.next()) {
            list.add(new Kendaraan(rs.getInt("id"), rs.getString("jenis"), rs.getInt("kapasitas")));
        }
        return list;
    }

    public void insert(Kendaraan k) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO kendaraan (jenis, kapasitas) VALUES (?, ?)");
        stmt.setString(1, k.getJenis());
        stmt.setInt(2, k.getKapasitas());
        stmt.executeUpdate();
    }

    public void delete(int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM kendaraan WHERE id = ?");
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
}