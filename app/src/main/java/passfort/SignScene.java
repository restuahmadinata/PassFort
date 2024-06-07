package passfort;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import passfort.com.example.controller.ContactController;

public class SignScene {
    private Stage primaryStage;
    private int authResult;
    private ContactController contactController;

    public SignScene(Stage primaryStage, int authResult) {
        this.primaryStage = primaryStage;
        this.authResult = authResult;
        this.contactController = new ContactController();
    }

    public void show() {

        // Create the form layout
        VBox signUpLayout = new VBox();
        signUpLayout.setId("form");

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

        // Full Name Field
        HBox fullNameField = new HBox();
        Label fullNameLabel = new Label("Your Name\t-→");
        fullNameLabel.getStyleClass().add("fieldLabel");

        TextField fullNameTextField = new TextField();
        fullNameTextField.getStyleClass().add("field");
        fullNameTextField.setPrefWidth(460);
        fullNameTextField.setPrefHeight(20);
        fullNameField.getChildren().addAll(fullNameLabel, fullNameTextField);
        fullNameField.setSpacing(20);
        fullNameField.setId("fullName");
        fullNameField.setAlignment(Pos.CENTER);

        // Username Field
        HBox usernameField = new HBox();
        Label usernameLabel = new Label("Username\t-→");
        usernameLabel.getStyleClass().add("fieldLabel");

        TextField usernameTextField = new TextField();
        usernameTextField.getStyleClass().add("field");
        usernameTextField.setPrefWidth(460);
        usernameTextField.setPrefHeight(20);
        usernameField.getChildren().addAll(usernameLabel, usernameTextField);
        usernameField.setSpacing(20);
        usernameField.setId("username");
        usernameField.setAlignment(Pos.CENTER);

        // Password Field
        HBox passwordField = new HBox();
        Label passwordLabel = new Label("Password\t-→");
        passwordLabel.getStyleClass().add("fieldLabel");
        passwordLabel.setPrefWidth(230);

        PasswordField passwordTextField = new PasswordField();
        passwordTextField.getStyleClass().add("field");
        passwordTextField.setPrefWidth(350);
        passwordTextField.setPrefHeight(20);

        // TextField to show password
        TextField textField = new TextField();
        textField.getStyleClass().add("field");
        textField.setPrefWidth(350);
        textField.setPrefHeight(20);
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

        // Confirm Password Field
        HBox confirmPasswordField = new HBox();
        Label confirmPasswordLabel = new Label("Confirm Pass\t-→");
        confirmPasswordLabel.getStyleClass().add("fieldLabel");

        PasswordField confirmPasswordTextField = new PasswordField();
        confirmPasswordTextField.getStyleClass().add("field");
        confirmPasswordTextField.setPrefWidth(460);
        confirmPasswordTextField.setPrefHeight(20);

        confirmPasswordField.getChildren().addAll(confirmPasswordLabel, confirmPasswordTextField);
        confirmPasswordField.setSpacing(20);
        confirmPasswordField.setId("confirmPassword");
        confirmPasswordField.setAlignment(Pos.CENTER);

        formContainer.getChildren().addAll(fullNameField, usernameField, passwordField, confirmPasswordField);
        formContainer.setSpacing(30);
        formContainer.setAlignment(Pos.CENTER);

        // Add button
        Button signUpButton = new Button("SIGN UP");
        signUpButton.setId("signButton");

        signUpButton.setOnAction(event -> {
            String fullName = fullNameTextField.getText();
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            String confirmPassword = confirmPasswordTextField.getText();

            if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                // Display an error message if any field is empty
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all fields!");
            } else if (!password.equals(confirmPassword)) {
                // Display an error message if the passwords do not match
                showAlert(Alert.AlertType.ERROR, "Input Error", "Passwords do not match!");
            } else {
                try {
                    // Create an instance of ContactController and check if the username already exists
                    ContactController contactController = new ContactController();
                    if (contactController.isUsernameTaken(username)) {
                        showAlert(Alert.AlertType.ERROR, "Registration Error", "Username is already used");
                    } else {
                        contactController.insertUser(username, password, "Regular", fullName);
                        authResult = authenticate(username, password);
                        showAlert(Alert.AlertType.INFORMATION, "Success", "User signed up successfully!");
                        DatabaseScene databaseScene = new DatabaseScene(primaryStage, authResult);
                        databaseScene.show();
                    }
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Can't connect to database!");
                }
            }
        });

        Button loginButton = new Button("Already have an account? Log In!");
        loginButton.setId("loginButton");

        loginButton.setOnAction(v -> {
            LoginScene loginScene = new LoginScene(primaryStage, authResult);
            loginScene.show();
        });

        VBox buttonContainer = new VBox();
        buttonContainer.getChildren().addAll(signUpButton, loginButton);
        buttonContainer.setSpacing(20);
        buttonContainer.setAlignment(Pos.CENTER);

        // Add fields to the form layout
        signUpLayout.getChildren().addAll(titleContainer, formContainer, buttonContainer);
        signUpLayout.setSpacing(50);
        signUpLayout.setAlignment(Pos.CENTER);

        // Create the scene and set it to the stage
        Scene scene = new Scene(signUpLayout, 1280, 720);
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

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/styles/sign.css").toExternalForm());
        alert.showAndWait();
    }

    private int authenticate(String username, String password) {
        return contactController.authenticateUser(username, password);
    }
}
