package jafafx.kasir.transportasi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.SQLException;

public class RuteView {
    private Stage primaryStage;
    private String username;

    public RuteView(Stage stage, String username) {
        this.primaryStage = stage;
        this.username = username;
    }

    public void show() {
        TableView<Rute> table = new TableView<>();
        ObservableList<Rute> data = FXCollections.observableArrayList();

        // Kolom tabel
        TableColumn<Rute, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(50);

        TableColumn<Rute, String> colAsal = new TableColumn<>("Asal");
        colAsal.setCellValueFactory(new PropertyValueFactory<>("asal"));
        colAsal.setPrefWidth(200);

        TableColumn<Rute, String> colTujuan = new TableColumn<>("Tujuan");
        colTujuan.setCellValueFactory(new PropertyValueFactory<>("tujuan"));
        colTujuan.setPrefWidth(200);

        TableColumn<Rute, Double> colJarak = new TableColumn<>("Jarak (km)");
        colJarak.setCellValueFactory(new PropertyValueFactory<>("jarak"));
        colJarak.setPrefWidth(100);

        table.getColumns().addAll(colId, colAsal, colTujuan, colJarak);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Input field
        TextField tfAsal = new TextField();
        tfAsal.setPromptText("Asal");
        tfAsal.setPrefWidth(150);

        TextField tfTujuan = new TextField();
        tfTujuan.setPromptText("Tujuan");
        tfTujuan.setPrefWidth(150);

        TextField tfJarak = new TextField();
        tfJarak.setPromptText("Jarak (km)");
        tfJarak.setPrefWidth(100);

        Button btnAdd = new Button("Tambah");
        btnAdd.setPrefWidth(100);

        // Layout input
        HBox inputBox = new HBox(10, tfAsal, tfTujuan, tfJarak, btnAdd);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(20));

        // Tombol kembali
        Button backBtn = new Button("Kembali");
        backBtn.setPrefWidth(100);
        HBox bottomLeft = new HBox(backBtn);
        bottomLeft.setAlignment(Pos.CENTER_LEFT);
        bottomLeft.setPadding(new Insets(10, 0, 0, 0));

        // Label copyright
        Label copyright = new Label("© 2025 Mela Septiani - Sistem Kasir Transportasi");
        copyright.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        HBox copyrightBox = new HBox(copyright);
        copyrightBox.setAlignment(Pos.CENTER);
        copyrightBox.setPadding(new Insets(10, 0, 0, 0));

        // Judul (dengan ukuran disesuaikan)
        Label title = new Label("Manajemen Data Rute");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");
        HBox titleBox = new HBox(title);
        titleBox.setAlignment(Pos.CENTER);


        // Ambil data awal
        try {
            RuteOperations rop = new RuteOperations();
            data.addAll(rop.getAll());
            table.setItems(data);

            btnAdd.setOnAction(e -> {
                try {
                    double jarak = Double.parseDouble(tfJarak.getText());
                    Rute r = new Rute(0, tfAsal.getText(), tfTujuan.getText(), jarak);
                    rop.insert(r);
                    data.setAll(rop.getAll());
                    tfAsal.clear();
                    tfTujuan.clear();
                    tfJarak.clear();
                } catch (NumberFormatException ex) {
                    showAlert("Input Error", "Jarak harus berupa angka desimal.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

            backBtn.setOnAction(e -> new DashboardView(primaryStage, username).show());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        VBox layout = new VBox(20,
                titleBox,
                inputBox,
                table,
                bottomLeft,
                copyrightBox
        );

        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Manajemen Rute");
        primaryStage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
