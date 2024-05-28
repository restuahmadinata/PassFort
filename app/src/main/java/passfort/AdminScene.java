package passfort;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AdminScene {
    private Stage primaryStage;

    public AdminScene(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void show() {

        // Create the form layout
        VBox loginLayout = new VBox();
        loginLayout.setId("form");

        // Form title
        VBox titleContainer = new VBox();

        Label greeting = new Label("PENAMPAKAN SOSOK ADMIN MERUDAPAKSA PINTU!");
        greeting.setId("greeting");

        Label formTitle = new Label("ADMIN ACCESS");
        formTitle.setId("formTitle");

        Label formSubtitle = new Label("Pilih kekuasan anda!");
        formSubtitle.setId("formSubtitle");
        titleContainer.getChildren().addAll(greeting, formTitle, formSubtitle);
        titleContainer.setSpacing(3);
        titleContainer.setAlignment(Pos.CENTER);

        // Add button
        Button delete = new Button("DELETE\nACCOUNT");
        delete.getStyleClass().add("adminButton");

        Button change = new Button("ADMIN\nACCOUNT");
        change.getStyleClass().add("adminButton");

        HBox buttonContainer = new HBox();
        buttonContainer.getChildren().addAll(delete, change);
        buttonContainer.setSpacing(20);
        buttonContainer.setAlignment(Pos.CENTER);

        Button exit = new Button("Jadi kroco");
        exit.setId("exit");
        exit.setOnAction(v -> {
            LoginScene loginScene = new LoginScene(primaryStage);
            loginScene.show();
        });

        // Add fields to the form layout
        loginLayout.getChildren().addAll(titleContainer, buttonContainer, exit);
        loginLayout.setSpacing(50);
        loginLayout.setAlignment(Pos.CENTER);


        // Create the scene and set it to the stage
        Scene scene = new Scene(loginLayout, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/styles/admin.css").toExternalForm());
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
