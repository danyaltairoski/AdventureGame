package views;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * The View part of MVC pattern
 * Class ScoreView.
 * Shows the previous scores of an adventure game.
 */
public class ScoreView {
    private AdventureGameView adventureGameView;
    private Button closeWindowButton;
    private TableView<List<StringProperty>> tableView = new TableView<>();

    /**
     *
     * Score View Constructor
     * __________________________
     * Initializes attributes
     * @param adventureGameView : The AdventureGameView for the main game
     *
     */
    public ScoreView(AdventureGameView adventureGameView)
    {
        this.adventureGameView = adventureGameView;

    }

    /**
     * Displays the scores
     */
    public void viewScore()
    {
        final Stage dialog = new Stage(); //dialogue box
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(adventureGameView.stage);

        VBox dialogVbox = new VBox(tableView);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.setStyle("-fx-background-color: #121212;");


        closeWindowButton = new Button("Close Window");
        closeWindowButton.setId("closeWindowButton"); // DO NOT MODIFY ID
        closeWindowButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        closeWindowButton.setPrefSize(200, 50);
        closeWindowButton.setFont(new Font(16));
        closeWindowButton.setOnAction(e -> dialog.close());
        AdventureGameView.makeButtonAccessible(closeWindowButton, "close window", "This is a button to close the ScoreBoard window", "Use this button to close the ScoreBoard window.");
        Scene dialogScene = new Scene(dialogVbox, 600, 400);
        dialog.setScene(dialogScene);
        dialog.setResizable(false);
        dialog.show();
    }

    /**
     * Get Scores and names to display in the on screen ListView
     * Populate the tableview attribute with the names and scores
     *
     * @param scorelist : An arraylist containing all the scores from score.csv file
     * @param namelist : An arraylist containing all the names from score.csv file
     */
    public void populateTableview(List<String> scorelist, List<String> namelist){
        TableColumn<List<StringProperty>, String> nameColumn = new TableColumn<>("Name");
        TableColumn<List<StringProperty>, String> scoreColumn = new TableColumn<>("Score");

        nameColumn.setCellValueFactory(cellData -> cellData.getValue().get(0));
        scoreColumn.setCellValueFactory(cellData -> cellData.getValue().get(1));


        tableView.getColumns().clear();
        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(scoreColumn);

        nameColumn.setPrefWidth(300); // Adjust the width as needed
        scoreColumn.setPrefWidth(300); // Adjust the width as needed
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        tableView.setStyle("-fx-text-fill: white;-fx-background-color: black");

        ObservableList<List<StringProperty>> data = FXCollections.observableArrayList();
        for (int i = 0; i < scorelist.size(); i++) {
            List<StringProperty> row = new ArrayList<>();
            row.add(new SimpleStringProperty(namelist.get(i)));
            row.add(new SimpleStringProperty(scorelist.get(i)));
            data.add(row);
        }
        tableView.setItems(data);
        viewScore();
    }

}

