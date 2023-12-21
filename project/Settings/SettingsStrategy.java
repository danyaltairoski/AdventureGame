package Settings;

/**
 * SettingsStrategy
 * a interface to be able to implement various settings in a settings window
 *
 */
public interface SettingsStrategy {
    /**
     * applySetting
     * a method that applies a change to a desired setting
     *
     * @param value the amount that you want to change a setting by
     */
    void applySetting(double value);
}

