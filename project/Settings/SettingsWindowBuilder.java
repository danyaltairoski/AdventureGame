package Settings;

import AdventureModel.Audio;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * SettingsWindowBuilder
 * a class to build the Settings Window
 * has a stage, and communicates with the Settings Strategy
 *
 */
public class SettingsWindowBuilder {
    private static SettingsWindowBuilder instance = new SettingsWindowBuilder();
    private Stage settingsStage;
    private Slider volumeSlider, contrastSlider, hueSlider, brightnessSlider, saturationSlider;
    private SettingsStrategy settingsStrategy;
    private ArrayList<Audio> Audios;
    private ArrayList<ColorAdjust> colorAdjusts;
    private TextArea accessibilityInfoTextArea;

    private SettingsWindowBuilder() {
        this.Audios = new ArrayList<>();
        this.colorAdjusts =  new ArrayList<>();
    }

    public static SettingsWindowBuilder getInstance() {
        return instance;
    }

    public SettingsWindowBuilder setSettingsStrategy(SettingsStrategy strategy) {
        this.settingsStrategy = strategy;
        return this;
    }

    /**
     * onSpeechRecognized
     * a method to manipulate the input from Speech-To-Text function, so that it can
     * be used by another class
     *
     * @param settingType the desired setting that you want to
     */
    private SettingsStrategy getSettingsStrategy(String settingType) {
        return switch (settingType) {
            case "Volume" -> new VolumeSetting(this.Audios);
            case "Contrast" -> new ContrastSetting(this.colorAdjusts);
            case "Hue" -> new HueSetting(this.colorAdjusts);
            case "Saturation" -> new SaturationSetting(this.colorAdjusts);
            case "Brightness" -> new BrightnessSetting(this.colorAdjusts);
            default -> throw new IllegalArgumentException("Invalid setting type: " + settingType);
        };
    }

    public void addAudio(Audio audio) {
        this.Audios.add(audio);
    }

    public void addColorAdjust(ColorAdjust colorAdjust) {
        this.colorAdjusts.add(colorAdjust);
    }

    private Slider createSlider(double min, double max, double initialValue) {
        Slider newSlider = new Slider(min, max, initialValue);
        newSlider.setVisible(false); // Initially hide the slider
        return newSlider;
    }

    private void showSliderForSetting(String settingType) {
        // Hide all sliders
        volumeSlider.setVisible(false);
        contrastSlider.setVisible(false);
        hueSlider.setVisible(false);
        saturationSlider.setVisible(false);
        brightnessSlider.setVisible(false);

        // Show the appropriate slider based on the selected setting
        switch (settingType) {
            case "Volume" -> volumeSlider.setVisible(true);
            case "Contrast" -> contrastSlider.setVisible(true);
            case "Hue" -> hueSlider.setVisible(true);
            case "Saturation" -> saturationSlider.setVisible(true);
            case "Brightness" -> brightnessSlider.setVisible(true);
            default -> throw new IllegalArgumentException("Invalid setting type: " + settingType);
        }
    }

    public SettingsWindowBuilder buildSettingsWindow() {
        settingsStage = new Stage();
        settingsStage.initModality(Modality.APPLICATION_MODAL);
        settingsStage.setTitle("Settings");

        // Add UI elements for settings
        ComboBox<String> settingTypeComboBox = new ComboBox<>();
        settingTypeComboBox.getItems().addAll("Volume", "Contrast", "Hue", "Saturation", "Brightness");
        settingTypeComboBox.setOnAction(e -> {
            String selectedSetting = settingTypeComboBox.getValue();
            setSettingsStrategy(getSettingsStrategy(selectedSetting));
            showSliderForSetting(selectedSetting);
        });

        // Use separate Sliders for different settings
        volumeSlider = createSlider(0.0, 1.0, 0.5);
        contrastSlider = createSlider(-1.0, 1.0, 0);
        hueSlider = createSlider(-1.0, 1.0, 0);
        saturationSlider = createSlider(-1.0, 1.0, 0);
        brightnessSlider = createSlider(-1.0, 1.0, 0);

        // Add labels for each slider
        Label volumeLabel = new Label("Volume:");
        Label contrastLabel = new Label("Contrast:");
        Label hueLabel = new Label("Hue:");
        Label saturationLabel = new Label("Saturation:");
        Label brightnessLabel = new Label("Brightness:");

        Button accessibilityButton = new Button("Accessibility Information");
        accessibilityButton.setOnAction(e -> showAccessibilityInformation());

        // Create a TextArea for accessibility information
        accessibilityInfoTextArea = new TextArea();
        accessibilityInfoTextArea.setEditable(false);
        accessibilityInfoTextArea.setWrapText(true);
        accessibilityInfoTextArea.setVisible(false);

        // Add the ComboBox, labels, and relevant Sliders to the layout
        VBox settingsLayout = new VBox(10,
                settingTypeComboBox,
                new HBox(volumeLabel, volumeSlider),
                new HBox(contrastLabel, contrastSlider),
                new HBox(hueLabel, hueSlider),
                new HBox(saturationLabel, saturationSlider),
                new HBox(brightnessLabel, brightnessSlider),
                accessibilityButton,
                accessibilityInfoTextArea
        );
        settingsLayout.setStyle("-fx-padding: 20px;");

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            if (settingsStrategy != null) {
                if (settingsStrategy instanceof VolumeSetting) {
                    ((VolumeSetting) settingsStrategy).applySetting(volumeSlider.getValue());
                } else if (settingsStrategy instanceof ContrastSetting) {
                    ((ContrastSetting) settingsStrategy).applySetting(contrastSlider.getValue());
                } else if (settingsStrategy instanceof HueSetting) {
                    ((HueSetting) settingsStrategy).applySetting(hueSlider.getValue());
                } else if (settingsStrategy instanceof SaturationSetting) {
                    ((SaturationSetting) settingsStrategy).applySetting(saturationSlider.getValue());
                } else if (settingsStrategy instanceof BrightnessSetting) {
                    ((BrightnessSetting) settingsStrategy).applySetting(brightnessSlider.getValue());
                }
            }

            settingsStage.close();
        });

        settingsLayout.getChildren().add(saveButton);

        // Set a larger width and height for the scene
        Scene settingsScene = new Scene(settingsLayout, 500, 500);

        settingsStage.setScene(settingsScene);
        return this;
    }

    private void showAccessibilityInformation() {
        // Replace the text below with the actual accessibility information
        String accessibilityInfoText = "This game provides the following accessibility features:\n\n" +
                "- High contrast settings\n" +
                "- Adjustable volume control\n" +
                "- Hue, saturation, and brightness adjustments\n" +
                "- Speech-to-Text Input \n\n" +
                "How to use Speech-to-Text: \n" +
                "1. Press the microphone button beside the input text field to speak!\n" +
                "2. Wait about 2 seconds, ... it will automatically input your command!\n" +
                "3. Press the stop Button if you would like to stop recording your input.\n\n" +
                "Feel free to customize the settings to enhance your gaming experience.";

        accessibilityInfoTextArea.setText(accessibilityInfoText);
        accessibilityInfoTextArea.setVisible(true);
    }

    public Stage showAndWait() {
        settingsStage.showAndWait();
        return settingsStage;
    }
}
