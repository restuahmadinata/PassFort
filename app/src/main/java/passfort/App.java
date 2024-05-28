package passfort;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        SignScene initialScene = new SignScene(primaryStage);
        initialScene.show();
   }

   public static void main(String[] args) {
      launch(args);
   }
}
