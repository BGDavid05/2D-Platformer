package audio;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {

    public static int MENU_1 = 0;
    public static int LEVEL_1 = 1;

    public static int JUMP = 1;
    public static int LVL_COMPLETED = 3;

    private Clip[] songs, effects;
    private int currentSongId;
    private float volume = 1f;
    private boolean songMute, effectMute;

    private static final String SETTINGS_FILE = "C://TheBoy/audio_settings.properties";

    public AudioPlayer() {
        loadSongs();
        // loadEffects();
        loadVolumeSetting();
        playSong(MENU_1);
    }

    private void loadSongs() {
        String[] names = {"menu", "level1"};
        songs = new Clip[names.length];
        for (int i = 0; i < songs.length; i++)
            songs[i] = getClip(names[i]);
    }

    private void loadVolumeSetting() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(SETTINGS_FILE)) {
            properties.load(input);
            volume = Float.parseFloat(properties.getProperty("volume", "1.0"));
            System.out.println("Volume set to " + volume);
        } catch (IOException | NumberFormatException e) {
            System.out.println("No previous volume setting found, using default.");
        }
    }

    private void saveVolumeSetting() {
        Properties properties = new Properties();
        try (OutputStream output = new FileOutputStream(SETTINGS_FILE)) {
            properties.setProperty("volume", String.valueOf(volume));
            properties.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadEffects() {
        String[] effectNames = {};
        effects = new Clip[effectNames.length];
        for (int i = 0; i < effects.length; i++)
            effects[i] = getClip(effectNames[i]);

        updateEffectsVolume();

    }

    private Clip getClip(String name) {
        URL url = getClass().getResource("/audio/" + name + ".wav");
        AudioInputStream audio;

        try {
            audio = AudioSystem.getAudioInputStream(url);
            Clip c = AudioSystem.getClip();
            c.open(audio);
            return c;

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {

            e.printStackTrace();
        }

        return null;

    }

    public void setVolume(float volume) {
        this.volume = volume;
        updateSongVolume();
        //updateEffectsVolume();
        saveVolumeSetting();
    }

    public void stopSong() {
        if (songs[currentSongId].isActive())
            songs[currentSongId].stop();
    }

    public void lvlCompleted() {
        stopSong();
        playEffect(LVL_COMPLETED);
    }

    public void playEffect(int effect) {
        effects[effect].setMicrosecondPosition(0);
        effects[effect].start();
    }

    public void playSong(int song) {
        stopSong();

        currentSongId = song;
        updateSongVolume();
        songs[currentSongId].setMicrosecondPosition(0);
        songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void toggleSongMute() {
        this.songMute = !songMute;
        for (Clip c : songs) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(songMute);
        }
    }

    public void toggleEffectMute() {
        this.effectMute = !effectMute;
        for (Clip c : effects) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(effectMute);
        }
        if (!effectMute)
            playEffect(JUMP);
    }

    private void updateSongVolume() {

        FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);

    }

    private void updateEffectsVolume() {
        for (Clip c : effects) {
            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }

}