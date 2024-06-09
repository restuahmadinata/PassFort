package passfort;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import passfort.com.example.controller.ContactController;

public class AdminThanos {
    private Stage primaryStage;
    private ContactController contactController;
    private int authResult;

    public AdminThanos(Stage primaryStage, int authResult) {
        this.primaryStage = primaryStage;
        this.contactController = new ContactController();
        this.authResult = authResult;
    }

    public void show() {

        VBox loginLayout = new VBox();
        loginLayout.setId("form");

        VBox titleContainer = new VBox();

        Label greeting = new Label("The time is nigh.");
        greeting.setId("greeting");

        Label formTitle = new Label("ADMIN - THANOS MODE");
        formTitle.setId("formTitle");

        titleContainer.getChildren().addAll(greeting, formTitle);
        titleContainer.setSpacing(3);
        titleContainer.setAlignment(Pos.CENTER);

        Label fieldLabel = new Label("Unalive a user!");
        fieldLabel.setId("fieldLabel");

        TextField userField = new TextField();
        userField.setId("userField");
        userField.setMaxWidth(300);

        Button delete = new Button("BITES!");
        delete.setId("delete");

        delete.setOnAction(event -> {
            String username = userField.getText();
            if (username.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Username field must not be empty!");
                return;
            }
        
            String role = contactController.getUserRole(username);
            if ("Admin".equals(role)) {
                showAlert(Alert.AlertType.ERROR, "Deletion Error", "Admin account cannot be deleted.");
                return;
            }
        
            try {
                if (contactController.isUsernameTaken(username)) {
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.setTitle("Confirm Deletion");
                    confirmationAlert.setHeaderText(null);
                    confirmationAlert.setContentText("Are you sure you want to delete the user '" + username + "'?");
                    ButtonType result = confirmationAlert.showAndWait().orElse(ButtonType.CANCEL);
                    if (result == ButtonType.OK) {
                        boolean userDeleted = contactController.deleteUserByUsername(username);
                        if (userDeleted) {
                            showAlert(Alert.AlertType.INFORMATION, "Success", "User '" + username + "' has been deleted.");
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete user '" + username + "'.");
                        }
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "User '" + username + "' does not exist.");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Could not connect to the database. Please try again later.");
            }
        });
        

        VBox fieldButtonContainer = new VBox();
        fieldButtonContainer.getChildren().addAll(fieldLabel, userField, delete);
        fieldButtonContainer.setSpacing(20);
        fieldButtonContainer.setAlignment(Pos.CENTER);

        Button exit = new Button("Admin - Normal Mode");
        exit.setId("exit");
        exit.setOnAction(v -> {
            AdminScene adminScene = new AdminScene(primaryStage, authResult);
            adminScene.show();
        });

        loginLayout.getChildren().addAll(titleContainer, fieldButtonContainer, exit);
        loginLayout.setSpacing(50);
        loginLayout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(loginLayout, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/styles/adminThanos.css").toExternalForm());
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
}
