package views;

import Settings.SettingsWindowBuilder;
import AdventureModel.AdventureGame;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Class StartPageView.
 * This is the Class that will visualize the start page.
 *
 */

public class StartPageView {
    private AdventureGameView adventureGameView;
    private AdventureGameView tutorialGameView;
    Background background = new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY));
    TextField nameInput;
    Button scoreButton, tutorialButton, settingsButton, playButton, continueButton, helpButton;
    GridPane gridPaneSP = new GridPane();
    GridPane gridpaneRP = new GridPane();
    private ColorAdjust colorAdjust;

    /**
     * StartPage View Constructor
     * Initializes attributes
     * @param mainGame : the AdventureGameView for main game
     * @param tutorialGame the AdventureGameView for tutorial
     */
    public StartPageView(AdventureGameView mainGame, AdventureGameView tutorialGame) {
        this.adventureGameView = mainGame;
        this.tutorialGameView = tutorialGame;
        initStartPage();
    }

    /**
     * Initialize the UI for the start page
     */
    public void initStartPage() {
        this.adventureGameView.stage.setTitle("Group21's Adventure Game");
        Image backgroundImage = new Image("file:Games/Images/reg_bgimg.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(1200, 700, true, true, false, true);
        BackgroundImage bgImage = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize);

        // Set the background of BorderPane
        gridPaneSP.setBackground(new Background(bgImage));
        ColumnConstraints column1 = new ColumnConstraints(150);
        ColumnConstraints column2 = new ColumnConstraints(150);
        ColumnConstraints column3 = new ColumnConstraints(600);
        ColumnConstraints column4 = new ColumnConstraints();


        // Row constraints
        RowConstraints row1 = new RowConstraints(190);
        RowConstraints row2 = new RowConstraints(100);
        RowConstraints row3 = new RowConstraints();
        RowConstraints row4 = new RowConstraints(100);
        row3.setVgrow(Priority.ALWAYS);

        gridPaneSP.getColumnConstraints().addAll(column1, column2, column3, column4);
        gridPaneSP.getRowConstraints().addAll(row1, row2, row3, row4);

        var scene = new Scene(gridPaneSP, 1200, 700);
        this.adventureGameView.stage.setScene(scene);
        this.adventureGameView.stage.setResizable(true);
        this.adventureGameView.stage.show();

        VBox vbox1 = new VBox();
        vbox1.setBorder(new Border(new BorderStroke(Color.TRANSPARENT,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(30))));

        //score
        scoreButton = new Button("ScoreBoard");
        scoreButton.setId("Score");
        this.adventureGameView.customizeButton(scoreButton, 200, 50);
        scoreButton.setBackground(background);
        AdventureGameView.makeButtonAccessible(scoreButton, "Score Board", "This button shows the previous scores.", "This button shows the previous scores. Click it in order to view previous high scores");
        addScoreEvent();


        // instruction
        helpButton = new Button("Instructions");
        helpButton.setId("Instructions");
        this.adventureGameView.customizeButton(helpButton, 200, 50);
        helpButton.setBackground(background);
        AdventureGameView.makeButtonAccessible(helpButton, "Help Button", "This button gives game instructions.", "This button gives instructions on the game controls. Click it to learn how to play.");
        addInstruction();

        tutorialButton = new Button("Tutorial");
        tutorialButton.setId("Tutorial");
        this.adventureGameView.customizeButton(tutorialButton, 200, 50);
        tutorialButton.setBackground(background);
        AdventureGameView.makeButtonAccessible(tutorialButton, "Tutorial Button", "This button allows you to play a tutorial of the game", "This button allows you to play a tutorial of the game.Click to play a tutorial");
        addTutorialEvent();


        settingsButton = new Button("Settings");
        settingsButton.setId("Accessibilty");
        this.adventureGameView.customizeButton(settingsButton, 200, 50);
        settingsButton.setBackground(background);
        AdventureGameView.makeButtonAccessible(settingsButton, "Settings", "This button allows you to view the settings of the game", "This button allows you to view the settings of the game.Click to view the settings");
        //addAccessibilityInfoEvent();

        settingsButton.setOnAction(e -> {
            adventureGameView.openSettingsWindow();
        });

        vbox1.getChildren().addAll(helpButton, tutorialButton, settingsButton, scoreButton);
        vbox1.setSpacing(10);
        gridPaneSP.add(vbox1, 0, 2, 2, 2);


        // play
        playButton = new Button("Play");
        scoreButton.setId("play");
        this.adventureGameView.customizeButton(playButton, 200, 50);
        playButton.setBackground(background);
        AdventureGameView.makeButtonAccessible(playButton, "Play", "This button starts the game.", "This button starts the game. Click it to play the game");
        addplayEvent();
        HBox hbox3 = new HBox();
        HBox.setHgrow(playButton, Priority.ALWAYS);
        hbox3.setBorder(new Border(new BorderStroke(Color.TRANSPARENT,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(25))));
        hbox3.getChildren().addAll(playButton);
        hbox3.setAlignment(Pos.BOTTOM_RIGHT);
        gridPaneSP.add(hbox3, 3, 3, 1, 1);


        Text text = new Text("ADVENTURE GAME");
        text.setFont(Font.font("Verdana", FontWeight.BOLD, 90));
        text.setFill(Color.BLACK); // Sets the text color to blue
        text.setStyle("-fx-stroke: white; -fx-stroke-width: 1;");


        gridPaneSP.add(text, 1, 0, 1, 2);

        colorAdjust = new ColorAdjust();

        for (Node node : scene.getRoot().getChildrenUnmodifiable()) {
            node.setEffect(colorAdjust);
        }

        this.adventureGameView.builder.addColorAdjust(colorAdjust);
    }

    /**
     * Initialize the UI for registration page
     */
    public void intiReg() {

        Image backgroundImage = new Image("file:Games/Images/reg_bgimg.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(1200, 700, true, true, false, true);
        BackgroundImage bgImage = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize);

        // Set the background of BorderPane
        gridpaneRP.setBackground(new Background(bgImage));
        ColumnConstraints column1 = new ColumnConstraints(300);
        ColumnConstraints column2 = new ColumnConstraints(600);
        ColumnConstraints column3 = new ColumnConstraints(300);
        RowConstraints row1 = new RowConstraints(500);
        RowConstraints row2 = new RowConstraints(100);
        RowConstraints row3 = new RowConstraints(100);
        gridpaneRP.getColumnConstraints().addAll(column1, column2, column3);
        gridpaneRP.getRowConstraints().addAll(row1, row2, row3);

        Label nameLabel = new Label("Enter Your Name:");
        nameLabel.setStyle("-fx-text-fill: black;");
        nameLabel.setFont(new Font("Arial", 16));

        nameInput = new TextField();
        nameInput.setFont(new Font("Arial", 16));
        nameInput.setFocusTraversable(true);

        VBox textEntry = new VBox();
        textEntry.setStyle("-fx-background-color: Transparent;");
        textEntry.setPadding(new Insets(20, 20, 20, 20));
        textEntry.getChildren().addAll(nameLabel, nameInput);
        textEntry.setSpacing(10);
        textEntry.setAlignment(Pos.CENTER);
        gridpaneRP.add(textEntry, 1, 0, 1, 1);
        addNameInputHandlingEvent();


        //continue Button
        continueButton = new Button("Continue");
        continueButton.setId("Continue");
        this.adventureGameView.customizeButton(continueButton, 220, 50);
        continueButton.setBackground(background);
        AdventureGameView.makeButtonAccessible(continueButton, "Continue Button", "This button allows you to continue to the game after entering your name", "This button allows you to continue to the game. Click here to continue after entering your name");
        addContinueEvent();
        gridpaneRP.add(continueButton, 2, 2, 1, 1);


        var scene = new Scene(gridpaneRP, 1200, 800);
        this.adventureGameView.stage.setScene(scene);
        this.adventureGameView.stage.setResizable(true);
        this.adventureGameView.stage.show();

    }

    /**
     *  This method handles the event related to the continue button.
     */
    public void addContinueEvent() {
        continueButton.setOnAction(e -> {
            String s = nameInput.getText().trim();
            if (!s.isEmpty()) {
                this.adventureGameView.model.player.setName(s);
                nameInput.clear();
                this.adventureGameView.intiUI();
            } else {
                Text printError = new Text("Enter your name first!");
                printError.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
                VBox textEntry = new VBox();
                textEntry.setStyle("-fx-background-color: Transparent;");
                textEntry.setPadding(new Insets(20, 20, 20, 20));
                textEntry.getChildren().add(printError);
                textEntry.setSpacing(10);
                textEntry.setAlignment(Pos.CENTER);
                gridpaneRP.add(textEntry, 1, 1, 1, 1);
                gridpaneRP.requestFocus();
            }

        });
    }

    /**
     * addNameInputHandlingEvent
     * __________________________
     * Adds an event handler to the nameInput attribute that takes the name of the player as input
     *
     */
    public void addNameInputHandlingEvent() {
        nameInput.setOnKeyPressed(event -> {
            this.gridpaneRP.getChildren().removeIf(n -> GridPane.getColumnIndex(n) == 2 && GridPane.getRowIndex(n) == 1);
            if (event.getCode().equals(KeyCode.ENTER)) {
                String s = nameInput.getText().trim();
                if (!s.isEmpty()) {
                    this.adventureGameView.model.player.setName(s);
                    nameInput.clear();
                    this.adventureGameView.intiUI();
                } else {
                    Text printError = new Text("Enter your name first!");
                    printError.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
                    VBox textEntry = new VBox();
                    textEntry.setStyle("-fx-background-color: Transparent;");
                    textEntry.setPadding(new Insets(20, 20, 20, 20));
                    textEntry.getChildren().add(printError);
                    textEntry.setSpacing(10);
                    textEntry.setAlignment(Pos.CENTER);
                    gridpaneRP.add(textEntry, 1, 1, 1, 1);
                    gridpaneRP.requestFocus();
                }

            } else if (event.getCode().equals(KeyCode.TAB)) {
                this.gridpaneRP.requestFocus();
            }
        });
    }
    /**
     *  This method handles the event related to the Score button.
     */
    public void addScoreEvent() {
        scoreButton.setOnAction(event -> {
            this.gridPaneSP.requestFocus();
            this.adventureGameView.scoreController.showScore();
        });
    }

    /**
     * This method handles the event related to the
     * play button on the start page
     */
    public void addplayEvent() {
        playButton.setOnAction(e -> {
            intiReg();
        });

    }
    /**
     *  This method handles the event related to the tutorial button.
     */
    public void addTutorialEvent() {
        tutorialButton.setOnAction(e -> {
            this.tutorialGameView.intiUI();
        });
    }
    /**
     * This method handles the event related to the
     * instruction button on the start page
     */
    private  void  addInstruction(){
        helpButton.setOnAction(e -> {
            InstructionsforStartPage();
        });
    }
    /*
     * Shows the game instructions for the start page.
     */
    public void InstructionsforStartPage() {
        if (this.adventureGameView.settingsToggle) {
            this.gridPaneSP.getChildren().removeIf(n -> GridPane.getColumnIndex(n) == 2 && GridPane.getRowIndex(n) == 2);
            this.adventureGameView.settingsToggle = false;
        } else {
            Label help_text = new Label(this.adventureGameView.model.getInstructions());
            help_text.setStyle("-fx-background: #000000; -fx-background-color:#412b15;-fx-text-fill: white");
            help_text.setWrapText(true);
            help_text.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
            gridPaneSP.add(help_text, 2, 2);
            this.adventureGameView.settingsToggle = true;
        }

    }

}
