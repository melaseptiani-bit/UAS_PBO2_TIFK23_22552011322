package jafafx.kasir.transportasi;

import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class RegisterView {
    private Stage primaryStage;
    private UserOperations userOperations;

    public RegisterView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public BorderPane getView() {
        // Root BorderPane agar responsif seperti web
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #0d47a1, #1e88e5);");

        // Center Card
        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.setMaxWidth(600); // Ukuran lebar seperti form web
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10, 0.5, 0, 2);"
        );

        // Judul
        Label titleLabel = new Label("🚀 Register Akun");
        titleLabel.setStyle(
            "-fx-font-size: 32px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #0d47a1;"
        );
        titleLabel.setTextAlignment(TextAlignment.CENTER);

        // Input Fields
        TextField usernameField = createStyledTextField("Username");
        PasswordField passwordField = createStyledPasswordField("Password");

        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.setItems(FXCollections.observableArrayList("User", "Admin"));
        roleComboBox.setValue("User");
        roleComboBox.setMaxWidth(Double.MAX_VALUE);
        roleComboBox.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-padding: 10px;" +
            "-fx-background-radius: 6;" +
            "-fx-border-color: #1e88e5;" +
            "-fx-border-radius: 6;"
        );

        // Register Button
        Button registerButton = new Button("Register");
        registerButton.setMaxWidth(Double.MAX_VALUE);
        registerButton.setStyle(
            "-fx-background-color: #1e88e5;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 12px;" +
            "-fx-background-radius: 6;"
        );

        // Hover effect
        registerButton.setOnMouseEntered(e -> registerButton.setStyle(
            "-fx-background-color: #1565c0;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 12px;" +
            "-fx-background-radius: 6;"
        ));
        registerButton.setOnMouseExited(e -> registerButton.setStyle(
            "-fx-background-color: #1e88e5;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 12px;" +
            "-fx-background-radius: 6;"
        ));

        // Link Login
        Label loginLink = new Label("Sudah punya akun? Login di sini.");
        loginLink.setStyle(
            "-fx-text-fill: #0d47a1;" +
            "-fx-underline: true;" +
            "-fx-font-size: 14px;"
        );
        loginLink.setOnMouseClicked(e -> {
            LoginView loginView = new LoginView(primaryStage);
            Scene loginScene = new Scene(loginView.getView(), 1024, 768);
            primaryStage.setScene(loginScene);
        });

        // Action Register
        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String role = roleComboBox.getValue();

            try {
                userOperations = new UserOperations();
                if (userOperations.registerUser(username, password, role)) {
                    showSuccess("Register berhasil! Silakan login.");
                    LoginView loginView = new LoginView(primaryStage);
                    primaryStage.setScene(new Scene(loginView.getView(), 1024, 768));
                } else {
                    showError("Register gagal! Username mungkin sudah digunakan.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                showError("Koneksi database gagal!");
            } catch (Exception ex) {
                ex.printStackTrace();
                showError("Terjadi kesalahan saat register.");
            }
        });

        card.getChildren().addAll(titleLabel, usernameField, passwordField, roleComboBox, registerButton, loginLink);
        root.setCenter(card);

        // Optional: Copyright
        Label copyright = new Label("© 2025 Kasir Transportasi");
        copyright.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");
        BorderPane.setAlignment(copyright, Pos.CENTER);
        root.setBottom(copyright);

        return root;
    }

    private TextField createStyledTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setMaxWidth(Double.MAX_VALUE);
        tf.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-padding: 10px;" +
            "-fx-background-radius: 6;" +
            "-fx-border-color: #1e88e5;" +
            "-fx-border-radius: 6;"
        );
        return tf;
    }

    private PasswordField createStyledPasswordField(String prompt) {
        PasswordField pf = new PasswordField();
        pf.setPromptText(prompt);
        pf.setMaxWidth(Double.MAX_VALUE);
        pf.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-padding: 10px;" +
            "-fx-background-radius: 6;" +
            "-fx-border-color: #1e88e5;" +
            "-fx-border-radius: 6;"
        );
        return pf;
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sukses");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Terjadi Kesalahan");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
