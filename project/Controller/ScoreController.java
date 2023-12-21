package Controller;
import views.AdventureGameView;
import views.ScoreView;
import AdventureModel.AdventureGame;
import AdventureModel.ScoreModel;

/**
 * The controller of MVC pattern
 * Maintains communication between the ScoreModel and the ScoreView Class
 */
public class ScoreController {

    public ScoreView scoreview;
    public ScoreModel scoremodel;

    /**
     * Score Controller Constructor
     * @param g: an AdventureGame containing the ScoreModel
     * @param view : the AdventureGameView that contains the ScoreView
     */

    public ScoreController(AdventureGame g, AdventureGameView view){
        this.scoremodel = g.scoremodel;
        this.scoreview = view.scoreView;
    }

    /**
     * Notifies the ScoreModel when the ending of a game
     */
    public void addScore(){
        this.scoremodel.updateData();
    }

    /**
     * Notifies the ScoreView when the user wants to view the ScoreBoard
     * Collects the list of scores and names and gives it to the ScoreView Class to display
     */
    public void showScore(){
        this.scoreview.populateTableview(this.scoremodel.getscoreList(), this.scoremodel.getNameList());
    }
}
