package de.pcps.jamtugether.model.instrument;

import android.content.Context;
import android.media.SoundPool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.instrument.base.Instrument;
import timber.log.Timber;

public class Drums extends Instrument {

    @Nullable
    private static Drums instance;

    private SoundPool soundPool;

    private int snareSound;
    private int kickSound;
    private int hatSound;
    private int cymbalSound;

    private boolean snareSoundLoaded;
    private boolean kickSoundLoaded;
    private boolean hatSoundLoaded;
    private boolean cymbalSoundLoaded;

    public Drums() {
        super(1, R.string.instrument_drums, R.string.play_drums_help, "drums", "drums");
    }

    public void prepare(@NonNull Context context) {
        soundPool = new SoundPool.Builder().setMaxStreams(4).build();

        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            if(sampleId == snareSound) {
                snareSoundLoaded = true;
            } else if(sampleId == kickSound) {
                kickSoundLoaded = true;
            } else if(sampleId == hatSound) {
                hatSoundLoaded = true;
            } else if(sampleId == cymbalSound) {
                cymbalSoundLoaded = true;
            }
        });

        snareSound = soundPool.load(context, R.raw.drum_snare, 1);
        kickSound = soundPool.load(context, R.raw.drum_kick, 1);
        hatSound = soundPool.load(context, R.raw.drum_hat, 1);
        cymbalSound = soundPool.load(context, R.raw.drum_cymbal, 1);
    }

    public void playSnare() {
        if(snareSoundLoaded) {
            playSound(snareSound);
        } else {
            Timber.w("snare sound not loaded yet");
        }
    }

    public void playKick() {
        if(kickSoundLoaded) {
            playSound(kickSound);
        } else {
            Timber.w("kick sound not loaded yet");
        }
    }

    public void playHat() {
        if(hatSoundLoaded) {
            playSound(hatSound);
        } else {
            Timber.w("hat sound not loaded yet");
        }
    }

    public void playCymbal() {
        if(cymbalSoundLoaded) {
            playSound(cymbalSound);
        } else {
            Timber.w("cymbal sound not loaded yet");
        }
    }

    private void playSound(int sound) {
        soundPool.play(sound, 1.0f, 1.0f, 0, 0, 10f);
    }

    @NonNull
    public static Drums getInstance() {
        if (instance == null) {
            instance = new Drums();
        }
        return instance;
    }

}
