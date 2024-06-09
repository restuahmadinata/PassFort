package passfort;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import passfort.com.example.controller.ContactController;

import java.util.ArrayList;
import java.util.List;

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
        VBox signUpLayout = new VBox();
        signUpLayout.setId("form");

        VBox titleContainer = new VBox();

        Label greeting = new Label("Welcome to");
        greeting.setId("greeting");

        Label formTitle = new Label("PASSFORT");
        formTitle.setId("formTitle");

        Label formSubtitle = new Label("- a minimalist password manager -");
        formSubtitle.setId("formSubtitle");
        titleContainer.getChildren().addAll(greeting, formTitle, formSubtitle);
        titleContainer.setSpacing(3);
        titleContainer.setAlignment(Pos.CENTER);

        VBox formContainer = new VBox();

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

        HBox passwordField = new HBox();
        Label passwordLabel = new Label("Password\t-→");
        passwordLabel.getStyleClass().add("fieldLabel");
        passwordLabel.setPrefWidth(230);

        PasswordField passwordTextField = new PasswordField();
        passwordTextField.getStyleClass().add("field");
        passwordTextField.setPrefWidth(350);
        passwordTextField.setPrefHeight(20);

        TextField textField = new TextField();
        textField.getStyleClass().add("field");
        textField.setPrefWidth(350);
        textField.setPrefHeight(20);
        textField.setManaged(false);
        textField.setVisible(false);

        CheckBox showPasswordCheckBox = new CheckBox("Show");
        showPasswordCheckBox.setId("showPass");

        textField.managedProperty().bind(showPasswordCheckBox.selectedProperty());
        textField.visibleProperty().bind(showPasswordCheckBox.selectedProperty());
        passwordTextField.managedProperty().bind(showPasswordCheckBox.selectedProperty().not());
        passwordTextField.visibleProperty().bind(showPasswordCheckBox.selectedProperty().not());

        textField.textProperty().bindBidirectional(passwordTextField.textProperty());

        passwordField.getChildren().addAll(passwordLabel, passwordTextField, textField, showPasswordCheckBox);
        passwordField.setSpacing(5);
        passwordField.setAlignment(Pos.CENTER);

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

        Button signUpButton = new Button("SIGN UP");
        signUpButton.setId("signButton");

        signUpButton.setOnAction(event -> {
            String fullName = fullNameTextField.getText();
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            String confirmPassword = confirmPasswordTextField.getText();

            if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all fields!");
            } else if (!password.equals(confirmPassword)) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Passwords do not match!");
            } else {
                try {
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

        Tooltip usernameTooltip = new Tooltip(getUsernameRequirements());
        usernameTextField.setTooltip(usernameTooltip);
        usernameTooltip.setShowDelay(Duration.seconds(0.3));
        usernameTooltip.setShowDuration(Duration.seconds(60));

        usernameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateUsernameTooltip(usernameTooltip, newValue);
        });

        Tooltip passwordTooltip = new Tooltip(getPasswordRequirements());
        passwordTextField.setTooltip(passwordTooltip);
        textField.setTooltip(passwordTooltip);
        passwordTooltip.setShowDelay(Duration.seconds(0.3));
        passwordTooltip.setShowDuration(Duration.seconds(60));

        passwordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            updatePasswordTooltip(passwordTooltip, newValue);
        });

        VBox buttonContainer = new VBox();
        buttonContainer.getChildren().addAll(signUpButton, loginButton);
        buttonContainer.setSpacing(20);
        buttonContainer.setAlignment(Pos.CENTER);

        signUpLayout.getChildren().addAll(titleContainer, formContainer, buttonContainer);
        signUpLayout.setSpacing(50);
        signUpLayout.setAlignment(Pos.CENTER);

        

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

    private String getUsernameRequirements() {
        return "-> Username must be 5-20 characters long\n" +
               "-> Can contain letters, numbers, and underscores\n" +
               "-> Cannot start or end with an underscore\n" +
               "-> Cannot contain consecutive underscores";
    }

    private String getPasswordRequirements() {
        return "-> Password must be at least 8 characters long\n" +
               "-> Contain at least one uppercase letter\n" +
               "-> One lowercase letter\n" +
               "-> One digit\n" +
               "-> One special character (@, $, !, %, *, ?, &)";
    }

    private void updateUsernameTooltip(Tooltip tooltip, String username) {
        List<String> errors = new ArrayList<>();
        if (username.length() < 5 || username.length() > 20) {
            errors.add("-> Username must be 5-20 characters long");
        }
        if (!username.matches("^[a-zA-Z0-9_]*$")) {
            errors.add("-> Can contain letters, numbers, and underscores only");
        }
        if (username.startsWith("_") || username.endsWith("_")) {
            errors.add("-> Cannot start or end with an underscore");
        }
        if (username.contains("__")) {
            errors.add("-> Cannot contain consecutive underscores");
        }
        if (errors.isEmpty()) {
            tooltip.setText("Username is valid");
        } else {
            tooltip.setText(String.join("\n", errors));
        }
    }

    private void updatePasswordTooltip(Tooltip tooltip, String password) {
        List<String> errors = new ArrayList<>();
        if (password.length() < 8) {
            errors.add("-> Password must be at least 8 characters long");
        }
        if (!password.matches(".*[A-Z].*")) {
            errors.add("-> Must contain at least one uppercase letter");
        }
        if (!password.matches(".*[a-z].*")) {
            errors.add("-> Must contain at least one lowercase letter");
        }
        if (!password.matches(".*\\d.*")) {
            errors.add("-> Must contain at least one digit");
        }
        if (!password.matches(".*[@$!%*?&].*")) {
            errors.add("-> Must contain at least one special character (@, $, !, %, *, ?, &)");
        }
        if (errors.isEmpty()) {
            tooltip.setText("Password is valid");
        } else {
            tooltip.setText(String.join("\n", errors));
        }
    }
}
