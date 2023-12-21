package Settings;

import javafx.scene.effect.ColorAdjust;

import java.util.ArrayList;

/**
 * HueSetting
 * a class that implements SettingsStrategy to be able to
 * implement hue changes in the AdventureGameView
 * through the Settings Window
 *
 */
public class HueSetting implements SettingsStrategy {
    private ArrayList<ColorAdjust> colorAdjusts;

    /**
     * HueSetting
     * constructor method
     *
     * @param colorAdjusts a collection of ColorAdjust in the game
     */
    public HueSetting(ArrayList<ColorAdjust>  colorAdjusts) {
        this.colorAdjusts = colorAdjusts;
    }

    /**
     * applySetting
     * ---
     * overrides interface SettingsStrategy
     * ---
     * a method that applies the change to the hue
     *
     * @param hueLevel the amount that you want to change hue
     */
    @Override
    public void applySetting(double hueLevel) {
        if (this.colorAdjusts != null) {
            for (ColorAdjust color : this.colorAdjusts) {
                color.setHue(hueLevel);
            }
        } else {
            System.out.println("ColorAdjust is not initialized. Cannot apply hue setting.");
        }
        System.out.println("Applying contrast setting to: " + hueLevel);
    }
}
