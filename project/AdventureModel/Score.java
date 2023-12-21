package AdventureModel;

import Entity.Player;

/**
 * This class contains the information about the score in the Adventure Game.
 * Each AdventureGame has a Score attribute that represents the current Score of that game
 * A score attribute stores the information about the player of the current game
 * and keeps track of the increase in score due to any score related events
 */
public class Score {

    public Player player;
    public AdventureGame adventureGame;
    public String name;
    private int score;


    /**
     * Score Constructor
     * @param a : An AdventureGame
     */
    public  Score(AdventureGame a) {
        this.adventureGame = a;
        this.player = a.player;
        this.name = a.player.getName();
        this.score = 0;
    }

    /**
     * Updates the score whenever there is a score related event
     * @param x: increments the score by 1 when the game is going on and
     *           increments by 100 when the player hits the trolls and
     *           decreases the score by 100 when the troll hits the player
     */
    public void updateScore(int x) {
        this.score +=  x;
    }

    /**
     * Returns the current score of the game
     * @return : the current score of the game
     */
    public int getScore() {
        return this.score;
    }

}

