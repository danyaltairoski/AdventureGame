package Settings;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;

/**
 * ContrastSetting
 * a class that implements SettingsStrategy to be able to
 * implement contrast changes in the AdventureGameView
 * through the Settings Window
 *
 */
public class ContrastSetting implements SettingsStrategy {
    private ArrayList<ColorAdjust> colorAdjusts;

    /**
     * ContrastSetting
     * constructor method
     *
     * @param colorAdjusts a collection of ColorAdjust in the game
     */
    public ContrastSetting(ArrayList<ColorAdjust>  colorAdjusts) {
        this.colorAdjusts = colorAdjusts;
    }

    /**
     * applySetting
     * ---
     * overrides interface SettingsStrategy
     * ---
     * a method that applies the change to the contrast
     *
     * @param contrastLevel the amount that you want to change contrast
     */
    @Override
    public void applySetting(double contrastLevel) {
        if (this.colorAdjusts != null) {
            for (ColorAdjust color : this.colorAdjusts) {
                color.setContrast(contrastLevel);
            }
        } else {
            System.out.println("ColorAdjust is not initialized. Cannot apply contrast setting.");
        }
        System.out.println("Applying contrast setting to: " + contrastLevel);
    }
}
