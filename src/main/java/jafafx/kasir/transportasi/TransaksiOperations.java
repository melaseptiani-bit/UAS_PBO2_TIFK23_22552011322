package jafafx.kasir.transportasi;

import java.io.*;
import java.sql.*;
import java.util.*;

public class TransaksiOperations {
    private Connection conn;

    public TransaksiOperations() throws SQLException {
        conn = DatabaseConnection.getConnection();
    }

    public List<Transaksi> getAll() throws SQLException {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT t.*, k.jenis, k.kapasitas, r.asal, r.tujuan, r.jarak " +
                     "FROM transaksi t " +
                     "JOIN kendaraan k ON t.kendaraan_id = k.id " +
                     "JOIN rute r ON t.rute_id = r.id";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Kendaraan kendaraan = new Kendaraan(
                    rs.getInt("kendaraan_id"),
                    rs.getString("jenis"),
                    rs.getInt("kapasitas")
                );
                Rute rute = new Rute(
                    rs.getInt("rute_id"),
                    rs.getString("asal"),
                    rs.getString("tujuan"),
                    rs.getDouble("jarak")
                );
                Transaksi t = new Transaksi(
                    rs.getInt("id"),
                    kendaraan,
                    rute,
                    rs.getTimestamp("waktu_masuk").toLocalDateTime(),
                    rs.getTimestamp("waktu_keluar").toLocalDateTime(),
                    rs.getDouble("total")
                );
                list.add(t);
            }
        }
        return list;
    }

    public void insert(Transaksi t) throws SQLException {
        String sql = "INSERT INTO transaksi (kendaraan_id, rute_id, waktu_masuk, waktu_keluar, total) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, t.getKendaraan().getId());
        stmt.setInt(2, t.getRute().getId());
        stmt.setTimestamp(3, Timestamp.valueOf(t.getWaktuMasuk()));
        stmt.setTimestamp(4, Timestamp.valueOf(t.getWaktuKeluar()));
        stmt.setDouble(5, t.getTotal());
        stmt.executeUpdate();
    }

    public void exportToCSV(List<Transaksi> transaksiList, File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            // Header kolom
            writer.println("Jenis Kendaraan,Kapasitas,Asal,Tujuan,Jarak (km),Waktu Masuk,Waktu Keluar,Total");

            for (Transaksi t : transaksiList) {
                Kendaraan kendaraan = t.getKendaraan();
                Rute rute = t.getRute();
                String waktuMasuk = t.getWaktuMasuk().toString();
                String waktuKeluar = t.getWaktuKeluar().toString();
                double total = t.getTotal();

                // Format baris CSV
                writer.printf("%s,%d,%s,%s,%.2f,%s,%s,%.2f%n",
                    kendaraan.getJenis(),
                    kendaraan.getKapasitas(),
                    rute.getAsal(),
                    rute.getTujuan(),
                    rute.getJarak(),
                    waktuMasuk,
                    waktuKeluar,
                    total
                );
            }
        }
    }
}
