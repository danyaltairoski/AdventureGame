import AdventureModel.AdventureGame;
import javafx.application.Application;
import javafx.stage.Stage;
import views.AdventureGameView;
import views.StartPageView;


import java.io.IOException;

/**
 * Class AdventureGameApp.
 */
public class AdventureGameApp extends  Application {

    AdventureGame model1;

    AdventureGame model2;
    AdventureGameView view1;

    AdventureGameView view2;
    StartPageView startPageView;

    public static void main(String[] args) {
        launch(args);
    }

    /*
    * JavaFX is a Framework, and to use it we will have to
    * respect its control flow!  To start the game, we need
    * to call "launch" which will in turn call "start" ...
     */
    @Override
    public void start(Stage primaryStage) throws IOException {

        this.model1 = new AdventureGame("TinyGame"); //change the name of the game if you want to try something bigger!
        this.model2 = new AdventureGame("Tutorial");
        this.view1 = new AdventureGameView(model1, primaryStage);
        this.view2 = new AdventureGameView(model2, primaryStage);

        this.startPageView = new StartPageView(this.view1, this.view2);
    }
}
