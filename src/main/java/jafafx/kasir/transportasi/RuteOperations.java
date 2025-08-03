package jafafx.kasir.transportasi;

import java.sql.*;
import java.util.*;

public class RuteOperations {
    private Connection conn;

    public RuteOperations() throws SQLException {
        conn = DatabaseConnection.getConnection();
    }

    public List<Rute> getAll() throws SQLException {
        List<Rute> list = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM rute");
        while (rs.next()) {
            list.add(new Rute(
                rs.getInt("id"),
                rs.getString("asal"),
                rs.getString("tujuan"),
                rs.getDouble("jarak")
            ));
        }
        return list;
    }

    public void insert(Rute r) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO rute (asal, tujuan, jarak) VALUES (?, ?, ?)");
        stmt.setString(1, r.getAsal());
        stmt.setString(2, r.getTujuan());
        stmt.setDouble(3, r.getJarak());
        stmt.executeUpdate();
    }

    public void delete(int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM rute WHERE id = ?");
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
}