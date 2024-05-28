package passfort;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class DatabaseScene {
    private Stage primaryStage;

    public DatabaseScene(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void show() {
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
            CreateScene createScene = new CreateScene(primaryStage);
            createScene.show();
        });

        Button updatePassword = new Button("→ Update Password");
        updatePassword.setOnAction(v -> {
            UpdateScene updateScene = new UpdateScene(primaryStage);
            updateScene.show();
        });

        Button deletePassword = new Button("→ Delete Password");
        deletePassword.setOnAction(v -> {
            DeleteScene deleteScene = new DeleteScene(primaryStage);
            deleteScene.show();
        });

        Button generatePassword = new Button("→ Generate Password");
        generatePassword.setOnAction(v -> {
            GenerateScene generateScene = new GenerateScene(primaryStage);
            generateScene.show();
        });

        Button passwordDatabase = new Button("→ Password database");
        passwordDatabase.setOnAction(v -> {
            DatabaseScene databaseScene = new DatabaseScene(primaryStage);
            databaseScene.show();
        });

        Button aboutUs = new Button("ABOUT US");
        aboutUs.setOnAction(v -> {
            AboutScene aboutScene = new AboutScene(primaryStage);
            aboutScene.show();
        });

        Button exit = new Button("EXIT");
        exit.setOnAction(v -> Platform.exit());

        newPassword.getStyleClass().add("menuButton");
        updatePassword.getStyleClass().add("menuButton");
        deletePassword.getStyleClass().add("menuButton");
        generatePassword.getStyleClass().add("menuButton");
        passwordDatabase.setId("databaseMenu");
        aboutUs.getStyleClass().add("menuButton");
        exit.getStyleClass().add("menuButton");

        // Add items to the menu
        menu.getChildren().addAll(menuTitle, newPassword, updatePassword, deletePassword, generatePassword, passwordDatabase, aboutUs, exit);

  
        // Create the form layout
        VBox formLayout = new VBox();
        formLayout.setId("form");

        // Form title
        VBox titleContainer = new VBox();
        Label formTitle = new Label("Password Database");
        formTitle.setId("formTitle");

        Label formSubtitle = new Label("Here we collect every single account and password you’ve created!\nStill on development tho :'v");
        formSubtitle.setId("formSubtitle");
        titleContainer.getChildren().addAll(formTitle, formSubtitle);
        titleContainer.setSpacing(3);

        // Add the title and table view to the form layout
        formLayout.getChildren().addAll(titleContainer);
        formLayout.setSpacing(20);

        // Add the menu and form layout to the main layout
        mainLayout.setLeft(menu);
        mainLayout.setCenter(formLayout);

        // Create the scene and set it to the stage
        Scene scene = new Scene(mainLayout, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/styles/database.css").toExternalForm());
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}