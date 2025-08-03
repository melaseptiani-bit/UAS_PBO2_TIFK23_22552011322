package jafafx.kasir.transportasi;

import java.sql.SQLException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginView {
    private Stage primaryStage;
    private UserOperations userOperations;

    public LoginView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public VBox getView() {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #0d47a1, #1e88e5);");
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setPrefSize(1280, 720); // Ukuran web fullscreen (HD layout)

        VBox card = new VBox(25);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40));
        card.setMaxWidth(500);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0.5, 0, 4);" +
            "-fx-border-radius: 12;"
        );

        Label titleLabel = new Label("🔐 Login Akun");
        titleLabel.setStyle(
            "-fx-font-size: 32px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #0d47a1;"
        );

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setPrefHeight(45);
        usernameField.setMaxWidth(Double.MAX_VALUE);
        usernameField.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-padding: 10px;" +
            "-fx-background-radius: 6;" +
            "-fx-border-color: #1e88e5;" +
            "-fx-border-radius: 6;"
        );

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefHeight(45);
        passwordField.setMaxWidth(Double.MAX_VALUE);
        passwordField.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-padding: 10px;" +
            "-fx-background-radius: 6;" +
            "-fx-border-color: #1e88e5;" +
            "-fx-border-radius: 6;"
        );

        Button loginButton = new Button("Login");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setPrefHeight(45);
        loginButton.setStyle(
            "-fx-background-color: #1e88e5;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 6;"
        );

        loginButton.setOnMouseEntered(e -> loginButton.setStyle(
            "-fx-background-color: #1565c0;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 6;"
        ));

        loginButton.setOnMouseExited(e -> loginButton.setStyle(
            "-fx-background-color: #1e88e5;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 6;"
        ));

        Label registerLink = new Label("Belum punya akun? Register di sini.");
        registerLink.setStyle(
            "-fx-text-fill: #0d47a1;" +
            "-fx-underline: true;" +
            "-fx-font-size: 14px;"
        );
        registerLink.setOnMouseClicked(event -> {
            RegisterView registerView = new RegisterView(primaryStage);
            Scene registerScene = new Scene(registerView.getView(), 1280, 720);
            primaryStage.setScene(registerScene);
        });

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            try {
                userOperations = new UserOperations();
                if (userOperations.loginUser(username, password)) {
                    DashboardView dashboardView = new DashboardView(primaryStage, username);
                    Scene dashboardScene = new Scene(dashboardView.getView(), 1280, 720);
                    primaryStage.setScene(dashboardScene);
                } else {
                    showError("Login gagal! Periksa username dan password Anda.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                showError("Koneksi database gagal!");
            }
        });

        // Tambahkan semua elemen ke dalam card
        VBox.setVgrow(usernameField, Priority.ALWAYS);
        VBox.setVgrow(passwordField, Priority.ALWAYS);
        VBox.setVgrow(loginButton, Priority.ALWAYS);
        card.getChildren().addAll(titleLabel, usernameField, passwordField, loginButton, registerLink);

        root.getChildren().add(card);
        return root;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Kesalahan");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
