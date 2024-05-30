package passfort;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import passfort.com.example.controller.ContactController;

public class LoginScene {
    private Stage primaryStage;
    private ContactController contactController;
    private int authResult;

    public LoginScene(Stage primaryStage, int authResult) {
        this.primaryStage = primaryStage;
        this.contactController = new ContactController();
        this.contactController.createTable(); // Ensure the table is created
        this.authResult = authResult;
    }

    public void show() {
        // Create the form layout
        VBox loginLayout = new VBox();
        loginLayout.setId("form");

        // Form title
        VBox titleContainer = new VBox();

        Label greeting = new Label("Glad to have you back!");
        greeting.setId("greeting");

        Label formTitle = new Label("PASSFORT");
        formTitle.setId("formTitle");

        Label formSubtitle = new Label("a minimalist password manager");
        formSubtitle.setId("formSubtitle");
        titleContainer.getChildren().addAll(greeting, formTitle, formSubtitle);
        titleContainer.setSpacing(3);
        titleContainer.setAlignment(Pos.CENTER);

        // Create form fields
        VBox formContainer = new VBox();

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
        usernameField.setAlignment(Pos.CENTER);

        // Password HBox
        HBox passwordField = new HBox();
        Label passwordLabel = new Label("password\t-→");
        passwordLabel.getStyleClass().add("fieldLabel");
        passwordLabel.setPrefWidth(275);

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
        passwordField.setSpacing(5);
        passwordField.setAlignment(Pos.CENTER);

        formContainer.getChildren().addAll(usernameField, passwordField);
        formContainer.setSpacing(50);
        formContainer.setAlignment(Pos.CENTER);

        // Add button
        Button loginButton = new Button("LOG IN");
        loginButton.setId("loginButton");

        loginButton.setOnAction(v -> {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Semua field harus diisi!");
            } else {
                authResult = authenticate(username, password); // Get the result code
                if (authResult > 0) {
                    if (username.equals("admin") && password.equals("ambatukam")) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Admin has been logged in!");
                        AdminScene adminScene = new AdminScene(primaryStage, authResult);
                        adminScene.show();
                    } else {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "User logged in successfully!");
                        CreateScene createScene = new CreateScene(primaryStage, authResult);
                        createScene.show();
                    }

                } else if (authResult == -1) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Wrong password!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Akun tidak ditemukan!");
                }
            }
        });

        Button signButton = new Button("No account? Sign In!");
        signButton.setId("signButton");

        signButton.setOnAction(v -> {
            SignScene signScene = new SignScene(primaryStage, authResult);
            signScene.show();
        });

        VBox buttonContainer = new VBox();
        buttonContainer.getChildren().addAll(loginButton, signButton);
        buttonContainer.setSpacing(20);
        buttonContainer.setAlignment(Pos.CENTER);

        // Add fields to the form layout
        loginLayout.getChildren().addAll(titleContainer, formContainer, buttonContainer);
        loginLayout.setSpacing(50);
        loginLayout.setAlignment(Pos.CENTER);

        // Create the scene and set it to the stage
        Scene scene = new Scene(loginLayout, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private int authenticate(String username, String password) {
        return contactController.authenticateUser(username, password);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}