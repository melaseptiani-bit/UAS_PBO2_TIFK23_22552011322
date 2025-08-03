package jafafx.kasir.transportasi;

import javafx.beans.property.*;

public class Kendaraan {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty jenis = new SimpleStringProperty();
    private final IntegerProperty kapasitas = new SimpleIntegerProperty();

    public Kendaraan() {}

    public Kendaraan(String jenis, int kapasitas) {
        this.jenis.set(jenis);
        this.kapasitas.set(kapasitas);
    }

    public Kendaraan(int id, String jenis, int kapasitas) {
        this.id.set(id);
        this.jenis.set(jenis);
        this.kapasitas.set(kapasitas);
    }

    // Getter dan Setter
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getJenis() { return jenis.get(); }
    public void setJenis(String jenis) { this.jenis.set(jenis); }
    public StringProperty jenisProperty() { return jenis; }

    public int getKapasitas() { return kapasitas.get(); }
    public void setKapasitas(int kapasitas) { this.kapasitas.set(kapasitas); }
    public IntegerProperty kapasitasProperty() { return kapasitas; }

    @Override
    public String toString() {
        return getJenis() + " (" + getKapasitas() + " penumpang)";
}

}
