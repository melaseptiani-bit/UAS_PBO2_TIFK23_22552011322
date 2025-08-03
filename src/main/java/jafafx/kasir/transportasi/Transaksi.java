package jafafx.kasir.transportasi;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class Transaksi {
    private IntegerProperty id = new SimpleIntegerProperty();
    private ObjectProperty<Kendaraan> kendaraan = new SimpleObjectProperty<>();
    private ObjectProperty<Rute> rute = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDateTime> waktuMasuk = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDateTime> waktuKeluar = new SimpleObjectProperty<>();
    private DoubleProperty total = new SimpleDoubleProperty();

    // Constructor tanpa ID (untuk insert baru)
    public Transaksi(Kendaraan kendaraan, Rute rute, LocalDateTime waktuMasuk, LocalDateTime waktuKeluar, double total) {
        this.kendaraan.set(kendaraan);
        this.rute.set(rute);
        this.waktuMasuk.set(waktuMasuk);
        this.waktuKeluar.set(waktuKeluar);
        this.total.set(total);
    }

    // Constructor lengkap (untuk ambil dari database)
    public Transaksi(int id, Kendaraan kendaraan, Rute rute, LocalDateTime waktuMasuk, LocalDateTime waktuKeluar, double total) {
        this.id.set(id);
        this.kendaraan.set(kendaraan);
        this.rute.set(rute);
        this.waktuMasuk.set(waktuMasuk);
        this.waktuKeluar.set(waktuKeluar);
        this.total.set(total);
    }

    // Getter & Setter
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public Kendaraan getKendaraan() { return kendaraan.get(); }
    public void setKendaraan(Kendaraan kendaraan) { this.kendaraan.set(kendaraan); }
    public ObjectProperty<Kendaraan> kendaraanProperty() { return kendaraan; }

    public Rute getRute() { return rute.get(); }
    public void setRute(Rute rute) { this.rute.set(rute); }
    public ObjectProperty<Rute> ruteProperty() { return rute; }

    public LocalDateTime getWaktuMasuk() { return waktuMasuk.get(); }
    public void setWaktuMasuk(LocalDateTime waktuMasuk) { this.waktuMasuk.set(waktuMasuk); }
    public ObjectProperty<LocalDateTime> waktuMasukProperty() { return waktuMasuk; }

    public LocalDateTime getWaktuKeluar() { return waktuKeluar.get(); }
    public void setWaktuKeluar(LocalDateTime waktuKeluar) { this.waktuKeluar.set(waktuKeluar); }
    public ObjectProperty<LocalDateTime> waktuKeluarProperty() { return waktuKeluar; }

    public double getTotal() { return total.get(); }
    public void setTotal(double total) { this.total.set(total); }
    public DoubleProperty totalProperty() { return total; }

    @Override
    public String toString() {
        return kendaraan.get() + " - " + rute.get(); // Untuk tampil di TableView jika diperlukan
    }
}
