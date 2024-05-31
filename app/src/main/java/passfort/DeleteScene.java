package passfort;

import java.sql.SQLException;

import org.sqlite.SQLiteException;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import passfort.com.example.controller.ContactController;

public class DeleteScene {
    private Stage primaryStage;
    private int userId;

    public DeleteScene(Stage primaryStage, int userId) {
        this.primaryStage = primaryStage;
        this.userId = userId;
    }

    public void show() throws SQLiteException {
        // Create the main layout
        BorderPane mainLayout = new BorderPane();

        // Create the menu
        VBox menu = new VBox();
        menu.setSpacing(10);
        menu.setId("menu");

        // Create menu items
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
            try {
                databaseScene.show();
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
        deletePassword.setId("deleteMenu");
        generatePassword.getStyleClass().add("menuButton");
        passwordDatabase.getStyleClass().add("menuButton");
        aboutUs.getStyleClass().add("menuButton");
        exit.getStyleClass().add("menuButton");

        // Add items to the menu
        menu.getChildren().addAll(menuTitle, newPassword, updatePassword, deletePassword, generatePassword, passwordDatabase, aboutUs, exit);

        // Create the form layout
        VBox formLayout = new VBox();
        formLayout.setId("form");

        // Form title
        VBox titleContainer = new VBox();
        Label formTitle = new Label("DELETE");
        formTitle.setId("formTitle");

        Label formSubtitle = new Label("Delete your existing account and password from the database");

        formSubtitle.setId("formSubtitle");
        titleContainer.getChildren().addAll(formTitle, formSubtitle);
        titleContainer.setSpacing(3);

        // Create form fields
        VBox formContainer = new VBox();

        HBox appField = new HBox();
        Label appLabel = new Label("apps\t\t-→");
        appLabel.getStyleClass().add("fieldLabel");

        ComboBox<String> appComboBox = new ComboBox<>();
        appComboBox.setPromptText("Select app");
        appComboBox.setId("appBox");
        appComboBox.setPrefWidth(460);
        appComboBox.setPrefHeight(20);
        appField.getChildren().addAll(appLabel, appComboBox);
        appField.setSpacing(20);
        appComboBox.getItems().addAll(
                "Google", "Facebook", "Instagram", "Paypal", "Gopay",
                "DANA", "OVO", "Gojek", "Grab", "Steam"
        );

        HBox usernameField = new HBox();
        Label usernameLabel = new Label("username\t-→");
        usernameLabel.getStyleClass().add("fieldLabel");

        TextField usernameTextField = new TextField();
        usernameTextField.getStyleClass().add("field");
        usernameTextField.setPrefWidth(460);
        usernameTextField.setPrefHeight(30);
        usernameField.getChildren().addAll(usernameLabel, usernameTextField);
        usernameField.setSpacing(20);
        usernameField.setId("username");

        formContainer.getChildren().addAll(appField, usernameField);
        formContainer.setSpacing(50);

        // Add button
        Label line = new Label("                                                               ");
        Button deleteButton = new Button("Delete");
        deleteButton.setId("deleteButton");

        HBox buttonContainer = new HBox(line, deleteButton);

        // Add fields to the form layout
        formLayout.getChildren().addAll(titleContainer, formContainer, buttonContainer);
        formLayout.setSpacing(50);

        // Add the menu and form layout to the main layout
        mainLayout.setLeft(menu);
        mainLayout.setCenter(formLayout);

        // Create the scene and set it to the stage
        Scene scene = new Scene(mainLayout, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/styles/delete.css").toExternalForm());
        primaryStage.setTitle("PassFort");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Set the action for the deleteButton
        deleteButton.setOnAction(event -> {
            String app = appComboBox.getValue();
            String username = usernameTextField.getText();

            if (app != null && !app.isEmpty() && username != null && !username.isEmpty()) {
                ContactController contactController = new ContactController();
                try {
                    // Check if the user exists in the AppDatabase
                    boolean userExistsForApp = contactController.checkUserExistsForApp(username, app, userId);
                    if (userExistsForApp) {
                        // Delete the user from AppDatabase
                        contactController.deleteUserFromAppDatabase(username, app, userId);
                        // Show success alert
                        showAlert(Alert.AlertType.INFORMATION, "Success", "User data deleted successfully from AppDatabase.");
                    } else {
                        // Show error alert if user or app not found
                        showAlert(Alert.AlertType.ERROR, "Error", "User or app not found in the AppDatabase.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while accessing the database.");
                }
            } else {
                // Show error alert if any field is empty
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/styles/delete.css").toExternalForm());
        alert.showAndWait();
    }
}
