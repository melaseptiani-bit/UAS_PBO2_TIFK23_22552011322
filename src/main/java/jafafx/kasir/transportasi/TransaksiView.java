package jafafx.kasir.transportasi;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.text.Font;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransaksiView {
    private Stage primaryStage;
    private String username;

    private ComboBox<Kendaraan> cbKendaraan = new ComboBox<>();
    private ComboBox<Rute> cbRute = new ComboBox<>();
    private DatePicker dpMasuk = new DatePicker();
    private Spinner<Integer> jamMasuk = new Spinner<>(0, 23, 0);
    private Spinner<Integer> menitMasuk = new Spinner<>(0, 59, 0);

    private DatePicker dpKeluar = new DatePicker();
    private Spinner<Integer> jamKeluar = new Spinner<>(0, 23, 0);
    private Spinner<Integer> menitKeluar = new Spinner<>(0, 59, 0);

    private TextField tfTotal = new TextField();

    public TransaksiView(Stage stage, String username) {
        this.primaryStage = stage;
        this.username = username;
    }

    public void show() {
        TableView<Transaksi> table = new TableView<>();
        ObservableList<Transaksi> data = FXCollections.observableArrayList();

        Label title = new Label("Manajemen Data Transaksi");
        title.setFont(new Font("Arial", 26));
        title.setStyle("-fx-font-weight: bold;");
        HBox titleBox = new HBox(title);
        titleBox.setAlignment(Pos.CENTER);

        TableColumn<Transaksi, String> colKendaraan = new TableColumn<>("Kendaraan");
        colKendaraan.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getKendaraan().toString()));

        TableColumn<Transaksi, String> colRute = new TableColumn<>("Rute");
        colRute.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getRute().toString()));

        TableColumn<Transaksi, LocalDateTime> colMasuk = new TableColumn<>("Waktu Masuk");
        colMasuk.setCellValueFactory(new PropertyValueFactory<>("waktuMasuk"));

        TableColumn<Transaksi, LocalDateTime> colKeluar = new TableColumn<>("Waktu Keluar");
        colKeluar.setCellValueFactory(new PropertyValueFactory<>("waktuKeluar"));

        TableColumn<Transaksi, Double> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        table.getColumns().addAll(colKendaraan, colRute, colMasuk, colKeluar, colTotal);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tfTotal.setPromptText("Total Biaya");
        tfTotal.setEditable(false);
        tfTotal.setMaxWidth(150);

        Button btnAdd = new Button("Tambah");
        Button btnExport = new Button("Export ke CSV");
        Button backBtn = new Button("Kembali");

        try {
            TransaksiOperations ops = new TransaksiOperations();
            KendaraanOperations kop = new KendaraanOperations();
            RuteOperations rop = new RuteOperations();

            cbKendaraan.setItems(FXCollections.observableArrayList(kop.getAll()));
            cbRute.setItems(FXCollections.observableArrayList(rop.getAll()));
            data.setAll(ops.getAll());
            table.setItems(data);

            cbKendaraan.setPrefWidth(200);
            cbRute.setPrefWidth(200);

            cbKendaraan.setOnAction(e -> hitungTotal());
            cbRute.setOnAction(e -> hitungTotal());

            btnAdd.setOnAction(e -> {
                try {
                    LocalDateTime waktuMasuk = combineDateTime(dpMasuk.getValue(), jamMasuk.getValue(), menitMasuk.getValue());
                    LocalDateTime waktuKeluar = combineDateTime(dpKeluar.getValue(), jamKeluar.getValue(), menitKeluar.getValue());

                    Transaksi t = new Transaksi(
                        cbKendaraan.getValue(),
                        cbRute.getValue(),
                        waktuMasuk,
                        waktuKeluar,
                        Double.parseDouble(tfTotal.getText())
                    );
                    ops.insert(t);
                    data.setAll(ops.getAll());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            table.setRowFactory(tv -> {
                TableRow<Transaksi> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (!row.isEmpty()) {
                        Transaksi t = row.getItem();
                        cbKendaraan.setValue(t.getKendaraan());
                        cbRute.setValue(t.getRute());

                        dpMasuk.setValue(t.getWaktuMasuk().toLocalDate());
                        jamMasuk.getValueFactory().setValue(t.getWaktuMasuk().getHour());
                        menitMasuk.getValueFactory().setValue(t.getWaktuMasuk().getMinute());

                        dpKeluar.setValue(t.getWaktuKeluar().toLocalDate());
                        jamKeluar.getValueFactory().setValue(t.getWaktuKeluar().getHour());
                        menitKeluar.getValueFactory().setValue(t.getWaktuKeluar().getMinute());

                        tfTotal.setText(String.valueOf(t.getTotal()));
                    }
                });
                return row;
            });

            btnExport.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save CSV File");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
                File file = fileChooser.showSaveDialog(primaryStage);

                if (file != null) {
                    try {
                        ops.exportToCSV(data, file);
                        new Alert(Alert.AlertType.INFORMATION, "Berhasil ekspor ke: " + file.getAbsolutePath()).showAndWait();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        new Alert(Alert.AlertType.ERROR, "Gagal ekspor file.").showAndWait();
                    } catch (Throwable ex) {
                        Logger.getLogger(TransaksiView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            backBtn.setOnAction(e -> new DashboardView(primaryStage, username).show());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Form input
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));

        HBox waktuMasukBox = new HBox(5, dpMasuk, jamMasuk, menitMasuk);
        HBox waktuKeluarBox = new HBox(5, dpKeluar, jamKeluar, menitKeluar);

        formGrid.add(new Label("Kendaraan:"), 0, 0);
        formGrid.add(cbKendaraan, 1, 0);
        formGrid.add(new Label("Rute:"), 0, 1);
        formGrid.add(cbRute, 1, 1);
        formGrid.add(new Label("Masuk:"), 2, 0);
        formGrid.add(waktuMasukBox, 3, 0);
        formGrid.add(new Label("Keluar:"), 2, 1);
        formGrid.add(waktuKeluarBox, 3, 1);
        formGrid.add(new Label("Total:"), 4, 0);
        formGrid.add(tfTotal, 5, 0);
        formGrid.add(btnAdd, 6, 0);
        formGrid.add(btnExport, 6, 1);
        formGrid.setAlignment(Pos.CENTER_LEFT);

        Label copyright = new Label("© 2025 Mela Septiani - Sistem Kasir Transportasi");
        copyright.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");
        HBox copyrightBox = new HBox(copyright);
        copyrightBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(20, titleBox, formGrid, table, backBtn, copyrightBox);
        root.setPadding(new Insets(20));
        root.setPrefWidth(1200);
        root.setPrefHeight(750);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Manajemen Transaksi");
        primaryStage.show();
    }

    private LocalDateTime combineDateTime(LocalDate date, int hour, int minute) {
        if (date == null) return null;
        return LocalDateTime.of(date, LocalTime.of(hour, minute));
    }

    private void hitungTotal() {
        Kendaraan kendaraan = cbKendaraan.getValue();
        Rute rute = cbRute.getValue();

        if (kendaraan != null && rute != null) {
            double tarifPerKm;
            switch (kendaraan.getJenis().toLowerCase()) {
                case "bus": tarifPerKm = 3000; break;
                case "mobil": tarifPerKm = 2000; break;
                default: tarifPerKm = 2500;
            }
            double total = rute.getJarak() * tarifPerKm;
            tfTotal.setText(String.format("%.2f", total));
        }
    }
}
