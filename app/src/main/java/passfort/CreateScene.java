package passfort;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.SQLException;
import org.sqlite.SQLiteException;
import passfort.com.example.controller.ContactController;

public class CreateScene {

    private Stage primaryStage;
    private int userId;

    public CreateScene(Stage primaryStage, int userId) {
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

        newPassword.setId("createMenu");
        updatePassword.getStyleClass().add("menuButton");
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
        Label formTitle = new Label("CREATE");
        formTitle.setId("formTitle");

        Label formSubtitle = new Label("Add new account and password into the database");
        formSubtitle.setId("formSubtitle");
        titleContainer.getChildren().addAll(formTitle, formSubtitle);
        titleContainer.setSpacing(3);

        VBox formContainer = new VBox();

        HBox appField = new HBox();
        Label appLabel = new Label("apps\t\t-→");
        appLabel.getStyleClass().add("fieldLabel");

        ComboBox<String> appComboBox = new ComboBox<>();
        appComboBox.setPromptText("Select app");
        appComboBox.setPrefWidth(460);
        appComboBox.setPrefHeight(20);
        appComboBox.getItems().addAll(
                "Google", "Facebook", "Instagram", "Paypal", "Gopay",
                "DANA", "OVO", "Gojek", "Grab", "Steam"
        );
        appField.getChildren().addAll(appLabel, appComboBox);
        appField.setSpacing(20);

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
        Button addButton = new Button("Add");
        addButton.setId("addButton");

        HBox buttonContainer = new HBox(line, addButton);

        formLayout.getChildren().addAll(titleContainer, formContainer, buttonContainer);
        formLayout.setSpacing(50);

        mainLayout.setLeft(menu);
        mainLayout.setCenter(formLayout);

        Scene scene = new Scene(mainLayout, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/styles/create.css").toExternalForm());
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

        addButton.setOnAction(event -> {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            String apps = appComboBox.getValue();
            
            if (username.isEmpty() || password.isEmpty() || apps == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
                return;
            }
            
            ContactController contactController = new ContactController();
            try {
                boolean userExists = contactController.checkUserExistsForApp(username, apps, userId);
                
                if (userExists) {
                    showAlert(Alert.AlertType.ERROR, "Error", "User Already Exists. The user already exists in the AppDatabase.");
                } else {
                    contactController.insertUserToAppDatabase(userId, username, password, apps);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "User Added. The user has been added successfully to AppDatabase.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Database Error. Failed to add user to AppDatabase.");
            }
        });
        
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
    
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/styles/create.css").toExternalForm());
        alert.showAndWait();
    }
}
