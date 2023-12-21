package Settings;

import AdventureModel.Audio;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
/**
 * VolumeSetting
 * a class that implements SettingsStrategy to be able to
 * implement volume changes in the AdventureGameView
 * through the Settings Window
 *
 */
class VolumeSetting implements SettingsStrategy {
    private ArrayList<Audio> Audios;

    /**
     * VolumeSetting
     * constructor method
     *
     * @param Audios a collection of Audios in the game
     */
    public VolumeSetting(ArrayList<Audio> Audios) {
        this.Audios = Audios;
    }

    /**
     * applySetting
     * ---
     * overrides interface SettingsStrategy
     * ---
     * a method that applies the change to the volume
     *
     * @param volumeLevel the amount that you want to change volume
     */
    @Override
    public void applySetting(double volumeLevel) {
        System.out.println("Adjusting volume setting to " + volumeLevel);

        if (this.Audios != null) {
            for (Audio audio : this.Audios) {
                audio.setVolume(volumeLevel);
            }
        } else {
            System.out.println("Audio is not initialized. Cannot apply volume setting.");
        }
    }

}
