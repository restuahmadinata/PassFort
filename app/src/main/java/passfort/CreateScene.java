package passfort;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import passfort.com.example.controller.ContactController;

import java.sql.SQLException;

import org.sqlite.SQLiteException;

public class CreateScene {

    private Stage primaryStage;
    private int userId;

    public CreateScene(Stage primaryStage, int userId) {
        this.primaryStage = primaryStage;
        this.userId = userId;
    }

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
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        Button deletePassword = new Button("→ Delete Password");
        deletePassword.setOnAction(v -> {
            DeleteScene deleteScene = new DeleteScene(primaryStage, userId);
            try {
                deleteScene.show();
            } catch (SQLiteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        Button generatePassword = new Button("→ Generate Password");
        generatePassword.setOnAction(v -> {
            GenerateScene generateScene = new GenerateScene(primaryStage, userId);
            try {
                generateScene.show();
            } catch (SQLiteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        Button passwordDatabase = new Button("→ Password database");
        passwordDatabase.setOnAction(v -> {
            DatabaseScene databaseScene = new DatabaseScene(primaryStage, userId);
            try {
                databaseScene.show();
            } catch (SQLiteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        Button aboutUs = new Button("ABOUT US");
        aboutUs.setOnAction(v -> {
            AboutScene aboutScene = new AboutScene(primaryStage, userId);
            try {
                aboutScene.show();
            } catch (SQLiteException e) {
                // TODO Auto-generated catch block
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
        aboutUs.getStyleClass().add("menuButton");
        exit.getStyleClass().add("menuButton");

        menu.getChildren().addAll(menuTitle, newPassword, updatePassword, deletePassword, generatePassword, passwordDatabase, aboutUs, exit);

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
        appComboBox.setId("appBox");
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
            
            ContactController contactController = new ContactController();
            try {
                // Check if the user already exists
                boolean userExists = contactController.checkUserExistsForApp(username, apps, userId);
                
                if (userExists) {
                    // Show error message if user already exists
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("User Already Exists");
                    alert.setContentText("The user already exists in the AppDatabase.");
                    alert.showAndWait();
                } else {
                    // Insert user data to the selected app database
                    contactController.insertUserToAppDatabase(userId, username, password, apps);

                    // Show success message
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("User Added");
                    alert.setContentText("The user has been added successfully to AppDatabase.");
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                // Handle error if insertion fails
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Database Error");
                alert.setContentText("Failed to add user to AppDatabase. Please try again later.");
                alert.showAndWait();
            }
        });
        
    }
}
