package passfort;

import org.sqlite.SQLiteException;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import passfort.com.example.controller.ContactController;
import passfort.models.UserAppData;

public class DatabaseScene {
    private Stage primaryStage;
    private int userId;

    public DatabaseScene(Stage primaryStage, int userId) {
        this.primaryStage = primaryStage;
        this.userId = userId;
    }

    public void show() {
        BorderPane mainLayout = new BorderPane();

        VBox menu = new VBox();
        menu.setSpacing(10);
        menu.setId("menu");

        Label menuTitle = new Label("MENU");
        menuTitle.setId("menuTitle");

        Button newPassword = new Button("→ New Password");
        newPassword.setOnAction(v -> {
            CreateScene createScene = new CreateScene(primaryStage, userId);
            createScene.show();
        });

        Button updatePassword = new Button("→ Update Password");
        updatePassword.setOnAction(v -> {
            UpdateScene updateScene = new UpdateScene(primaryStage, userId);
            try {
                updateScene.show();
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
        });

        Button deletePassword = new Button("→ Delete Password");
        deletePassword.setOnAction(v -> {
            DeleteScene deleteScene = new DeleteScene(primaryStage, userId);
            try {
                deleteScene.show();
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
        });

        Button generatePassword = new Button("→ Generate Password");
        generatePassword.setOnAction(v -> {
            GenerateScene generateScene = new GenerateScene(primaryStage, userId);
            try {
                generateScene.show();
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
        });

        Button passwordDatabase = new Button("→ Password database");
        passwordDatabase.setOnAction(v -> {
            DatabaseScene databaseScene = new DatabaseScene(primaryStage, userId);
            databaseScene.show();
        });

        Button aboutUs = new Button("ABOUT US");
        aboutUs.setOnAction(v -> {
            AboutScene aboutScene = new AboutScene(primaryStage, userId);
            try {
                aboutScene.show();
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
        });

        Button exit = new Button("EXIT");
        exit.setOnAction(v -> {
            LoginScene loginScene = new LoginScene(primaryStage, userId);
            loginScene.show();
        });

        newPassword.getStyleClass().add("menuButton");
        updatePassword.getStyleClass().add("menuButton");
        deletePassword.getStyleClass().add("menuButton");
        generatePassword.getStyleClass().add("menuButton");
        passwordDatabase.setId("databaseMenu");
        aboutUs.getStyleClass().add("menuButton");
        exit.getStyleClass().add("menuButton");

        menu.getChildren().addAll(menuTitle, newPassword, updatePassword, deletePassword, generatePassword, passwordDatabase, aboutUs, exit);

        VBox formLayout = new VBox();
        formLayout.setId("form");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setId("scrollPane");
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);

        VBox titleContainer = new VBox();
        Label formTitle = new Label("Password Database");
        formTitle.setId("formTitle");

        Label formSubtitle = new Label("Here we collect every single account you've created");
        formSubtitle.setId("formSubtitle");
        titleContainer.getChildren().addAll(formTitle, formSubtitle);
        titleContainer.setSpacing(3);

        TableView<UserAppData> tableView = new TableView<>();
        tableView.setItems(new ContactController().selectUserAppDataByUserId(userId));

        TableColumn<UserAppData, String> appCol = new TableColumn<>("App");
        appCol.setCellValueFactory(new PropertyValueFactory<>("apps"));
        appCol.prefWidthProperty().bind(tableView.widthProperty().multiply(0.3));

        TableColumn<UserAppData, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameCol.prefWidthProperty().bind(tableView.widthProperty().multiply(0.3));

        TableColumn<UserAppData, String> passwordCol = new TableColumn<>("Password");
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordCol.prefWidthProperty().bind(tableView.widthProperty().multiply(0.4));
        passwordCol.setVisible(false); // Initially hide the password column

        tableView.getColumns().addAll(appCol, usernameCol, passwordCol);

        Button togglePasswords = new Button("Show Passwords");
        togglePasswords.setId("showPass");
        togglePasswords.setOnAction(event -> {
            if (passwordCol.isVisible()) {
                passwordCol.setVisible(false);
                togglePasswords.setText("Show Pass");
            } else {
                passwordCol.setVisible(true);
                togglePasswords.setText("Hide Pass");
            }
        });

        Label line = new Label();

        formLayout.getChildren().addAll(titleContainer, tableView, togglePasswords, line);
        formLayout.setSpacing(50);
        scrollPane.setContent(formLayout);

        mainLayout.setLeft(menu);
        mainLayout.setCenter(scrollPane);

        Scene scene = new Scene(mainLayout, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/styles/database.css").toExternalForm());
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
