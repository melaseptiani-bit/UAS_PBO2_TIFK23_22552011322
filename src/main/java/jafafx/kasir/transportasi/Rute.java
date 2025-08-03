package jafafx.kasir.transportasi;

import javafx.beans.property.*;

public class Rute {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty asal = new SimpleStringProperty();
    private final StringProperty tujuan = new SimpleStringProperty();
    private final DoubleProperty jarak = new SimpleDoubleProperty();

    public Rute() {}

    public Rute(String asal, String tujuan, double jarak) {
        this.asal.set(asal);
        this.tujuan.set(tujuan);
        this.jarak.set(jarak);
    }

    public Rute(int id, String asal, String tujuan, double jarak) {
        this.id.set(id);
        this.asal.set(asal);
        this.tujuan.set(tujuan);
        this.jarak.set(jarak);
    }

    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getAsal() { return asal.get(); }
    public void setAsal(String asal) { this.asal.set(asal); }
    public StringProperty asalProperty() { return asal; }

    public String getTujuan() { return tujuan.get(); }
    public void setTujuan(String tujuan) { this.tujuan.set(tujuan); }
    public StringProperty tujuanProperty() { return tujuan; }

    public double getJarak() { return jarak.get(); }
    public void setJarak(double jarak) { this.jarak.set(jarak); }
    public DoubleProperty jarakProperty() { return jarak; }

    public StringProperty asalTujuanProperty() {
        return new SimpleStringProperty(getAsal() + " - " + getTujuan());
    }

    @Override
    public String toString() {
        return getAsal() + " → " + getTujuan() + " (" + getJarak() + " km)";
}
}
