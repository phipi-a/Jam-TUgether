package de.pcps.jamtugether.audio.instrument.drums;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.base.Instrument;

import de.pcps.jamtugether.audio.sound.pool.DrumsSoundPool;
import de.pcps.jamtugether.model.sound.SoundResource;
import de.pcps.jamtugether.audio.sound.pool.base.BaseSoundPool;

public class Drums extends Instrument {

    @Nullable
    private static Drums instance;

    @RawRes
    public static final int SNARE = SoundResource.SNARE.getResource();
    public static final int SNARE_PITCH = 50;

    @RawRes
    public static final int KICK = SoundResource.KICK.getResource();
    public static final int KICK_PITCH = 30;

    @RawRes
    public static final int HAT = SoundResource.HAT.getResource();
    public static final int HAT_PITCH = 70;

    @RawRes
    public static final int CYMBAL = SoundResource.CYMBAL.getResource();
    public static final int CYMBAL_PITCH = 90;

    public Drums() {
        super(1, R.string.instrument_drums, R.string.play_drums_help, "drums", "drums");
    }

    @RawRes
    @Override
    public int getSoundResource(int pitch) {
        switch (pitch) {
            case SNARE_PITCH:
                return SNARE;
            case KICK_PITCH:
                return KICK;
            case HAT_PITCH:
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

    @Override
    public boolean soundsNeedToBeStopped() {
        return false;
    }

    @Override
    public boolean soundsNeedToBeResumed() {
        return false;
    }

    public void playSnare() {
        if (soundPool != null) {
            soundPool.playSoundRes(SNARE, 1);
        }
    }

    public void playKick() {
        if (soundPool != null) {
            soundPool.playSoundRes(KICK, 1);
        }
    }

    public void playHat() {
        if (soundPool != null) {
            soundPool.playSoundRes(HAT, 1);
        }
    }

    public void playCymbal() {
        if (soundPool != null) {
            soundPool.playSoundRes(CYMBAL, 1);
        }
    }

    @NonNull
    public static Drums getInstance() {
        if (instance == null) {
            instance = new Drums();
        }
        return instance;
    }
}
