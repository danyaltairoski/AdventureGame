package views;
import AdventureModel.*;
import Entity.Player;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import AdventureModel.AdventureObject;

import java.util.*;

/**
 * Class Extender.
 *
 * Extends the AdventureGameView class and adds functionality to buttons.
 */
public class Extender {

    private Player player; //current player
    private AdventureGame model; //current AdventureGame
    private GridPane pane; //current gridpane
    private AdventureGameView view; //the AdventureGameView object

    /**
     * Constructor for Extender
     * -----------------------------
     * @param p is the current player in the current AdventureGameView
     * @param m is the current AdventureGame selected
     * @param pane is the current gridpane being used in AdventureGameView
     * @param view is the current AdventureGameView object
     */
    public Extender(Player p, AdventureGame m, GridPane pane, AdventureGameView view){
        this.player = p;
        this.model = m;
        this.pane = pane;
        this.view = view;
    }

    /**
     * getDirections
     * --------------------
     * Fetches all the directions the player can move to other rooms.
     * Finds all items in each direction and whether a troll can be found in a certain direction.
     *
     * @return a hashmap<String, String> that has key values of directions the player can go
     * and values of the items and troll present in that specific direction.
     */
    public HashMap<String, String> getDirections(){
        List<Passage> possible = this.player.getCurrentRoom().getMotionTable().getDirection(); //list of all possible passages in the room
        HashMap<String, String> map = new HashMap(); //map for direction and items

        //Loop over every possible passage and extract directions and trolls
        for(Passage p: possible){
            String direction = p.getDirection(); //direction of the room from current room
            int roomNumber = p.getDestinationRoom(); //get room number of destination room

            //check for only valid directions from the passage table
            if (direction.equals("NORTH") || direction.equals("SOUTH") || direction.equals("EAST")
                    || direction.equals("WEST") || direction.equals("UP") || direction.equals("DOWN")
                    || direction.equals("IN") || direction.equals("OUT")) {
                ArrayList<AdventureObject> roomObjects = model.getRooms().get(roomNumber).objectsInRoom; //arraylist of all objects in that room
                ArrayList<String> objectString = new ArrayList<>(); //arraylist used for value in map

                //loop over every object in a specific direction and add to arraylist for map value
                for (AdventureObject r : roomObjects) {
                    objectString.add(r.getName());
                }
                //check if there is a troll in a certain direction
                if (model.getRooms().get(roomNumber).troll != null){
                    objectString.add("troll");
                }
                //adding the objects and directions into the hashmap
                String listAsString = String.join(", ", objectString);
                //check if the certain direction has no objects and trolls in that room and format line accordingly
                if (listAsString.equals("")) {
                    map.put(direction, "None");
                } else {
                    map.put(direction, listAsString); //put into the map the direction as a key and objects as the value
                }
            }
        }
        return map;
    }

    /**
     * executeSurroundingItems
     * --------------------------
     *  Uses the hashmap of items and directions and shows them on screen to the player.
     *  Plays drinking audio, shows the animation and then shows the directions and items.
     *  After the potion wears off, it would return back to normal gameplay.
     *  Extends AdventureGameView by adding functionality to the POTION object.
     */
    public void executeSurroundingItems(){
        try{
            //play drinking audio
            this.view.Audios.get(0).play(18);

            HashMap<String, String> surroundingRooms = getDirections(); //set up hashmap

            //play first animation frame for 1/3 of a second
            String firstImage = this.model.getDirectoryName() + "/animation-frames/full.jpg"; //load picture
            Image firstImageFile = new Image(firstImage);
            //format ImageView object
            ImageView firstImageView = new ImageView(firstImageFile);
            firstImageView.setPreserveRatio(true);
            firstImageView.setFitWidth(400);
            firstImageView.setFitHeight(400);
            //create a new Vbox and add it to cell 1,1 of the main gridpane
            VBox roomPane1 = new VBox(firstImageView);
            roomPane1.setPadding(new Insets(10));
            roomPane1.setAlignment(Pos.TOP_CENTER);
            roomPane1.setStyle("-fx-background-color: #000000;");
            pane.add(roomPane1, 1, 1);
            view.stage.sizeToScene();

            //play second animation frame for 1/3 a second
            PauseTransition second = new PauseTransition(Duration.millis(667));
            second.setOnFinished(f->{
                String secondImage = this.model.getDirectoryName() + "/animation-frames/half.jpg";
                Image secondImagefile = new Image(secondImage);
                ImageView secondImageView = new ImageView(secondImagefile);
                secondImageView.setPreserveRatio(true);
                secondImageView.setFitWidth(400);
                secondImageView.setFitHeight(400);
                VBox roomPane2 = new VBox(secondImageView);
                roomPane2.setPadding(new Insets(10));
                roomPane2.setAlignment(Pos.TOP_CENTER);
                roomPane2.setStyle("-fx-background-color: #000000;");
                pane.add(roomPane2, 1, 1);
                view.stage.sizeToScene();

                //play third animation frame for 1/3 a second
                PauseTransition third = new PauseTransition(Duration.millis(666));
                third.setOnFinished(g->{
                    String thirdImage = this.model.getDirectoryName() + "/animation-frames/empty.jpg";
                    Image thirdImageFile = new Image(thirdImage);
                    ImageView thirdImageView = new ImageView(thirdImageFile);
                    thirdImageView.setPreserveRatio(true);
                    thirdImageView.setFitWidth(400);
                    thirdImageView.setFitHeight(400);
                    VBox roomPane3 = new VBox(thirdImageView);
                    roomPane3.setPadding(new Insets(10));
                    roomPane3.setAlignment(Pos.TOP_CENTER);
                    roomPane3.setStyle("-fx-background-color: #000000;");
                    pane.add(roomPane3, 1, 1);
                    view.stage.sizeToScene();

                    //remove all elements in the middle of the gridpane after animation
                    PauseTransition surr = new PauseTransition(Duration.millis(666));
                    surr.setOnFinished(k -> {
                        this.pane.getChildren().removeAll(this.pane.getChildren().filtered(node -> GridPane.getRowIndex(node) == 1
                                && GridPane.getColumnIndex(node) == 1));
                        //format text to be shown on screen
                        String onScreen = "Directions and the objects in those directions:\n";
                        for(Map.Entry<String, String> entry: surroundingRooms.entrySet()){
                            String key = entry.getKey();
                            String value = entry.getValue();
                            onScreen += key + ": " + value + "\n";
                        }
                        Text text = new Text(onScreen);
                        text.setFont(Font.font("ariel", FontWeight.NORMAL, FontPosture.ITALIC, 25));
                        text.setFill(Color.WHITE);
                        text.setWrappingWidth(this.pane.getCellBounds(1, 1).getWidth());
                        text.setTextAlignment(TextAlignment.CENTER);
                        this.pane.add(text, 1, 1); //add text to the middle of gridpane for 8 seconds

                        //return to main game after displaying text
                        PauseTransition pause = new PauseTransition(Duration.seconds(8));
                        pause.setOnFinished(j ->{
                            this.pane.getChildren().removeAll(this.pane.getChildren().filtered(node ->
                                    GridPane.getRowIndex(node) == 1
                                            && GridPane.getColumnIndex(node) == 1));
                            this.pane.add(this.view.canvas, 1, 1);
                        });
                        pause.play(); //plays the pause at correct points
                    });
                    surr.play();
                });
                third.play();
            });
            second.play();
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
