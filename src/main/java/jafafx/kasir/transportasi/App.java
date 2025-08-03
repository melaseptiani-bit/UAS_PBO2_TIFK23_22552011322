package jafafx.kasir.transportasi;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Inisialisasi Root Layout
            BorderPane root = new BorderPane();

            // Tampilkan Halaman LoginView terlebih dahulu
            LoginView loginView = new LoginView(primaryStage);
            if (loginView.getView() != null) {
                root.setCenter(loginView.getView());
            } else {
                System.err.println("❌ Gagal memuat tampilan LoginView.");
                return;
            }

            // Buat Scene
            Scene scene = new Scene(root, 1200, 800);

            // Atur Stage
            primaryStage.setTitle("Kasir Transportasi App");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("❌ Terjadi kesalahan saat memulai aplikasi:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
