package AdventureModel;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/*
* Class to play the audio in the game
 */
public class Audio {
    String[] musicFile = new String[22];

    boolean mediaPlaying;

    Clip clip;

    public int cooldown = 0;
    private Map<String, Double> volumeSettings = new HashMap<>();

    public Audio() {
        musicFile[0] = "Resources/Audio/correctkey.wav";
        musicFile[1] = "Resources/Audio/startingroom.wav";
        musicFile[2] = "Resources/Audio/corridor.wav";
        musicFile[3] = "Resources/Audio/insidebuilding.wav";
        musicFile[4] = "Resources/Audio/keyroom.wav";
        musicFile[5] = "Resources/Audio/boss1chamber.wav";
        musicFile[6] = "Resources/Audio/keychamber.wav";
        musicFile[7] = "Resources/Audio/gunroom.wav";
        musicFile[8] = "Resources/Audio/boss2lair.wav";
        musicFile[9] = "Resources/Audio/boss3arena.wav";
        musicFile[10] = "Resources/Audio/exit.wav";
        musicFile[11] = "Resources/Audio/wasd.wav";
        musicFile[12] = "Resources/Audio/keytut.wav";
        musicFile[13] = "Resources/Audio/minitroll.wav";
        musicFile[14] = "Resources/Audio/supertroll.wav";
        musicFile[15] = "Resources/Audio/kingtroll.wav";
        musicFile[16] = "Resources/Audio/finish.wav";
        musicFile[17] = "Resources/Audio/defeatmonster.wav";
        musicFile[18] = "Resources/Audio/drinking.wav";
    }

    public void play(int i){
        cooldown = 0;
        try {
            String currentMusicFile = musicFile[i];
            File file = new File(musicFile[i]);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            if (volumeSettings.containsKey(currentMusicFile)) {
                setVolume(volumeSettings.get(currentMusicFile));
            } else {
                setVolume(0.5);
            }

            clip.start();
            mediaPlaying = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (mediaPlaying) {
            clip.stop();
            mediaPlaying = false;
        }
    }

    /**
     * setVolume
     * set the volume of the audio system
     *
     * @param input amount that you want to change volume (range is 0 to 1)
     */
    public void setVolume(double input) {
        if (clip != null) {
            try {
                FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

                float volume = (float) (Math.log(input) / Math.log(10.0) * 20.0);

                control.setValue(volume);

                for (String file : musicFile) {
                    volumeSettings.put(file, input); //for every file, set the desired volume level
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
