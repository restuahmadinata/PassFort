package passfort;

import java.sql.SQLException;
import org.sqlite.SQLiteException;
import java.util.Optional;
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
        BorderPane mainLayout = new BorderPane();

        VBox menu = new VBox();
        menu.setSpacing(10);
        menu.setId("menu");

        Label menuTitle = new Label("MENU");
        menuTitle.setId("menuTitle");

        Button newPassword = new Button("→ New Password");
        newPassword.setOnAction(v -> {
            CreateScene createScene = new CreateScene(primaryStage, userId);
            try {
                createScene.show();
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
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

        Button userProfile = new Button("PROFILE");
        userProfile.setOnAction(v -> {
            UserScene userScene = new UserScene(primaryStage, userId);
            userScene.show();
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
        userProfile.getStyleClass().add("menuButton");
        aboutUs.getStyleClass().add("menuButton");
        exit.getStyleClass().add("menuButton");

        menu.getChildren().addAll(menuTitle, newPassword, updatePassword, deletePassword, generatePassword, passwordDatabase, userProfile, aboutUs, exit);

        VBox formLayout = new VBox();
        formLayout.setId("form");

        VBox titleContainer = new VBox();
        Label formTitle = new Label("UPDATE");
        formTitle.setId("formTitle");

        Label formSubtitle = new Label("Update your existing account and password detail");
        
        formSubtitle.setId("formSubtitle");
        titleContainer.getChildren().addAll(formTitle, formSubtitle);
        titleContainer.setSpacing(3);

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
        
        TextField textField = new TextField();
        textField.getStyleClass().add("field");
        textField.setPrefWidth(350);
        textField.setPrefHeight(30);
        textField.setManaged(false);
        textField.setVisible(false);
        
        CheckBox showPasswordCheckBox = new CheckBox("Show?");
        showPasswordCheckBox.setId("showPass");

        textField.managedProperty().bind(showPasswordCheckBox.selectedProperty());
        textField.visibleProperty().bind(showPasswordCheckBox.selectedProperty());
        passwordTextField.managedProperty().bind(showPasswordCheckBox.selectedProperty().not());
        passwordTextField.visibleProperty().bind(showPasswordCheckBox.selectedProperty().not());

        textField.textProperty().bindBidirectional(passwordTextField.textProperty());

        passwordField.getChildren().addAll(passwordLabel, passwordTextField, textField, showPasswordCheckBox);
        passwordField.setSpacing(20);

        formContainer.getChildren().addAll(appField, usernameField, passwordField);
        formContainer.setSpacing(50);

        Label line = new Label("                                                               ");
        Button updateButton = new Button("Update");
        updateButton.setId("updateButton");

        updateButton.setOnAction(event -> {
            String app = appComboBox.getValue();
            String username = usernameTextField.getText();
            String newPasswordValue = passwordTextField.getText();

            if (app != null && !app.isEmpty() && username != null && !username.isEmpty() && newPasswordValue != null && !newPasswordValue.isEmpty()) {
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Password Verification");
                dialog.setHeaderText("Please enter your password to proceed with updating");

                ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        
                PasswordField passwordFieldUser = new PasswordField();
                passwordFieldUser.setPromptText("Password");
        
                VBox content = new VBox();
                content.setSpacing(10);
                content.getChildren().addAll(passwordFieldUser);
                dialog.getDialogPane().setContent(content);
                dialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles/update.css").toExternalForm());
        
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == okButtonType) {
                        return passwordFieldUser.getText();
                    }
                    return null;
                });

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(currentPassword -> {
                    ContactController contactController = new ContactController();
                    try {
                        boolean passwordCorrect = contactController.verifyPassword(userId, currentPassword);
                        if (passwordCorrect) {
                            boolean userExistsForApp = contactController.checkUserExistsForApp(username, app, userId);
                            if (userExistsForApp) {
                                contactController.updateUserPasswordForApp(username, newPasswordValue, app, userId);
                                showAlert(Alert.AlertType.INFORMATION, "Success", "Password updated successfully.");
                            } else {
                                showAlert(Alert.AlertType.ERROR, "Error", "User or app not found.");
                            }
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Error", "Incorrect current password. Please try again.");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while accessing the database.");
                    }
                });

            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            }
        });

        HBox buttonContainer = new HBox(line, updateButton);

        formLayout.getChildren().addAll(titleContainer, formContainer, buttonContainer);
        formLayout.setSpacing(50);

        mainLayout.setLeft(menu);
        mainLayout.setCenter(formLayout);

        Scene scene = new Scene(mainLayout, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/styles/update.css").toExternalForm());
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
        dialogPane.getStylesheets().add(getClass().getResource("/styles/update.css").toExternalForm());
        alert.showAndWait();
    }
}

