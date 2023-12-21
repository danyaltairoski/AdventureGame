package Settings;

import javafx.scene.effect.ColorAdjust;

import java.util.ArrayList;

/**
 * BrightnessSetting
 * a class that implements SettingsStrategy to be able to
 * implement brightness changes in the AdventureGameView
 * through the Settings Window
 *
 */
public class BrightnessSetting implements SettingsStrategy {
    private ArrayList<ColorAdjust> colorAdjusts;

    /**
     * BrightnessSetting
     * constructor method
     *
     * @param colorAdjusts a collection of ColorAdjust in the game
     */
    public BrightnessSetting(ArrayList<ColorAdjust>  colorAdjusts) {
        this.colorAdjusts = colorAdjusts;
    }

    /**
     * applySetting
     * ---
     * overrides interface SettingsStrategy
     * ---
     * a method that applies the change to the brightness
     *
     * @param brightnessLevel the amount that you want to change brightness
     */
    @Override
    public void applySetting(double brightnessLevel) {
        if (this.colorAdjusts != null) {
            for (ColorAdjust color : this.colorAdjusts) {
                color.setBrightness(brightnessLevel);
            }
        } else {
            System.out.println("ColorAdjust is not initialized. Cannot apply brightness setting.");
        }
        System.out.println("Applying contrast setting to: " + brightnessLevel);
    }
}
