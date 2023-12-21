package views;

import Controller.ScoreController;
import AdventureModel.*;
import Entity.Bullet;
import Settings.SettingsWindowBuilder;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.input.KeyEvent; //you will need these!
import javafx.scene.input.KeyCode;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.scene.AccessibleRole;

import Speech.SpeechRecognitionListener;
import Speech.SpeechToTextController;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Class AdventureGameView.
 *
 * This is the Class that will visualize your model.
 * You are asked to demo your visualization via a Zoom
 * recording. Place a link to your recording below.
 *
 * PHASE 2 PROJECT DEMO LINK: https://drive.google.com/file/d/1faqBPgMr4ySHva6tcDbD3JN14_c2FksN/view?usp=share_link
 * PASSWORD: N/A
 */
public class AdventureGameView extends JPanel implements Runnable, SpeechRecognitionListener {

    public AdventureGame model; //model of the game
    Stage stage; //stage on which all is rendered
    Button saveButton, loadButton, settingsButton; //buttons
    Boolean settingsToggle = false; //is help on display?

    GridPane gridPane = new GridPane(); //to hold images and buttons
    Label commandLabel = new Label(); //to hold commands
    Label roomDescLabel = new Label(); //to hold room description and/or instructions
    VBox objectsInRoom = new VBox(); //to hold room items
    VBox objectsInInventory = new VBox(); //to hold inventory items
    TextField inputTextField; //for user input

    public ArrayList<Audio> Audios; //to play audio
    public ArrayList<ColorAdjust> colorAdjusts;

    public ColorAdjust viewAdjust;
    private boolean mediaPlaying; //to know if the audio is playing

    public Canvas canvas = new Canvas(768, 576); //canvas to draw the main game onto

    Thread mainThread; //thread that the game will be running on

    public boolean wPressed, aPressed, sPressed, dPressed; //true when button is pressed, false when not being pressed

    public BlockMap blockMap = new BlockMap(canvas); // Information about all the blocks that need to be drawn onto the canvas

    public Collision collisionCheck = new Collision(this);

    private boolean isEventBeingProcessed = false;

    public ScoreView scoreView;

    public SettingsWindowBuilder builder = SettingsWindowBuilder.getInstance();
    ScoreController scoreController;

    Timeline timeline;

    Label score;

    Audio audio = new Audio(); // Audio class that plays audio on this screen

    // Model and views that are used to load the game again after it's completed
    AdventureGame model1;

    AdventureGame model2;
    AdventureGameView view1;

    AdventureGameView view2;
    StartPageView startPageView;

    /**
     * Adventure Game View Constructor
     * __________________________
     * Initializes attributes
     */
    public AdventureGameView(AdventureGame model, Stage stage) {
        this.model = model;
        this.stage = stage;
        // Instantiate and start the mainThread
        mainThread = new Thread(this);
        mainThread.start();
        this.Audios = new ArrayList<>();
        this.colorAdjusts = new ArrayList<>();
        // Map tiles and canvas creation
        this.model.blockMap = this.blockMap;
        this.model.canvas = this.canvas;
        commandLabel.setText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
        this.model.audio = this.audio;
        this.scoreView = new ScoreView(this);
        this.scoreController = new ScoreController(this.model, this);
    }


    /*
    * This method is called when the application is run. It is a loop that keeps running in the background and keeps
    * everything updated. It continuously calls update() and paint() to update and redraw everything that is on screen
     */
    public void run() {
        while (mainThread != null) {
            Platform.runLater(() -> {
                try {
                    update();
                } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                    throw new RuntimeException(e);
                }
                paint();
            });
            try {
                Thread.sleep(16); // Add a small delay to control the frame rate
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    * Updates all entities and objects in room. Continuously called to check player/troll collision with walls and each
    *  other, update player and troll locations, update bullets and combat damage. Also keeps track of player location
    * to check when player needs to be sent to a different room.
     */
    public void update() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        if (!this.model.getPlayer().direction.equals("idle")) {
            this.collisionCheck.checkCollision(this.model.getPlayer());
        }
        Audios.add(audio);
        audio.cooldown++;
        // Check users location to see if they are trying to walk into a different room. Call movePlayer() if they are and update items
        if (this.model.getPlayer().getCurrentRoom() != null) {
            if (this.model.getPlayer().getCurrentRoom().troll != null) {
                if (this.model.getPlayer().getX() < 24) {
                    if (!this.model.getPlayer().getCurrentRoom().troll.alive) {
                        for (Passage passage : this.model.getPlayer().getCurrentRoom().getMotionTable().passageTable) {
                            if (passage.getDirection().equals("WEST") && passage.getIsBlocked()) {
                                if (this.model.getPlayer().checkIfObjectInInventory(passage.getKeyName())) {
                                    this.model.movePlayer("WEST");
                                    commandLabel.setText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
                                    updateItems();
                                    this.model.getPlayer().setX(672);
                                    this.model.getPlayer().setY(264);
                                } else {
                                    commandLabel.setText("You need the correct key to enter this room");
                                    if (audio.cooldown > 50) {
                                        audio.play(0);
                                    }
                                    this.model.getPlayer().setX(this.model.getPlayer().getX() + 2);
                                }
                            } else if (passage.getDirection().equals("WEST")) {
                                this.model.movePlayer("WEST");
                                commandLabel.setText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
                                updateItems();
                                this.model.getPlayer().setX(672);
                                this.model.getPlayer().setY(264);
                            }
                        }
                    } else {
                        commandLabel.setText("You must defeat the monster to continue");
                        if (audio.cooldown > 50) {
                            audio.play(17);
                        }
                        this.model.getPlayer().setX(this.model.getPlayer().getX() + 2);
                    }
                } else if (this.model.getPlayer().getX() + 48 > 744) {
                    if (!this.model.getPlayer().getCurrentRoom().troll.alive) {
                        for (Passage passage : this.model.getPlayer().getCurrentRoom().getMotionTable().passageTable) {
                            if (passage.getDirection().equals("EAST") && passage.getIsBlocked()) {
                                if (this.model.getPlayer().checkIfObjectInInventory(passage.getKeyName())) {
                                    this.model.movePlayer("EAST");
                                    commandLabel.setText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
                                    updateItems();
                                    this.model.getPlayer().setX(48);
                                    this.model.getPlayer().setY(264);
                                } else {
                                    commandLabel.setText("You need the correct key to enter this room");
                                    if (audio.cooldown > 50) {
                                        audio.play(0);
                                    }
                                    this.model.getPlayer().setX(this.model.getPlayer().getX() - 2);
                                }
                            } else if (passage.getDirection().equals("EAST")) {
                                this.model.movePlayer("EAST");
                                commandLabel.setText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
                                updateItems();
                                this.model.getPlayer().setX(48);
                                this.model.getPlayer().setY(264);
                            }
                        }
                    } else {
                        commandLabel.setText("You must defeat the monster to continue");
                        if (audio.cooldown > 50) {
                            audio.play(17);
                        }
                        this.model.getPlayer().setX(this.model.getPlayer().getX() - 2);
                    }
                } else if (this.model.getPlayer().getY() < 24) {
                    if (!this.model.getPlayer().getCurrentRoom().troll.alive) {
                        for (Passage passage : this.model.getPlayer().getCurrentRoom().getMotionTable().passageTable) {
                            if (passage.getDirection().equals("NORTH") && passage.getIsBlocked()) {
                                if (this.model.getPlayer().checkIfObjectInInventory(passage.getKeyName())) {
                                    this.model.movePlayer("NORTH");
                                    commandLabel.setText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
                                    updateItems();
                                    this.model.getPlayer().setX(360);
                                    this.model.getPlayer().setY(480);
                                } else {
                                    commandLabel.setText("You need the correct key to enter this room");
                                    if (audio.cooldown > 50) {
                                        audio.play(0);
                                    }
                                    this.model.getPlayer().setY(this.model.getPlayer().getY() + 2);
                                }
                            } else if (passage.getDirection().equals("NORTH")) {
                                this.model.movePlayer("NORTH");
                                commandLabel.setText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
                                updateItems();
                                this.model.getPlayer().setX(360);
                                this.model.getPlayer().setY(480);
                            }
                        }
                    } else {
                        commandLabel.setText("You must defeat the monster to continue");
                        if (audio.cooldown > 50) {
                            audio.play(17);
                        }
                        this.model.getPlayer().setY(this.model.getPlayer().getY() + 2);
                    }
                } else if (this.model.getPlayer().getY() + 48 > 552) {
                    if (!this.model.getPlayer().getCurrentRoom().troll.alive) {
                        for (Passage passage : this.model.getPlayer().getCurrentRoom().getMotionTable().passageTable) {
                            if (passage.getDirection().equals("SOUTH") && passage.getIsBlocked()) {
                                if (this.model.getPlayer().checkIfObjectInInventory(passage.getKeyName())) {
                                    this.model.movePlayer("SOUTH");
                                    commandLabel.setText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
                                    updateItems();
                                    this.model.getPlayer().setX(360);
                                    this.model.getPlayer().setY(48);
                                } else {
                                    commandLabel.setText("You need the correct key to enter this room");
                                    if (audio.cooldown > 50) {
                                        audio.play(0);
                                    }
                                    this.model.getPlayer().setY(this.model.getPlayer().getY() - 2);
                                }
                            } else if (passage.getDirection().equals("SOUTH")) {
                                this.model.movePlayer("SOUTH");
                                commandLabel.setText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
                                updateItems();
                                this.model.getPlayer().setX(360);
                                this.model.getPlayer().setY(48);
                            }
                        }
                    } else {
                        commandLabel.setText("You must defeat the monster to continue");
                        if (audio.cooldown > 50) {
                            audio.play(17);
                        }
                        this.model.getPlayer().setY(this.model.getPlayer().getY() - 2);
                    }
                }
            } else {
                if (this.model.getPlayer().getX() < 24) {
                    for (Passage passage : this.model.getPlayer().getCurrentRoom().getMotionTable().passageTable) {
                        if (passage.getDirection().equals("WEST") && passage.getIsBlocked()) {
                            if (this.model.getPlayer().checkIfObjectInInventory(passage.getKeyName())) {
                                this.model.movePlayer("WEST");
                                commandLabel.setText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
                                updateItems();
                                this.model.getPlayer().setX(672);
                                this.model.getPlayer().setY(264);
                            } else {
                                commandLabel.setText("You need the correct key to enter this room");
                                if (audio.cooldown > 50) {
                                    audio.play(0);
                                }
                                this.model.getPlayer().setX(this.model.getPlayer().getX() + 2);
                            }
                        } else if (passage.getDirection().equals("WEST")) {
                            this.model.movePlayer("WEST");
                            commandLabel.setText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
                            updateItems();
                            this.model.getPlayer().setX(672);
                            this.model.getPlayer().setY(264);
                        }
                    }
                } else if (this.model.getPlayer().getX() + 48 > 744) {
                    for (Passage passage : this.model.getPlayer().getCurrentRoom().getMotionTable().passageTable) {
                        if (passage.getDirection().equals("EAST") && passage.getIsBlocked()) {
                            if (this.model.getPlayer().checkIfObjectInInventory(passage.getKeyName())) {
                                this.model.movePlayer("EAST");
                                commandLabel.setText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
                                updateItems();
                                this.model.getPlayer().setX(48);
                                this.model.getPlayer().setY(264);
                            } else {
                                commandLabel.setText("You need the correct key to enter this room");
                                if (audio.cooldown > 50) {
                                    audio.play(0);
                                }
                                this.model.getPlayer().setX(this.model.getPlayer().getX() - 2);
                            }
                        } else if (passage.getDirection().equals("EAST")) {
                            this.model.movePlayer("EAST");
                            commandLabel.setText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
                            updateItems();
                            this.model.getPlayer().setX(48);
                            this.model.getPlayer().setY(264);
                        }
                    }
                } else if (this.model.getPlayer().getY() < 24) {
                    for (Passage passage : this.model.getPlayer().getCurrentRoom().getMotionTable().passageTable) {
                        if (passage.getDirection().equals("NORTH") && passage.getIsBlocked()) {
                            if (this.model.getPlayer().checkIfObjectInInventory(passage.getKeyName())) {
                                this.model.movePlayer("NORTH");
                                commandLabel.setText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
                                updateItems();
                                this.model.getPlayer().setX(360);
                                this.model.getPlayer().setY(480);
                            } else {
                                commandLabel.setText("You need the correct key to enter this room");
                                if (audio.cooldown > 50) {
                                    audio.play(0);
                                }
                                this.model.getPlayer().setY(this.model.getPlayer().getY() + 2);
                            }
                        } else if (passage.getDirection().equals("NORTH")) {
                            this.model.movePlayer("NORTH");
                            commandLabel.setText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
                            updateItems();
                            this.model.getPlayer().setX(360);
                            this.model.getPlayer().setY(480);
                        }
                    }
                } else if (this.model.getPlayer().getY() + 48 > 552) {
                    for (Passage passage : this.model.getPlayer().getCurrentRoom().getMotionTable().passageTable) {
                        if (passage.getDirection().equals("SOUTH") && passage.getIsBlocked()) {
                            if (this.model.getPlayer().checkIfObjectInInventory(passage.getKeyName())) {
                                this.model.movePlayer("SOUTH");
                                commandLabel.setText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
                                updateItems();
                                this.model.getPlayer().setX(360);
                                this.model.getPlayer().setY(48);
                            } else {
                                commandLabel.setText("You need the correct key to enter this room");
                                if (audio.cooldown > 50) {
                                    audio.play(0);
                                }
                                this.model.getPlayer().setY(this.model.getPlayer().getY() - 2);
                            }
                        } else if (passage.getDirection().equals("SOUTH")) {
                            this.model.movePlayer("SOUTH");
                            commandLabel.setText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
                            updateItems();
                            this.model.getPlayer().setX(360);
                            this.model.getPlayer().setY(48);
                        }
                    }
                }
            }
        }
        // Update the players location based on what key they are presssing
        this.model.getPlayer().update(wPressed, aPressed, sPressed, dPressed, gc, 48);

        // Check collision of player and trolls, then check if player is attack or if bullets are colliding to do damage
        if (this.model.getPlayer().getCurrentRoom() != null) {
            if (this.model.getPlayer().getCurrentRoom().troll != null) {
                this.collisionCheck.checkCollision(this.model.getPlayer().getCurrentRoom().troll);
                boolean collided = this.collisionCheck.checkEntityCollision(this.model.getPlayer(), this.model.getPlayer().getCurrentRoom().troll);
                this.collisionCheck.checkEntityCollision(this.model.getPlayer().getCurrentRoom().troll, this.model.getPlayer());
                if (this.model.getPlayer().attacking && this.model.getPlayer().getCurrentRoom().troll.cooldown > 15) {
                    if (collided) {
                        this.model.getPlayer().getCurrentRoom().troll.health -= this.model.getPlayer().damage;
                        this.model.getPlayer().getCurrentRoom().troll.cooldown = 0;
                        this.model.gamescore.updateScore(100);
                    }
                    if (this.model.getPlayer().checkIfObjectInInventory("GUN") && this.model.getPlayer().cooldown > 20) {
                        this.model.getPlayer().bullets.add(new Bullet(this.model.getPlayer()));
                        this.model.getPlayer().cooldown = 0;
                    }
                }
                this.model.getPlayer().getCurrentRoom().troll.update(gc);
            }
            if (this.model.getPlayer().getCurrentRoom().troll != null) {
                for (Bullet bullet : this.model.getPlayer().bullets) {
                    bullet.update();
                    if (this.collisionCheck.checkEntityCollision(bullet, this.model.getPlayer().getCurrentRoom().troll) && this.model.getPlayer().getCurrentRoom().troll.cooldown > 15) {
                        if (this.model.getPlayer().getCurrentRoom().troll != null) {
                            this.model.getPlayer().getCurrentRoom().troll.health -= this.model.getPlayer().damage;
                            this.model.getPlayer().getCurrentRoom().troll.cooldown = 0;
                            this.model.gamescore.updateScore(100);
                        }
                    }
                }
                for (Bullet bullet : this.model.getPlayer().getCurrentRoom().troll.bullets) {
                    bullet.update();
                    if (this.collisionCheck.checkEntityCollision(bullet, this.model.getPlayer()) && this.model.getPlayer().getCurrentRoom().troll.cooldown > 15) {
                        if (this.model.getPlayer().health != 0) {
                            this.model.getPlayer().health -= this.model.getPlayer().getCurrentRoom().troll.damage;
                            this.model.gamescore.updateScore(-100);
                        }
                        this.model.getPlayer().getCurrentRoom().troll.cooldown = 0;
                    }
                }
            }
            if (this.model.getPlayer().health <= 0 || this.model.getPlayer().getCurrentRoom().getRoomNumber() == 10 || this.model.getPlayer().getCurrentRoom().getRoomNumber() == 16){
                submitEvent("END");
            }
        }
    }

    /*
    * Draws everything that's been updated onto the screen
     */
    public void paint() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        if (this.model.getPlayer().getCurrentRoom() != null) {
            this.blockMap.draw(gc, this.model.getPlayer().getCurrentRoom().getRoomName());
            if (this.model.getPlayer().getCurrentRoom().troll != null) {
                this.model.getPlayer().getCurrentRoom().troll.draw(gc);
            }
            for (Bullet bullet : this.model.getPlayer().bullets) {
                bullet.draw(gc);
            }
            if (this.model.getPlayer().getCurrentRoom().troll != null) {
                for (Bullet bullet : this.model.getPlayer().getCurrentRoom().troll.bullets) {
                    bullet.draw(gc);
                }
            }
        }
        this.model.getPlayer().draw(gc);
    }

    /**
     * Initialize the UI
     */
    public void intiUI() {

        // setting up the stage
        this.stage.setTitle("<Group_21>'s Adventure Game");

        //Inventory + Room items
        objectsInInventory.setSpacing(10);
        objectsInInventory.setAlignment(Pos.TOP_CENTER);
        objectsInRoom.setSpacing(10);
        objectsInRoom.setAlignment(Pos.TOP_CENTER);

        // GridPane, anyone?
        gridPane.setPadding(new Insets(20));
        gridPane.setBackground(new Background(new BackgroundFill(
                Color.valueOf("#000000"),
                new CornerRadii(0),
                new Insets(0)
        )));

        //Three columns, three rows for the GridPane
        ColumnConstraints column1 = new ColumnConstraints(200);
        ColumnConstraints column2 = new ColumnConstraints(768);
        ColumnConstraints column3 = new ColumnConstraints(250);
        column3.setHgrow( Priority.SOMETIMES ); //let some columns grow to take any extra space
        column1.setHgrow( Priority.SOMETIMES );

        // Row constraints
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints( 576 );
        RowConstraints row3 = new RowConstraints();
        row1.setVgrow( Priority.SOMETIMES );
        row3.setVgrow( Priority.SOMETIMES );

        gridPane.getColumnConstraints().addAll( column1 , column2 , column1 );
        gridPane.getRowConstraints().addAll( row1 , row2 , row1 );

        // Buttons
        saveButton = new Button("Save");
        saveButton.setId("Save");
        customizeButton(saveButton, 100, 50);
        makeButtonAccessible(saveButton, "Save Button", "This button saves the game.", "This button saves the game. Click it in order to save your current progress, so you can play more later.");
        addSaveEvent();

        loadButton = new Button("Load");
        loadButton.setId("Load");
        customizeButton(loadButton, 100, 50);
        makeButtonAccessible(loadButton, "Load Button", "This button loads a game from a file.", "This button loads the game from a file. Click it in order to load a game that you saved at a prior date.");
        addLoadEvent();

        // create settingsButton
        settingsButton = new Button("Settings");
        settingsButton.setId("Settings");
        customizeButton(settingsButton, 200, 50);
        makeButtonAccessible(settingsButton, "Settings Button", "This button directs you to the settings.", "This button gives settings to change the game controls. Click it to learn how to play.");
        addSettingsEvent();

        settingsButton.setStyle("-fx-background-color: #17871b");
        saveButton.setStyle("-fx-background-color: #17871b");
        loadButton.setStyle("-fx-background-color: #17871b");



        Button blankbutton = new Button();
        blankbutton.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        blankbutton.setPrefWidth(100);
        // adding recordButton
        ImageView microphoneIcon = new ImageView(new Image("Speech/src/micro.png")); // Adjust the path accordingly
        microphoneIcon.setFitWidth(25);
        microphoneIcon.setFitHeight(25);
        Button recordButton = new Button(null, microphoneIcon);

        //adding stopButton
        ImageView stopIcon = new ImageView((new Image("Speech/src/stop.png")));
        stopIcon.setFitWidth(25);
        stopIcon.setFitHeight(25);
        Button stopButton = new Button(null, stopIcon);

        SpeechToTextController controller = new SpeechToTextController(this);

        recordButton.setOnAction(event -> {
            // Start recording when the button is clicked
            controller.setEventSubmitted(false);
            controller.setStopRecording(false);
            new Thread(controller::convertSpeechToText).start();

            // disable buttons depending on action
            stopButton.setDisable(false);
            recordButton.setDisable(true);
        });

        stopButton.setOnAction(event -> {
            // Stop recording when the stop button is clicked
            controller.stopRecording();

            // disable buttons depending on action
            recordButton.setDisable(false);
            stopButton.setDisable(true);
        });

        HBox topButtons = new HBox();
        topButtons.getChildren().addAll(saveButton, settingsButton, loadButton);
        topButtons.setSpacing(10);
        topButtons.setAlignment(Pos.CENTER);

        inputTextField = new TextField();
        inputTextField.setFont(new Font("Arial", 16));
        inputTextField.setFocusTraversable(true);

        inputTextField.setAccessibleRole(AccessibleRole.TEXT_AREA);
        inputTextField.setAccessibleRoleDescription("Text Entry Box");
        inputTextField.setAccessibleText("Enter commands in this box.");
        inputTextField.setAccessibleHelp("This is the area in which you can enter commands you would like to play.  Enter a command and hit return to continue.");
        addTextHandlingEvent(); //attach an event to this input field

        //labels for inventory and room items
        Label objLabel =  new Label("Objects in Room");
        objLabel.setAlignment(Pos.CENTER);
        objLabel.setStyle("-fx-text-fill: white;");
        objLabel.setFont(new Font("Arial", 16));

        Label invLabel =  new Label("Your Inventory");
        invLabel.setAlignment(Pos.CENTER);
        invLabel.setStyle("-fx-text-fill: white;");
        invLabel.setFont(new Font("Arial", 16));

        //add all the widgets to the GridPane
        gridPane.add( objLabel, 0, 0, 1, 1 );  // Add label
        gridPane.add( topButtons, 1, 0, 1, 1 );  // Add buttons
        gridPane.add( invLabel, 2, 0, 1, 1 );  // Add label

        commandLabel.setStyle("-fx-text-fill: white;");
        commandLabel.setFont(new Font("Arial", 16));

        updateItems(); //update items shows inventory and objects in rooms

        // adding recordButton & stopButton beside textEntry
        HBox recordTextEntry = new HBox();
        recordTextEntry.setAlignment(Pos.CENTER); // Set alignment to center
        recordTextEntry.setPadding(new Insets(20, 20, 20, 20));
        HBox.setHgrow(inputTextField, Priority.ALWAYS);
        recordTextEntry.setSpacing(10);
        recordTextEntry.getChildren().addAll(inputTextField, recordButton, stopButton);

        //scorebutton
        if (this.model.getPlayer().getName() != null) {
            this.score = new Label("NAME: " + this.model.getPlayer().getName() + " | SCORE: " + this.model.gamescore.getScore());
        } else {
            this.score = new Label("SCORE: " + this.model.gamescore.getScore());
        }
        score.setStyle("-fx-text-fill: white;-fx-font-family: 'Arial'; -fx-font-size: 15");

        // adding the text area and submit button to a VBox
        VBox textEntry = new VBox();
        textEntry.setStyle("-fx-background-color: #000000;");
        textEntry.setPadding(new Insets(20, 20, 20, 20));
        textEntry.getChildren().addAll(score,commandLabel, recordTextEntry);
        textEntry.setSpacing(10);
        textEntry.setAlignment(Pos.CENTER);
        gridPane.add( textEntry, 0, 2, 3, 1 );

        // add the canvas that shows the main game into the center of the screen
        gridPane.add(canvas, 1, 1);

        // Render everything
        var scene = new Scene( gridPane ,  1200, 800);

        // add all the keyboard inputs that let the game know when a key is being pressed
        scene.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode == KeyCode.W) {
                wPressed = true;
            }
            if (keyCode == KeyCode.A) {
                aPressed = true;
            }
            if (keyCode == KeyCode.S) {
                sPressed = true;
            }
            if (keyCode == KeyCode.D) {
                dPressed = true;
            }
            if (keyCode == KeyCode.P) {
                this.model.getPlayer().attacking = true;
            }
        });
        scene.setOnKeyReleased(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode == KeyCode.W) {
                wPressed = false;
            }
            if (keyCode == KeyCode.A) {
                aPressed = false;
            }
            if (keyCode == KeyCode.S) {
                sPressed = false;
            }
            if (keyCode == KeyCode.D) {
                dPressed = false;
            }
            if (keyCode == KeyCode.P) {
                this.model.getPlayer().attacking = false;
            }
        });
        this.timeline = new Timeline(new KeyFrame(Duration.millis(1000), e ->
        {
            this.model.gamescore.updateScore(1);
            if (this.model.getPlayer().getName() != null) {
                this.score.setText("NAME: " + this.model.getPlayer().getName() + " | SCORE: " + this.model.gamescore.getScore());
            } else {
                this.score.setText("SCORE: " + this.model.gamescore.getScore());
            }
        }
        ));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        audio.play(1);

        scene.setFill(Color.BLACK);

        viewAdjust = new ColorAdjust();

        builder.addColorAdjust(viewAdjust);

        for (Node node : scene.getRoot().getChildrenUnmodifiable()) {
            node.setEffect(viewAdjust);
        }

        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.show();
    }

    /**
     * onSpeechRecognized
     * ---
     * overrides interface SpeechRecognitionListener
     * ---
     * a method that takes the Speech-to-Text output and inputs it into the TextField.
     *
     * @param transcript the output from the Speech-to-Text function
     * @param controller the object that communicates between the Google API and the AdventureGame
     */
    @Override
    public void onSpeechRecognized(String transcript, Speech.SpeechToTextController controller) {
        Platform.runLater(() -> {
            if (!isEventBeingProcessed) {
                inputTextField.setText(transcript);

                // Set the flag to prevent additional submissions
                isEventBeingProcessed = true;

                // Submit the event after a delay
                submitEventAfterDelay(transcript);
            }
        });
    }

    /**
     * submitEventAfterDelay
     *
     * a method that takes the Speech-to-Text output and waits a couple
     * of seconds before submitting the event to the model.
     *
     * @param transcript the output from the Speech-to-Text function
     */
    private void submitEventAfterDelay(String transcript) {
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> {
            submitEvent(transcript);
            inputTextField.clear();

            // Reset the flag after submission to allow the next event
            isEventBeingProcessed = false;
        });
        pause.play();
    }


    /**
     * makeButtonAccessible
     * __________________________
     * For information about ARIA standards, see
     * https://www.w3.org/WAI/standards-guidelines/aria/
     *
     * @param inputButton the button to add screenreader hooks to
     * @param name ARIA name
     * @param shortString ARIA accessible text
     * @param longString ARIA accessible help text
     */
    public static void makeButtonAccessible(Button inputButton, String name, String shortString, String longString) {
        inputButton.setAccessibleRole(AccessibleRole.BUTTON);
        inputButton.setAccessibleRoleDescription(name);
        inputButton.setAccessibleText(shortString);
        inputButton.setAccessibleHelp(longString);
        inputButton.setFocusTraversable(true);
    }

    /**
     * customizeButton
     * __________________________
     *
     * @param inputButton the button to make stylish :)
     * @param w width
     * @param h height
     */
    public void customizeButton(Button inputButton, int w, int h) {
        inputButton.setPrefSize(w, h);
        inputButton.setFont(new Font("Arial", 16));
        inputButton.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-style: solid;-fx-text-fill: white");
    }

    /**
     * addTextHandlingEvent
     * __________________________
     * Add an event handler to the myTextField attribute 
     *
     * Your event handler should respond when users 
     * hits the ENTER or TAB KEY. If the user hits 
     * the ENTER Key, strip white space from the
     * input to inputTextField and pass the stripped
     * string to submitEvent for processing.
     *
     * If the user hits the TAB key, move the focus 
     * of the scene onto any other node in the scene 
     * graph by invoking requestFocus method.
     * this.addSettingsEvent();
     */
    private void addTextHandlingEvent() {
        inputTextField.setOnKeyPressed(this::handleKeyEvent);

    }

    public void handleKeyEvent(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            String input = inputTextField.getText().trim();
            submitEvent(input);
            inputTextField.clear();
        } else if (event.getCode().equals(KeyCode.TAB)) {
            gridPane.requestFocus();
        }
    }



    /**
     * submitEvent
     * __________________________
     *
     * @param text the command that needs to be processed
     */
    private void submitEvent(String text) {

        text = text.strip(); //get rid of white space
        audio.stop();

        if (text.equalsIgnoreCase("LOOK") || text.equalsIgnoreCase("L")) {
            String roomDesc = this.model.getPlayer().getCurrentRoom().getRoomDescription();
            String objectString = this.model.getPlayer().getCurrentRoom().getObjectString();

            if (!objectString.isEmpty()) roomDescLabel.setText(roomDesc + "\n\nObjects in this room:\n" + objectString);
            //articulateRoomDescription(); //all we want, if we are looking, is to repeat description.
            return;
        } else if (text.equalsIgnoreCase("HELP") || text.equalsIgnoreCase("H")) {
            openSettingsWindow();
            return;
        } else if (text.equalsIgnoreCase("COMMANDS") || text.equalsIgnoreCase("C")) {
            showCommands(); //this is new!  We did not have this command in A1
            return;
        }

        //try to move!
        String output = this.model.interpretAction(text); //process the command!

        if (output == null || (!output.equals("GAME OVER") && !output.equals("FORCED") && !output.equals("HELP"))) {
            updateItems();
        }
        else if (output.equals("GAME OVER")) { //When the game ends
            if (this.model.getDirectoryName().equals("TinyGame")) {
                Room room = this.model.getRooms().get(1);
                this.model.getPlayer().setCurrentRoom(room);
            } else {
                Room room = this.model.getRooms().get(11);
                this.model.getPlayer().setCurrentRoom(room);
            }
            this.model.getPlayer().health = 3;
            this.timeline.stop();
            this.scoreController.addScore();
            String onScreen = "GAME OVER\n";
            onScreen = onScreen + "score: " + String.valueOf(this.model.gamescore.getScore()); //add score attribute
            //add code for getting the score onto the text object
            Text gameOver = new Text(onScreen);
            PauseTransition deathPause = new PauseTransition(Duration.millis(500)); //half a second delay
            deathPause.setOnFinished(k -> {
                this.gridPane.getChildren().removeAll(this.gridPane.getChildren());
                gameOver.setFont(Font.font("ariel", FontWeight.NORMAL, FontPosture.REGULAR, 30));
                gameOver.setFill(Color.WHITE);
                gameOver.setWrappingWidth(this.gridPane.getCellBounds(1, 1).getWidth());
                gameOver.setTextAlignment(TextAlignment.CENTER);
                this.gridPane.add(gameOver, 1, 1);
                PauseTransition pause = new PauseTransition(Duration.seconds(5));
                pause.setOnFinished(event -> {
                    stage.close();
                    this.model1 = new AdventureGame("TinyGame"); //change the name of the game if you want to try something bigger!
                    this.model2 = new AdventureGame("Tutorial");
                    this.view1 = new AdventureGameView(model1, this.stage);
                    this.view2 = new AdventureGameView(model2, this.stage);

                    this.startPageView = new StartPageView(this.view1, this.view2);
                });
                pause.play();
            });
            deathPause.play();
        } else if (output.equals("FORCED")) {
            //write code here to handle "FORCED" events!
            updateItems();
            //articulateRoomDescription();

            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(e ->{
                //if commands of room has forced
                // submitEvent("Forced")
                if (this.model.getPlayer().getCurrentRoom().getCommands().contains("FORCED")){
                    submitEvent("FORCED");
                }
            });
            pause.play();
        }
    }


    /**
     * showCommands
     * __________________________
     *
     * update the text in the GUI (within roomDescLabel)
     * to show all the moves that are possible from the 
     * current room.
     */
    private void showCommands() {
        try {
            String onScreen = "The following commands for this room are \n" +
                    this.model.getPlayer().getCurrentRoom().getCommands();

            Text text = new Text(onScreen);
            text.setFont(Font.font("ariel", FontWeight.NORMAL, FontPosture.ITALIC, 25));
            text.setFill(Color.WHITE);
            text.setWrappingWidth(400);
            text.setTextAlignment(TextAlignment.CENTER);

            StackPane stackPane = new StackPane();

            // Set the background color of the StackPane to black
            stackPane.setStyle("-fx-background-color: black;");

            stackPane.getChildren().add(text);

            // Center the Text node within the StackPane
            StackPane.setAlignment(text, Pos.CENTER);

            // Add information to the GridPane
            this.gridPane.add(stackPane, 1, 1);

            PauseTransition pause = new PauseTransition(Duration.seconds(8));
            pause.setOnFinished(event -> {
                // Cleanup: Remove the displayed information
                this.gridPane.getChildren().removeAll(this.gridPane.getChildren().filtered(node ->
                        GridPane.getRowIndex(node) == 1 && GridPane.getColumnIndex(node) == 1));

                // Optionally add back the original content
                this.gridPane.add(canvas, 1, 1);
            });

            // Start the delay
            pause.play();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * updateItems
     * __________________________
     *
     * The method populates the objectsInRoom and objectsInInventory Vboxes.
     * Each Vbox should contain a collection of Buttons that represent object in the game.
     * Each button represents a different object. Based on the object, there will be functionality for it.
     * Whenever the object is clicked, it will switch between inventory and the room floor.
     * 
     * Images of each object are in the assets 
     * folders of the given adventure game.
     */
    public void updateItems() {
        objectsInInventory.getChildren().clear();
        objectsInRoom.getChildren().clear();

        //Loop over every object found in the room
        for(AdventureObject object: this.model.getPlayer().getCurrentRoom().objectsInRoom){
            //create image of object
            String objectImagePath = "file:" + this.model.getDirectoryName() + "/objectImages/" + object.getName() + ".jpg";
            Image objectImage = new Image(objectImagePath);
            ImageView objectViewImage = new ImageView(objectImage);
            objectViewImage.setPreserveRatio(true);
            objectViewImage.setFitWidth(100);
            objectViewImage.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
            objectViewImage.setAccessibleText("this button lets you drop an item from inventory or pick it up from room");

            //create button of object
            Button objectButton = new Button(object.getName());
            objectButton.setGraphic(objectViewImage);
            objectButton.setContentDisplay(ContentDisplay.TOP);
            objectButton.setId(object.getName());
            makeButtonAccessible(objectButton, object.getName()+" button",
                    "The button for " + object.getName() + ".", "this button lets you interact with the "
                            + object.getName() + " to put in inventory or room.");

            // objectButton action handler
            objectButton.setOnAction(e -> {
                if (objectsInInventory.getChildren().contains(objectButton)){
                    objectsInInventory.getChildren().remove(objectButton);
                    objectsInRoom.getChildren().add(objectButton);


                    this.model.getPlayer().inventory.remove(object);
                    this.model.getPlayer().getCurrentRoom().objectsInRoom.add(object);
                }
                else if (objectsInRoom.getChildren().contains(objectButton)) {
                    objectsInRoom.getChildren().remove(objectButton);
                    objectsInInventory.getChildren().add(objectButton);

                    this.model.getPlayer().getCurrentRoom().objectsInRoom.remove(object);

                    //check if the object is a potion, if true then call the extender to complete functionality
                    if (Objects.equals(object.getName(), "POTION")){
                        //initialize the decorator
                        Extender extender = new Extender(this.model.getPlayer(), this.model, this.gridPane, this);
                        extender.executeSurroundingItems();
                        objectsInInventory.getChildren().remove(objectButton); //remove the potion from inventory after use
                    }else{
                        this.model.getPlayer().inventory.add(object);
                    }
                }
            });
            objectsInRoom.getChildren().add(objectButton);
        }

        //Loop over all object found in the players inventory
        for(AdventureObject object: this.model.getPlayer().inventory){
            //create images for objects
            String objectImagePath = "file:" + this.model.getDirectoryName() + "/objectImages/" + object.getName() + ".jpg";
            Image objectImage = new Image(objectImagePath);
            ImageView objectViewImage = new ImageView(objectImage);
            objectViewImage.setPreserveRatio(true);
            objectViewImage.setFitWidth(100);
            objectViewImage.setAccessibleText("this button lets you drop an item from inventory or pick it up from room");
            objectViewImage.setAccessibleRole(AccessibleRole.IMAGE_VIEW);

            //create button for object
            Button objectButton = new Button(object.getName());
            objectButton.setGraphic(objectViewImage);
            objectButton.setContentDisplay(ContentDisplay.BOTTOM);
            objectButton.setId(object.getName());
            makeButtonAccessible(objectButton, object.getName()+" button",
                    "The button for " + object.getName() + ".", "this button lets you interact with the "
                            + object.getName() + " to put in inventory or room.");

            // objectButton action handler
            objectButton.setOnAction(e -> {
                //if the object was in the players inventory when it was clicked
                if (objectsInInventory.getChildren().contains(objectButton)){

                    objectsInInventory.getChildren().remove(objectButton);
                    objectsInRoom.getChildren().add(objectButton);

                    this.model.getPlayer().inventory.remove(object);
                    this.model.getPlayer().getCurrentRoom().objectsInRoom.add(object);
                }//if the object was in the room when it was clicked
                else if (objectsInRoom.getChildren().contains(objectButton)) {
                    objectsInRoom.getChildren().remove(objectButton);
                    objectsInInventory.getChildren().add(objectButton);

                    this.model.getPlayer().getCurrentRoom().objectsInRoom.remove(object);
                    this.model.getPlayer().inventory.add(object);
                }
            });
            objectsInInventory.getChildren().add(objectButton);
        }

        //create scrollpanes and place on either side of the screen
        ScrollPane scO = new ScrollPane(objectsInRoom);
        scO.setPadding(new Insets(10));
        scO.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        scO.setFitToWidth(true);
        gridPane.add(scO,0,1);

        ScrollPane scI = new ScrollPane(objectsInInventory);
        scI.setFitToWidth(true);
        scI.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        gridPane.add(scI,2,1);
    }

    /**
     * openSettingsWindow
     * ---------------------
     * Show the game settings.
     */
    public void openSettingsWindow() {
        try {
            for (Audio audio : Audios) {
                builder.addAudio(audio);
            }
            for (ColorAdjust color : colorAdjusts) {
                builder.addColorAdjust(color);
            }
            builder.buildSettingsWindow().showAndWait();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * This method handles the event related to the
     * help button.
     */
    public void addSettingsEvent() {
        settingsButton.setOnAction(e -> {
            openSettingsWindow();
        });
    }

    /**
     * This method handles the event related to the
     * save button.
     */
    public void addSaveEvent() {
        saveButton.setOnAction(e -> {
            gridPane.requestFocus();
            SaveView saveView = new SaveView(this);
        });
    }

    /**
     * This method handles the event related to the
     * load button.
     */
    public void addLoadEvent() {
        loadButton.setOnAction(e -> {
            gridPane.requestFocus();
            LoadView loadView = new LoadView(this);
        });
    }
}
