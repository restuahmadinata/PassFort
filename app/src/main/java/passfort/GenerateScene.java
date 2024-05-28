package passfort;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.security.SecureRandom;

public class GenerateScene {
    private Stage primaryStage;

    public GenerateScene(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void show() {
        // Create the main layout
        BorderPane mainLayout = new BorderPane();

        // Create the menu
        VBox menu = new VBox();
        menu.setSpacing(10);
        menu.setPadding(new Insets(10));
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
        generatePassword.setId("generateMenu");
        passwordDatabase.getStyleClass().add("menuButton");
        aboutUs.getStyleClass().add("menuButton");
        exit.getStyleClass().add("menuButton");

        // Add items to the menu
        menu.getChildren().addAll(menuTitle, newPassword, updatePassword, deletePassword, generatePassword, passwordDatabase, aboutUs, exit);

        // Create the form layout
        VBox formLayout = new VBox();
        formLayout.setId("form");
        formLayout.setPadding(new Insets(10));

        // Form title
        VBox titleContainer = new VBox();
        Label formTitle = new Label("Generate");
        formTitle.setId("formTitle");

        Label formSubtitle = new Label("Too lazy to think about your new password? We got you covered!");
        formSubtitle.setId("formSubtitle");

        titleContainer.getChildren().addAll(formTitle, formSubtitle);
        titleContainer.setSpacing(3);

        // Add input fields to the form layout
        formLayout.setSpacing(20);

        VBox charTitleContainer = new VBox();
        Label charLabel = new Label("How many characters?");
        charLabel.getStyleClass().add("optionLabel");
        Label charPrompt = new Label(">> Insert number between 8-15");
        charPrompt.setId("charPrompt");
        charTitleContainer.getChildren().addAll(charLabel, charPrompt);
        charTitleContainer.setSpacing(15);

        TextField charField = new TextField();
        charField.setPrefWidth(75);
        charField.setPrefHeight(65);
        charField.setId("charField");

        HBox charContainer = new HBox();
        charContainer.getChildren().addAll(charTitleContainer, charField);
        charContainer.setSpacing(20);

        // OPTIONS
        VBox optionsContainer = new VBox();
        Label optionsLabel = new Label("Consist of?");
        optionsLabel.getStyleClass().add("optionLabel");

        HBox optionsBox = new HBox();
        CheckBox numberBox = new CheckBox(" >> Number");
        CheckBox symbolBox = new CheckBox(">> Symbol");
        CheckBox alphabetBox = new CheckBox(">> Alphabet");
        numberBox.getStyleClass().add("optionsBox");
        symbolBox.getStyleClass().add("optionsBox");
        alphabetBox.getStyleClass().add("optionsBox");

        optionsBox.getChildren().addAll(numberBox, symbolBox, alphabetBox);
        optionsBox.setSpacing(10);

        optionsContainer.getChildren().addAll(optionsLabel, optionsBox);
        optionsContainer.setSpacing(5);

        HBox generateBox = new HBox();
        TextField outputField = new TextField();
        outputField.setPrefWidth(450);
        outputField.setPrefHeight(70);
        outputField.setId("outputField");
        Button generateButton = new Button("⟳");
        generateButton.setId("generateButton");
        Button copyButton = new Button("🗐");
        copyButton.setId("copyButton");

        generateButton.setOnAction(e -> {
            String lengthText = charField.getText();
            if (lengthText.matches("\\d+")) {
                int length = Integer.parseInt(lengthText);
                if (length >= 8 && length <= 15) {
                    boolean useNumbers = numberBox.isSelected();
                    boolean useSymbols = symbolBox.isSelected();
                    boolean useAlphabets = alphabetBox.isSelected();
                    if (useNumbers || useSymbols || useAlphabets) {
                        String password = generatePassword(length, useNumbers, useSymbols, useAlphabets);
                        outputField.setText(password);
                        copyButton.setText("🗐");
                    } else {
                        outputField.setText("Select at least one option.");
                    }
                } else {
                    outputField.setText("Enter a number between 8-15.");
                }
            } else {
                outputField.setText("Enter a valid number.");
            }
        });

        copyButton.setOnAction(v -> {
            String text = outputField.getText();
            if (text != null && !text.isEmpty()) {
                // Copy text to clipboard
                final Clipboard clipboard = Clipboard.getSystemClipboard();
                final ClipboardContent content = new ClipboardContent();
                content.putString(text);
                clipboard.setContent(content);

                // Change button text to "done"
                copyButton.setText("🗹");
            }
        });

        generateBox.getChildren().addAll(outputField, generateButton, copyButton);
        generateBox.setSpacing(10);

        formLayout.getChildren().addAll(titleContainer, charContainer, optionsContainer, generateBox);
        formLayout.setSpacing(50);

        // Add the menu and form layout to the main layout
        mainLayout.setLeft(menu);
        mainLayout.setCenter(formLayout);

        // Create the scene and set it to the stage
        Scene scene = new Scene(mainLayout, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/styles/generate.css").toExternalForm());
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String generatePassword(int length, boolean useNumbers, boolean useSymbols, boolean useAlphabets) {
        String numbers = "0123456789";
        String symbols = "!@#$%^&*()-_=+<>?";
        String alphabets = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        
        StringBuilder characterSet = new StringBuilder();
        if (useNumbers) {
            characterSet.append(numbers);
        }
        if (useSymbols) {
            characterSet.append(symbols);
        }
        if (useAlphabets) {
            characterSet.append(alphabets);
        }

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characterSet.length());
            password.append(characterSet.charAt(index));
        }

        return password.toString();
    }
}
