package jafafx.kasir.transportasi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.SQLException;

public class KendaraanView {
    private Stage primaryStage;
    private String username;

    public KendaraanView(Stage stage, String username) {
        this.primaryStage = stage;
        this.username = username;
    }

    public void show() {
        // Judul
        Label title = new Label("Manajemen Data Kendaraan");
        title.setFont(new Font("Arial", 26));
        title.setStyle("-fx-font-weight: bold;");
        title.setAlignment(Pos.CENTER);

        // Tabel
        TableView<Kendaraan> table = new TableView<>();
        ObservableList<Kendaraan> data = FXCollections.observableArrayList();

        TableColumn<Kendaraan, Integer> colId = new TableColumn<>("ID No");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setMinWidth(70);

        TableColumn<Kendaraan, String> colJenis = new TableColumn<>("Jenis");
        colJenis.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        colJenis.setMinWidth(300);

        TableColumn<Kendaraan, Integer> colKapasitas = new TableColumn<>("Kapasitas");
        colKapasitas.setCellValueFactory(new PropertyValueFactory<>("kapasitas"));
        colKapasitas.setMinWidth(150);

        table.getColumns().addAll(colId, colJenis, colKapasitas);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Form input
        TextField tfJenis = new TextField();
        tfJenis.setPromptText("Jenis Kendaraan");
        tfJenis.setPrefWidth(200);

        TextField tfKapasitas = new TextField();
        tfKapasitas.setPromptText("Kapasitas");
        tfKapasitas.setPrefWidth(100);

        Button btnAdd = new Button("Tambah");
        btnAdd.setPrefWidth(100);

        HBox form = new HBox(10, tfJenis, tfKapasitas, btnAdd);
        form.setAlignment(Pos.CENTER);

        // Tombol kembali
        Button backBtn = new Button("Kembali");
        HBox backBox = new HBox(backBtn);
        backBox.setAlignment(Pos.CENTER_LEFT);
        backBox.setPadding(new Insets(10, 0, 0, 0));

        // Copyright di tengah bawah
        Label copyright = new Label("© 2025 Mela Septiani - Sistem Kasir Transportasi");
        copyright.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");
        HBox copyrightBox = new HBox(copyright);
        copyrightBox.setAlignment(Pos.CENTER);
        copyrightBox.setPadding(new Insets(10, 0, 10, 0));

        VBox layout = new VBox(20, title, form, table, backBox, copyrightBox);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPrefWidth(900);

        Scene scene = new Scene(layout, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Data Kendaraan");
        primaryStage.show();

        // Operasi database
        try {
            KendaraanOperations kop = new KendaraanOperations();
            data.addAll(kop.getAll());
            table.setItems(data);

            btnAdd.setOnAction(e -> {
                try {
                    String jenis = tfJenis.getText();
                    int kapasitas = Integer.parseInt(tfKapasitas.getText());

                    if (!jenis.isEmpty()) {
                        Kendaraan k = new Kendaraan(0, jenis, kapasitas);
                        kop.insert(k);
                        data.setAll(kop.getAll());
                        tfJenis.clear();
                        tfKapasitas.clear();
                    }
                } catch (Exception ex) {
                    showAlert("Input tidak valid. Pastikan kapasitas berupa angka.");
                }
            });

            backBtn.setOnAction(e -> new DashboardView(primaryStage, username).show());

        } catch (SQLException e) {
            showAlert("Gagal memuat data dari database.");
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Kesalahan");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
