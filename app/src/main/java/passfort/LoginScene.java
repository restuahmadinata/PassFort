package passfort;

import org.sqlite.SQLiteException;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import passfort.com.example.controller.ContactController;

public class LoginScene {
    private Stage primaryStage;
    private ContactController contactController;
    private int authResult;

    public LoginScene(Stage primaryStage, int authResult) {
        this.primaryStage = primaryStage;
        this.contactController = new ContactController();
        this.contactController.createTable();
        this.authResult = authResult;
    }

    public void show() {
        VBox loginLayout = new VBox();
        loginLayout.setId("form");

        VBox titleContainer = new VBox();

        Label greeting = new Label("Glad to have you back!");
        greeting.setId("greeting");

        Label formTitle = new Label("PASSFORT");
        formTitle.setId("formTitle");

        Label formSubtitle = new Label("- a minimalist password manager -");
        formSubtitle.setId("formSubtitle");
        titleContainer.getChildren().addAll(greeting, formTitle, formSubtitle);
        titleContainer.setSpacing(3);
        titleContainer.setAlignment(Pos.CENTER);

        VBox formContainer = new VBox();

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

        Tooltip passwordTooltip = new Tooltip("Remember to securely store your password!\n\nUse another media like notes, paper or something else.");
        passwordTooltip.setShowDelay(Duration.seconds(0.3));
        passwordTooltip.setShowDuration(Duration.seconds(60));
        Tooltip.install(passwordTextField, passwordTooltip);

        TextField textField = new TextField();
        textField.getStyleClass().add("field");
        textField.setPrefWidth(350);
        textField.setPrefHeight(20);
        textField.setManaged(false);
        textField.setVisible(false);
        textField.setTooltip(passwordTooltip);

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

        formContainer.getChildren().addAll(usernameField, passwordField);
        formContainer.setSpacing(30);
        formContainer.setAlignment(Pos.CENTER);

        Button loginButton = new Button("LOG IN");
        loginButton.setId("loginButton");
        
        loginButton.setOnAction(v -> {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
        
            if (username.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all fields!");
            } else {
                ContactController contactController = new ContactController();
                int authResult = contactController.authenticateUser(username, password);
                
                if (authResult > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "User logged in successfully!");
                    DatabaseScene databaseScene = new DatabaseScene(primaryStage, authResult);
                    try {
                        databaseScene.show();
                    } catch (SQLiteException e) {
                        e.printStackTrace();
                    }
                } else if (authResult < 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Admin has been logged in!");
                    AdminScene adminScene = new AdminScene(primaryStage, -authResult);
                    adminScene.show();
                } else if (authResult == 0) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Account not found!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Wrong password!");
                }
            }
        });

        Button signButton = new Button("No account? Sign Up!");
        signButton.setId("signButton");

        signButton.setOnAction(v -> {
            SignScene signScene = new SignScene(primaryStage, authResult);
            signScene.show();
        });

        Button forgotPasswordButton = new Button("Forgot Password?");
        forgotPasswordButton.setId("forgotPasswordButton");

        Tooltip forgotPasswordTooltip = new Tooltip("Really? Even though I've reminded you.");
        forgotPasswordTooltip.setShowDelay(Duration.seconds(0.3));
        forgotPasswordTooltip.setShowDuration(Duration.seconds(5));
        Tooltip.install(forgotPasswordButton, forgotPasswordTooltip);

        forgotPasswordButton.setOnMouseEntered(e -> {
            forgotPasswordTooltip.show(forgotPasswordButton, e.getScreenX(), e.getScreenY() + 10);
        });
        forgotPasswordButton.setOnMouseExited(e -> {
            forgotPasswordTooltip.hide();
        });

        forgotPasswordButton.setOnAction(event -> {
            showAlert(Alert.AlertType.INFORMATION, "Forgot Password", 
                    "Please contact support for password recovery assistance.\n\n- Dev Whatsapp - 087723390480");
        });

        VBox buttonContainer = new VBox();
        buttonContainer.getChildren().addAll(loginButton, signButton, forgotPasswordButton);
        buttonContainer.setSpacing(20);
        buttonContainer.setAlignment(Pos.CENTER);

        loginLayout.getChildren().addAll(titleContainer, formContainer, buttonContainer);
        loginLayout.setSpacing(50);
        loginLayout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(loginLayout, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
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
        dialogPane.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
        alert.showAndWait();
    }
}