package passfort;

import java.sql.SQLException;
import org.sqlite.SQLiteException;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import passfort.com.example.controller.ContactController;

public class UpdateScene {
    private Stage primaryStage;
    private int userId;

    public UpdateScene(Stage primaryStage, int userId) {
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
        updatePassword.setId("updateMenu");
        deletePassword.getStyleClass().add("menuButton");
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
        Label formTitle = new Label("UPDATE");
        formTitle.setId("formTitle");

        Label formSubtitle = new Label("Update your existing account and password detail");
        
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

        HBox passwordField = new HBox();
        Label passwordLabel = new Label("password\t-→");
        passwordLabel.getStyleClass().add("fieldLabel");
        passwordLabel.setPrefWidth(260);


        PasswordField passwordTextField = new PasswordField();
        passwordTextField.getStyleClass().add("field");
        passwordTextField.setPrefWidth(350);
        passwordTextField.setPrefHeight(30);
        
        // TextField to show password
        TextField textField = new TextField();
        textField.getStyleClass().add("field");
        textField.setPrefWidth(350);
        textField.setPrefHeight(30);
        textField.setManaged(false);
        textField.setVisible(false);
        
        // CheckBox to show/hide password
        CheckBox showPasswordCheckBox = new CheckBox("Show?");
        showPasswordCheckBox.setId("showPass");

        // Bind properties
        textField.managedProperty().bind(showPasswordCheckBox.selectedProperty());
        textField.visibleProperty().bind(showPasswordCheckBox.selectedProperty());
        passwordTextField.managedProperty().bind(showPasswordCheckBox.selectedProperty().not());
        passwordTextField.visibleProperty().bind(showPasswordCheckBox.selectedProperty().not());

        // Synchronize text content
        textField.textProperty().bindBidirectional(passwordTextField.textProperty());

        // Adding components to the passwordField HBox
        passwordField.getChildren().addAll(passwordLabel, passwordTextField, textField, showPasswordCheckBox);
        passwordField.setSpacing(20);

        formContainer.getChildren().addAll(appField, usernameField, passwordField);
        formContainer.setSpacing(50);

        // Add button
        Label line = new Label("                                                               ");
        Button updateButton = new Button("Update");
        updateButton.setId("updateButton");

        HBox buttonContainer = new HBox(line, updateButton);

        // Add fields to the form layout
        formLayout.getChildren().addAll(titleContainer, formContainer, buttonContainer);
        formLayout.setSpacing(50);

        // Add the menu and form layout to the main layout
        mainLayout.setLeft(menu);
        mainLayout.setCenter(formLayout);

        // Create the scene and set it to the stage
        Scene scene = new Scene(mainLayout, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/styles/update.css").toExternalForm());
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Set the action for the updateButton
        updateButton.setOnAction(event -> {
            try {
                extracted(appComboBox, usernameTextField, passwordTextField);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void extracted(ComboBox<String> appComboBox, TextField usernameTextField, PasswordField passwordTextField)
    throws SQLException {
// Di dalam method extracted
String app = appComboBox.getValue();
String username = usernameTextField.getText();
String newPassword = passwordTextField.getText();

if (app != null && !app.isEmpty() && username != null && !username.isEmpty() && newPassword != null && !newPassword.isEmpty()) {
    ContactController contactController = new ContactController();
    try {
        // Panggil metode checkUserExistsForApp dengan nilai username dan app yang sesuai
        boolean userExistsForApp = contactController.checkUserExistsForApp(username, app);
        if (userExistsForApp) {
            // Update password jika user ditemukan untuk aplikasi yang dipilih
            contactController.updateUserPasswordForApp(username, newPassword, app);
            // Tampilkan pesan sukses atau lakukan tindakan yang sesuai
            showAlert("Password updated successfully.");
        } else {
            // Tampilkan pesan error jika user atau aplikasi tidak ditemukan
            showAlert("User or app not found.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
} else {
    // Tampilkan pesan error jika ada field yang kosong
    showAlert("Please fill in all fields.");
    }
}




    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Update Status");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

