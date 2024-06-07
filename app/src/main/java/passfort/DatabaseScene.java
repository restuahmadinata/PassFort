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

import java.util.Optional;

public class DatabaseScene {
    private Stage primaryStage;
    private int userId;
    private ContactController contactController;

    public DatabaseScene(Stage primaryStage, int userId) {
        this.primaryStage = primaryStage;
        this.userId = userId;
        this.contactController = new ContactController();
    }

    @SuppressWarnings("unchecked")
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

        Button userProfile = new Button("PROFILE");
        userProfile.setOnAction(v -> {
            UserScene userScene = new UserScene(primaryStage, userId);
            try {
                userScene.show();
            } catch (SQLiteException e) {

                e.printStackTrace();
            }
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
        userProfile.getStyleClass().add("menuButton");
        aboutUs.getStyleClass().add("menuButton");
        exit.getStyleClass().add("menuButton");

        menu.getChildren().addAll(menuTitle, newPassword, updatePassword, deletePassword, generatePassword, passwordDatabase, userProfile, aboutUs, exit);

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
        tableView.setItems(contactController.selectUserAppDataByUserId(userId));

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
                togglePasswords.setText("Show Passwords");
            } else {
                // Show a dialog to ask for the user's password
                PasswordField passwordFieldUser = new PasswordField();
                passwordFieldUser.setPromptText("Enter your password");
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle("Password Verification");
                dialog.setHeaderText("Please enter your password to show all saved passwords");
                dialog.getDialogPane().setContent(passwordFieldUser);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    String inputPassword = passwordFieldUser.getText();
                    if (contactController.verifyPassword(userId, inputPassword)) {
                        passwordCol.setVisible(true);
                        togglePasswords.setText("Hide Passwords");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Authentication Failed", "The password you entered is incorrect.");
                    }
                }
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

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/styles/database.css").toExternalForm());
        alert.showAndWait();
    }
}
