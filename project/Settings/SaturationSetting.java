package Settings;

import javafx.scene.effect.ColorAdjust;

import java.util.ArrayList;

/**
 * SaturationSetting
 * a class that implements SettingsStrategy to be able to
 * implement saturation changes in the AdventureGameView
 * through the Settings Window
 *
 */
public class SaturationSetting implements SettingsStrategy {
    private ArrayList<ColorAdjust> colorAdjusts;

    /**
     * SaturationSetting
     * constructor method
     *
     * @param colorAdjusts a collection of ColorAdjust in the game
     */
    public SaturationSetting(ArrayList<ColorAdjust>  colorAdjusts) {
        this.colorAdjusts = colorAdjusts;
    }

    /**
     * applySetting
     * ---
     * overrides interface SettingsStrategy
     * ---
     * a method that applies the change to the saturation
     *
     * @param saturationLevel the amount that you want to change saturation
     */
    @Override
    public void applySetting(double saturationLevel) {
        if (this.colorAdjusts != null) {
            for (ColorAdjust color : this.colorAdjusts) {
                color.setSaturation(saturationLevel);
            }
        } else {
            System.out.println("ColorAdjust is not initialized. Cannot apply saturation setting.");
        }
        System.out.println("Applying contrast setting to: " + saturationLevel);
    }
}
