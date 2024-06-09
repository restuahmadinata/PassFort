package passfort;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import passfort.com.example.controller.ContactController;
import org.sqlite.SQLiteException;
import java.util.ArrayList;
import java.util.List;

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

    public void show() {
        ContactController contactController = new ContactController();
        String fullName = contactController.getUserFullName(userId);

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
            userScene.show();
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

        menu.getChildren().addAll(menuTitle, newPassword, updatePassword, deletePassword, generatePassword, passwordDatabase, userProfile, aboutUs, exit);

        VBox infoLayout = new VBox();
        infoLayout.setId("infoLayout");
        infoLayout.setPadding(new Insets(10));

        VBox titleContainer = new VBox();

        Label infoTitle = new Label("USER PROFILE");
        infoTitle.setId("infoTitle");

        Label infoSubtitle = new Label("This is you.");
        infoSubtitle.setId("infoSubtitle");

        titleContainer.getChildren().addAll(infoTitle, infoSubtitle);
        titleContainer.setSpacing(3);

        Image defaultImage = new Image(getClass().getResourceAsStream("/images/default.jpg"));
        ImageView imageView = new ImageView(defaultImage);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        setCircularImage(imageView, 75);
        HBox imageContainer = new HBox(imageView);
        imageContainer.setAlignment(Pos.CENTER);

        Label greetingLabel = new Label("Hello, " + fullName + "!");
        greetingLabel.setId("greetingLabel");

        Button changePasswordButton = new Button("CHANGE PASSWORD");
        changePasswordButton.getStyleClass().add("userButton");

        changePasswordButton.setOnAction(e -> {
            PasswordField oldPasswordField = new PasswordField();
            oldPasswordField.setPromptText("Old Password");

            PasswordField newPasswordField = new PasswordField();
            newPasswordField.setPromptText("New Password");

            PasswordField confirmPasswordField = new PasswordField();
            confirmPasswordField.setPromptText("Confirm New Password");

            Tooltip newPasswordTooltip = new Tooltip(getPasswordRequirements());
            newPasswordField.setTooltip(newPasswordTooltip);
            confirmPasswordField.setTooltip(newPasswordTooltip);
            newPasswordTooltip.setShowDelay(Duration.seconds(0.3));
            newPasswordTooltip.setShowDuration(Duration.seconds(60));

            newPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
                updatePasswordTooltip(newPasswordTooltip, newValue);
            });

            confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
                updatePasswordTooltip(newPasswordTooltip, newValue);
            });

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setHeaderText("Enter Your Old Password");
            dialog.getDialogPane().setContent(oldPasswordField);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            dialog.setTitle("Change Password");
            dialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles/database.css").toExternalForm());

            dialog.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK) {
                    String oldPassword = oldPasswordField.getText();
                    boolean isOldPasswordCorrect = contactController.verifyPassword(userId, oldPassword);
                    if (isOldPasswordCorrect) {
                        Dialog<ButtonType> newPasswordDialog = new Dialog<>();
                        newPasswordDialog.setHeaderText("Enter Your New Password");
                        newPasswordDialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles/database.css").toExternalForm());
                        GridPane gridPane = new GridPane();
                        gridPane.setHgap(10);
                        gridPane.setVgap(10);
                        gridPane.addRow(0, new Label("New Password: "), newPasswordField);
                        gridPane.addRow(1, new Label("Confirm Pass: "), confirmPasswordField);
                        newPasswordDialog.getDialogPane().setContent(gridPane);
                        newPasswordDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
                        newPasswordDialog.setTitle("Change Password");
                        newPasswordDialog.showAndWait().ifPresent(newResult -> {
                            if (newResult == ButtonType.OK) {
                                String newPasswordText = newPasswordField.getText();
                                String confirmPassword = confirmPasswordField.getText();
                                if (!isValidPassword(newPasswordText)) {
                                    showAlert(Alert.AlertType.ERROR, "Error", "Password does not meet the requirements!");
                                } else if (!newPasswordText.equals(confirmPassword)) {
                                    showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match!");
                                } else {
                                    contactController.updateUserPassword(userId, newPasswordText);
                                    showAlert(Alert.AlertType.INFORMATION, "Success", "Password successfully changed!");
                                }
                            }
                        });
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Incorrect old password. Please try again.");
                    }
                }
            });
        });

        VBox userContainer = new VBox();
        userContainer.getChildren().addAll(imageContainer, greetingLabel, changePasswordButton);
        userContainer.setSpacing(30);
        userContainer.setAlignment(Pos.CENTER);

        infoLayout.getChildren().addAll(titleContainer, userContainer);
        infoLayout.setSpacing(20);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(infoLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        mainLayout.setLeft(menu);
        mainLayout.setCenter(scrollPane);

        Scene scene = new Scene(mainLayout, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/styles/user.css").toExternalForm());
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

    private String getPasswordRequirements() {
        return "-> Password must be at least 8 characters long\n" +
               "-> Contain at least one uppercase letter\n" +
               "-> One lowercase letter\n" +
               "-> One digit\n" +
               "-> One special character (@, $, !, %, *, ?, &)";
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

    private boolean isValidPassword(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[@$!%*?&].*");
    }
}

