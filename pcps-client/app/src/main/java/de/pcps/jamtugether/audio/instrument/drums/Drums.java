package de.pcps.jamtugether.audio.instrument.drums;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.base.Instrument;

import de.pcps.jamtugether.audio.soundpool.DrumsSoundPool;
import de.pcps.jamtugether.audio.soundpool.base.BaseSoundPool;

public class Drums extends Instrument {

    @Nullable
    private static Drums instance;

    @RawRes
    private static final int SNARE = R.raw.drum_snare;

    @RawRes
    private static final int KICK = R.raw.drum_kick;

    @RawRes
    private static final int HAT = R.raw.drum_hat;

    @RawRes
    private static final int CYMBAL = R.raw.drum_cymbal;

    public Drums() {
        super(1, R.string.instrument_drums, R.string.play_drums_help, "drums", "drums");
    }

    @RawRes
    @Override
    public int getSoundResource(int element) {
        switch (element) {
            case 0:
                return SNARE;
            case 1:
                return KICK;
            case 2:
                return HAT;
            default:
                return CYMBAL;
        }
    }

    @NonNull
    @Override
    public BaseSoundPool createSoundPool(@NonNull Context context) {
        return new DrumsSoundPool(context);
    }

    public void playSnare() {
        soundPool.playSoundRes(SNARE, 1);
    }

    public void playKick() {
        soundPool.playSoundRes(KICK, 1);
    }

    public void playHat() {
        soundPool.playSoundRes(HAT, 1);
    }

    public void playCymbal() {
        soundPool.playSoundRes(CYMBAL, 1);
    }

    @NonNull
    public static Drums getInstance() {
        if (instance == null) {
            instance = new Drums();
        }
        return instance;
    }
}
