package AdventureModel;


import java.io.*;
import java.util.*;

/**
 * The Model part of the MVC pattern.
 * Each AdventureGame has a ScoreModel  attribute that stores the data
 * in the score.csv file and gives the information to the ScoreController Class when it is asked to
 */

public class ScoreModel {
    public HashMap<String, String> score_list;
    private AdventureGame game;
    private String filename;

    /**
     * ScoreModel constructor
     * @param a
     * @throws IOException
     */
    public ScoreModel(AdventureGame a) throws IOException {
        this.score_list = new HashMap<>();
        this.game = a;
        createFile();
    }

    /**
     * Creates the score.csv file in Games/--the name of the game--/ directory
     * @throws IOException
     */
    private void createFile() throws IOException {
        String s1 = this.game.getDirectoryName() + File.separator + "score.csv";
        this.filename = s1;
        File scorefile = new File(s1);
        if (!scorefile.exists()) {
            scorefile.createNewFile();
        }
    }

    /**
     * Updates the score.csv file when the ScoreController notifies of the end of a game
     */
    public void updateData() {
        String data = this.game.player.getName() + "," + this.game.gamescore.getScore() + "\n";
        String s1 = this.game.getDirectoryName() + File.separator + "score.csv";
        try (FileWriter fileWriter = new FileWriter(s1, true)) {
            fileWriter.write(data);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the CSV file.");
        }
    }

    /**
     * Returns the list of the scores to the ScoreController that is later passed on to the
     * ScoreView class to populate the TableView
     * @return An arraylist containing the scores in the csv file
     */
    public List<String> getscoreList() {
        List<String> ret = new ArrayList<>();
        String s1 = this.game.getDirectoryName() + File.separator +"score.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(s1))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                ret.add(values[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
    /**
     * Returns the list of the names to the ScoreController that is later passed on to the
     * ScoreView class to populate the TableView
     * @return An arraylist containing the names in the csv file
     */
    public List<String> getNameList() {
        List<String> ret = new ArrayList<>();
        String s1 = this.game.getDirectoryName() + File.separator + "score.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(s1))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                ret.add(values[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}


