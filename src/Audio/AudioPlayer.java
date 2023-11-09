package Audio;

import UI.Setting;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class AudioPlayer {
    public static int COUNTER = 0;
    public static int ATK1 = 1;
    public static int ATK2 = 2;
    public static int ATK3 = 3;
    public static int ATK4 = 4;
    public static int BLOCK1 = 5;
    public static int BLOCK2 = 6;
    public static int FOOTSTEP1 = 7;
    public static int BUFF = 8;
    public static int SELECT = 9;
    public static int ENTER = 10;
    public static int DASH = 11;
    public static int REVIVE = 12;
    public static int ENEMYATK1 = 13;
    public static int ENEMYRANGEATK1 = 14;
    public static int ENEMYRANGEATK2 = 15;
    public static int ENEMYCHARGE = 16;

    private static String[] name = {"counter","sword1","sword2","sword3","sword4","block","block2","footstep1","Buff","menuSelect","menuEnter","dash","revive","enemyATK1","enemyRangeATK1","enemyRangeATK2","enemyCharge"};
    private static Clip[] effect;
    private static float volume = 0.5f;
    public AudioPlayer(){
        loadAudio();
    }

    private void loadAudio(){
        effect = new Clip[name.length];
        for (int i=0;i< effect.length;i++){
            effect[i] = getClip(name[i]);
        }
    }

    private Clip getClip(String file){
        URL url = getClass().getResource("/Sound/"+ file + ".wav");
        AudioInputStream is = null;
        try {
            is = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(is);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public static void playEffect(int effectCalled){
        URL url = AudioPlayer.class.getResource("/Sound/"+ name[effectCalled] + ".wav");
        AudioInputStream is = null;
        try {
            is = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(is);
            setVolume();
            FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = control.getMaximum() - control.getMinimum();
            float gain = (range * volume) + control.getMinimum();
            control.setValue(gain);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setVolume(){
        volume = Setting.getSFXChoice();
    }
}
