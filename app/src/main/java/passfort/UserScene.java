package passfort;

import java.io.File;

import org.sqlite.SQLiteException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import passfort.com.example.controller.*;

public class UserScene {
    private Stage primaryStage;
    private int userId;
    

    public UserScene(Stage primaryStage, int userId) {
        this.primaryStage = primaryStage;
        this.userId = userId;
    }

    private void setCircularImage(ImageView imageView, double radius) {
        Circle clip = new Circle(radius, radius, radius);
        imageView.setClip(clip);
    }

    public void show() throws SQLiteException {
        ContactController contactController = new ContactController();
        String fullName = contactController.getUserFullName(userId);

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
            createScene.show();
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
            databaseScene.show();
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

        Button userProfile = new Button("PROFILE");
        userProfile.setOnAction(v -> {
            UserScene userScene = new UserScene(primaryStage, userId);
            try {
                userScene.show();
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
        userProfile.setId("userMenu");
        aboutUs.getStyleClass().add("menuButton");
        exit.getStyleClass().add("menuButton");

        // Add items to the menu
        menu.getChildren().addAll(menuTitle, newPassword, updatePassword, deletePassword, generatePassword, passwordDatabase, userProfile, aboutUs, exit);


        // InfoLayout
        VBox infoLayout = new VBox();
        infoLayout.setId("infoLayout");
        infoLayout.setPadding(new Insets(10));

        // Form title
        VBox titleContainer = new VBox();

        Label infoTitle = new Label("USER PROFILE");
        infoTitle.setId("infoTitle");

        Label infoSubtitle = new Label("This is you");
        infoSubtitle.setId("infoSubtitle");

        titleContainer.getChildren().addAll(infoTitle, infoSubtitle);
        titleContainer.setSpacing(3);

        // Add fields to the form layout
        Image defaultImage = new Image(getClass().getResourceAsStream("/images/default.jpg"));
        ImageView imageView = new ImageView(defaultImage);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        setCircularImage(imageView, 75);
        HBox imageContainer = new HBox(imageView);
        imageContainer.setAlignment(Pos.CENTER);

        Label greetingLabel = new Label("Hello, " + fullName + "!");
        greetingLabel.setId("greetingLabel");

        Button changePhotoButton = new Button("Change Photo");
        changePhotoButton.setId("changePhotoButton");
        changePhotoButton.setOnAction(e -> {
            // Membuat file chooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Photo");
            // Menambahkan filter untuk jenis file gambar
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
            );

            // Menampilkan dialog pemilih file
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            // Jika pengguna memilih file
            if (selectedFile != null) {
                // Membuat gambar dari file yang dipilih
                Image newImage = new Image(selectedFile.toURI().toString());
                // Mengatur gambar baru ke ImageView
                imageView.setImage(newImage);
            }
        });

        Button changePasswordButton = new Button("Change Password");
        changePasswordButton.setId("changePasswordButton");
        changePasswordButton.setOnAction(e -> {
            // Menampilkan prompt untuk memasukkan password lama
            PasswordField oldPasswordField = new PasswordField();
            oldPasswordField.setPromptText("Old Password");
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setHeaderText("Enter Your Old Password");
            dialog.getDialogPane().setContent(oldPasswordField);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            dialog.setTitle("Change Password");
            dialog.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK) {
                    String oldPassword = oldPasswordField.getText();
                    // Verifikasi password lama
                    boolean isOldPasswordCorrect = contactController.verifyPassword(userId, oldPassword);
                    if (isOldPasswordCorrect) {
                        // Jika password lama benar, menampilkan prompt untuk memasukkan password baru
                        PasswordField newPasswordField = new PasswordField();
                        newPasswordField.setPromptText("New Password");
                        Dialog<ButtonType> newPasswordDialog = new Dialog<>();
                        newPasswordDialog.setHeaderText("Enter Your New Password");
                        newPasswordDialog.getDialogPane().setContent(newPasswordField);
                        newPasswordDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
                        newPasswordDialog.setTitle("Change Password");
                        newPasswordDialog.showAndWait().ifPresent(newResult -> {
                            if (newResult == ButtonType.OK) {
                                String newPwd = newPasswordField.getText(); // Menggunakan nama variabel baru
                                contactController.updateUserPassword(userId, newPwd);
                                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                                successAlert.setHeaderText(null);
                                successAlert.setContentText("Password successfully changed!");
                                successAlert.showAndWait();
                            }
                        });
                    } else {
                        // Jika password lama salah, tampilkan pesan kesalahan
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("Incorrect old password. Please try again.");
                        errorAlert.showAndWait();
                    }
                }
            });
        });

        VBox userContainer = new VBox();
        userContainer.getChildren().addAll(imageContainer, greetingLabel, changePhotoButton, changePasswordButton);
        userContainer.setSpacing(30);
        userContainer.setAlignment(Pos.CENTER);

        infoLayout.getChildren().addAll(titleContainer, userContainer);
        infoLayout.setSpacing(20);

        // Create a scroll pane for info layout
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(infoLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // Add the menu and scroll pane to the main layout
        mainLayout.setLeft(menu);
        mainLayout.setCenter(scrollPane);

        // Create the scene and set it to the stage
        Scene scene = new Scene(mainLayout, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/styles/user.css").toExternalForm());
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}