package passfort;


import java.io.InputStream;

import org.sqlite.SQLiteException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class AboutScene {
    private Stage primaryStage;
    private int userId;

    public AboutScene(Stage primaryStage, int userId) {
        this.primaryStage = primaryStage;
        this.userId = userId;
    }

    private void setCircularImage(ImageView imageView, double radius) {
        Circle clip = new Circle(radius, radius, radius);
        imageView.setClip(clip);
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
        updatePassword.getStyleClass().add("menuButton");
        deletePassword.getStyleClass().add("menuButton");
        generatePassword.getStyleClass().add("menuButton");
        passwordDatabase.getStyleClass().add("menuButton");
        userProfile.getStyleClass().add("menuButton");
        aboutUs.setId("aboutMenu");
        exit.getStyleClass().add("menuButton");

        // Add items to the menu
        menu.getChildren().addAll(menuTitle, newPassword, updatePassword, deletePassword, generatePassword, passwordDatabase, userProfile, aboutUs, exit);


        // InfoLayout
        VBox infoLayout = new VBox();
        infoLayout.setId("infoLayout");
        infoLayout.setPadding(new Insets(10));

        // Form title
        VBox titleContainer = new VBox();

        Label infoTitle = new Label("ABOUT US");
        infoTitle.setId("infoTitle");

        Label infoSubtitle = new Label("Wanna know who create this program?");
        infoSubtitle.setId("infoSubtitle");

        titleContainer.getChildren().addAll(infoTitle, infoSubtitle);
        titleContainer.setSpacing(3);

        // Add fields to the form layout
        infoLayout.getChildren().addAll(titleContainer);

        // Create a ScrollPane to contain user information
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setId("scrollPane");
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);

        VBox userInfoContainer = new VBox();
        userInfoContainer.setSpacing(10);
        userInfoContainer.setId("userInfoContainer");

        String[] usernames = {"Radinata", "Alief Kobokan", "Zakiya", "Kelvin L"};
        String[] descriptions = {
            "Suaminya Mirai Kuriyama.\nMengisi waktu luang dengan nonton anime atau denger musik.",
            "Makhluk ini bernafas dengan Valorant",
            "Msuk sisfo auto pilot dr ortu.\nsuka makan eskrim.\nmotto hidup \"tidur itu optional\"",
            "Our mentor."
        };
        String[] imagePaths = {
            "images/radinata.jpg",
            "images/alif.jpg",
            "images/zakiyah.jpg",
            "images/kelvin_l.png"
        };

        for (int i = 0; i < 4; i++) {
            VBox userContainer = new VBox();
            userContainer.setSpacing(10);

            // User image
            ImageView imageView = new ImageView();
            try {
                InputStream stream = getClass().getClassLoader().getResourceAsStream(imagePaths[i]);
                if (stream != null) {
                    imageView.setImage(new Image(stream));
                    imageView.setFitWidth(150);
                    imageView.setFitHeight(150);
                    setCircularImage(imageView, 75);
                } else {
                    System.err.println("Failed to load image: " + imagePaths[i]);
                }
            } catch (Exception e) {
                System.err.println("Error loading image: " + e.getMessage());
                e.printStackTrace();
            }

            // Username
            Label usernameLabel = new Label(usernames[i]);
            usernameLabel.setId("usernameLabel");

            // Description
            Label descriptionLabel = new Label(descriptions[i]);
            descriptionLabel.setId("descriptionLabel");

            Label line = new Label("-----------------------------------------------------------------------------------------------------");
            line.setId("line");

            // Add items to the user container
            userContainer.getChildren().addAll(imageView, usernameLabel, descriptionLabel, line);
            userContainer.setAlignment(Pos.CENTER);
            userContainer.setId("userContainer");

            // Add the user container to the main container
            userInfoContainer.getChildren().add(userContainer);
        }

        // Add the user information container to the scroll pane
        scrollPane.setContent(userInfoContainer);

        // Add the scroll pane to the form layout
        infoLayout.getChildren().add(scrollPane);

        // Add the menu and form layout to the main layout
        mainLayout.setLeft(menu);
        mainLayout.setCenter(infoLayout);

        // Create the scene and set it to the stage
        Scene scene = new Scene(mainLayout, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/styles/about.css").toExternalForm());
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}