package jafafx.kasir.transportasi;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;

public class DashboardView {
    private Stage primaryStage;
    private UserOperations userOperations;
    private KendaraanOperations kendaraanOps;
    private RuteOperations ruteOps;
    private TransaksiOperations transaksiOps;
    private String username;

    private VBox sidebar;
    private boolean sidebarVisible = true;
    private BorderPane root;

    public DashboardView(Stage primaryStage, String username) {
        this.primaryStage = primaryStage;
        this.username = username;

        try {
            this.userOperations = new UserOperations();
            this.kendaraanOps = new KendaraanOperations();
            this.ruteOps = new RuteOperations();
            this.transaksiOps = new TransaksiOperations();
        } catch (SQLException ex) {
            Logger.getLogger(DashboardView.class.getName()).log(Level.SEVERE, "Initialization failed", ex);
        }
    }

    public void show() {
        Scene scene = new Scene(getView(), 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Dashboard");
        primaryStage.show();
    }

    public Parent getView() {
        root = new BorderPane();
        root.setPadding(new Insets(0));
        root.setStyle("-fx-background-color: #f4f6f8;");

        sidebar = createSidebar();
        Node topBar = createTopBar();
        VBox content = createContent();

        HBox footer = createFooter();

        root.setTop(topBar);
        root.setLeft(sidebar);
        root.setCenter(content);
        root.setBottom(footer);

        return root;
    }

    private Node createTopBar() {
        HBox topBar = new HBox();
        topBar.setStyle("-fx-background-color: #0d47a1;");
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_LEFT);

        Button toggleBtn = new Button("☰");
        toggleBtn.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-background-color: transparent;");
        toggleBtn.setOnAction(e -> toggleSidebar());

        topBar.getChildren().add(toggleBtn);
        return topBar;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #1565c0;");
        sidebar.setPrefWidth(220);
        sidebar.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("📊 Dashboard");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        Button kendaraanBtn = createSidebarButton("🚗 Kendaraan", () -> {
            new KendaraanView(primaryStage, username).show();
        });

        Button ruteBtn = createSidebarButton("🛣 Rute", () -> {
            new RuteView(primaryStage, username).show();
        });

        Button transaksiBtn = createSidebarButton("💰 Transaksi", () -> {
            try {
                new TransaksiView(primaryStage, username).show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button logoutBtn = createSidebarButton("🚪 Logout", () -> {
            primaryStage.setScene(new Scene(new LoginView(primaryStage).getView(), 800, 600));
        });

        sidebar.getChildren().addAll(title, kendaraanBtn, ruteBtn, transaksiBtn, logoutBtn);
        return sidebar;
    }

    private void toggleSidebar() {
        sidebarVisible = !sidebarVisible;
        root.setLeft(sidebarVisible ? sidebar : null);
    }

    private VBox createContent() {
        VBox content = new VBox(30);
        content.setPadding(new Insets(40));
        content.setStyle("-fx-background-color: #f4f6f8;");

        Label welcome = new Label("👋 Selamat datang, " + username + "!");
        welcome.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #0d47a1;");

        Label subtitle = new Label("Statistik Sistem");
        subtitle.setStyle("-fx-font-size: 18px; -fx-text-fill: #333;");

        HBox statsBox = new HBox(30);
        statsBox.setAlignment(Pos.CENTER_LEFT);

        statsBox.getChildren().addAll(
            createStatCard("🚗 Total Kendaraan", getCountSafely(() -> kendaraanOps.getAll().size())),
            createStatCard("🛣 Total Rute", getCountSafely(() -> ruteOps.getAll().size())),
            createStatCard("💰 Total Transaksi", getCountSafely(() -> transaksiOps.getAll().size()))
        );

        VBox profileCard = createProfileCard();

        content.getChildren().addAll(welcome, subtitle, statsBox, profileCard);
        return content;
    }

    private VBox createStatCard(String title, int value) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 3);");
        card.setPrefWidth(200);

        Label label = new Label(title);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

        Label val = new Label(String.valueOf(value));
        val.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #0d47a1;");

        card.getChildren().addAll(label, val);
        return card;
    }

    private VBox createProfileCard() {
        VBox profileBox = new VBox(8);
        profileBox.setPadding(new Insets(20));
        profileBox.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10px;" +
            "-fx-border-color: #e0e0e0;" +
            "-fx-border-radius: 10px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 3);"
        );

        if (userOperations != null) {
            User user = userOperations.getProfile(username);
            if (user != null) {
                Label profileLabel = new Label("👤 Username: " + user.getUsername());
                Label roleLabel = new Label("🔐 Role: " + user.getRole());
                profileLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #444;");
                roleLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #444;");
                profileBox.getChildren().addAll(profileLabel, roleLabel);
            } else {
                profileBox.getChildren().add(new Label("❌ Gagal memuat profil pengguna."));
            }
        } else {
            profileBox.getChildren().add(new Label("❌ UserOperations tidak tersedia."));
        }

        return profileBox;
    }

    private Button createSidebarButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle("-fx-background-color: #1e88e5; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 6px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #1565c0; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 6px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #1e88e5; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 6px;"));
        button.setOnAction(e -> action.run());
        return button;
    }

    private int getCountSafely(CountSupplier supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return 0;
        }
    }

    private HBox createFooter() {
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(15));
        footer.setStyle("-fx-background-color: #eeeeee;");

        Label copyright = new Label("© 2025 Mela Septiani - Sistem Kasir Transportasi");
        copyright.setStyle("-fx-font-size: 12px; -fx-text-fill: #777;");
        footer.getChildren().add(copyright);
        return footer;
    }

    @FunctionalInterface
    interface CountSupplier {
        int get() throws Exception;
    }
}
