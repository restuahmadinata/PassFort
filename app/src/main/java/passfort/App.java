package passfort;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {
   @Override
   public void start(Stage primaryStage) {
      SignScene initialScene = new SignScene(primaryStage, 0);
      initialScene.show();

      Image icon = new Image("/images/icon.png");
      primaryStage.getIcons().add(icon);
      primaryStage.setTitle("PassFort");
   }

   public static void main(String[] args) {
      launch(args);
   }
}
