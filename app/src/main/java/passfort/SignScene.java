package passfort;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import passfort.com.example.controller.ContactController;

public class SignScene {
    private Stage primaryStage;

    public SignScene(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void show() {

        // Create the form layout
        VBox loginLayout = new VBox();
        loginLayout.setId("form");

        // Form title
        VBox titleContainer = new VBox();

        Label greeting = new Label("Welcome to");
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
        Button signButton = new Button("SIGN IN");
        signButton.setId("signButton");

        signButton.setOnAction(event -> {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                // Display an error message if the username or password is empty
                showAlert(Alert.AlertType.ERROR, "Input Error", "Semua field harus diisi!");
            } else {
                try {
                    // Create an instance of ContactController and check if the username already exists
                    ContactController contactController = new ContactController();
                    if (contactController.isUsernameTaken(username)) {
                        showAlert(Alert.AlertType.ERROR, "Registration Error", "Username sudah digunakan");
                    } else {
                        // Insert the user
                        contactController.insertUser(username, password);

                        // Show a success message
                        showAlert(Alert.AlertType.INFORMATION, "Success", "User signed up successfully!");

                        // Navigate to the CreateScene
                        CreateScene createScene = new CreateScene(primaryStage);
                        createScene.show();
                    }
                } catch (Exception e) {
                    // Display an error message if there is a database error
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Tidak dapat terhubung ke database, coba lagi nanti");
                }
            }
        });

        Button loginButton = new Button("Already have an account? Log In!");
        loginButton.setId("loginButton");

        loginButton.setOnAction(v -> {
            LoginScene loginScene = new LoginScene(primaryStage);
            loginScene.show();
        });

        VBox buttonContainer = new VBox();
        buttonContainer.getChildren().addAll(signButton, loginButton);
        buttonContainer.setSpacing(20);
        buttonContainer.setAlignment(Pos.CENTER);

        // Add fields to the form layout
        loginLayout.getChildren().addAll(titleContainer, formContainer, buttonContainer);
        loginLayout.setSpacing(50);
        loginLayout.setAlignment(Pos.CENTER);

        // Create the scene and set it to the stage
        Scene scene = new Scene(loginLayout, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/styles/sign.css").toExternalForm());
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
