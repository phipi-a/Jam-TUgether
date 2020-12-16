package de.pcps.jamtugether.model.instrument;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.instrument.base.Instrument;
import de.pcps.jamtugether.ui.room.music.instrument.drums.DrumsViewModel;

import static java.security.AccessController.getContext;

// todo
public class Drums extends Instrument {

    private SoundPool soundPool;
    private int kick;
    private int cymbal;
    private int snare;
    private int hat;



    @Nullable
    private static Drums instance;

    public Drums() {
        super(1, R.string.instrument_drums, R.string.play_drums_help, "drums", "drums");
    }

    public void playSnare(){
        playSound(snare);
    }
    public void playHat(){
        playSound(hat);
    }
    public void playCymbal(){
        playSound(cymbal);
    }
    public void playKick(){
        playSound(kick);
    }


    public void prepare(@NonNull Context context){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(4)
                    .setAudioAttributes(audioAttributes)
                    .build();

        //soundPool = new SoundPool.Builder().setMaxStreams(4).build();
        hat=soundPool.load(context,R.raw.drum_hat,1);
        cymbal=soundPool.load(context,R.raw.drum_cymbal,1);
        kick=soundPool.load(context,R.raw.drum_kick,1);
        snare=soundPool.load(context,R.raw.drum_snare,1);
    }

    private void playSound(int sound){

        soundPool.play(sound,1,1,0,0,1);
    }

    @NonNull
    public static Drums getInstance() {
        if(instance == null) {
            instance = new Drums();
        }
        return instance;
    }

}
