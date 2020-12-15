package de.pcps.jamtugether.model.instrument;

import android.content.Context;
import android.media.SoundPool;

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
    private DrumsViewModel viewModel;



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
        soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        snare=soundPool.load(context,R.raw.drum_snare,1);
        hat=soundPool.load(context,R.raw.drum_hat,1);
        kick=soundPool.load(context,R.raw.drum_kick,1);
        cymbal=soundPool.load(context,R.raw.drum_cymbal,1);
    }

    private void playSound(int sound){
        soundPool.play(sound,1.0f,1.0f,0,0,10f);
    }

    @NonNull
    public static Drums getInstance() {
        if(instance == null) {
            instance = new Drums();
        }
        return instance;
    }

}
