package de.pcps.jamtugether.audio.instrument.drums;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.base.Instrument;

import de.pcps.jamtugether.audio.sound.pool.DrumsSoundPool;
import de.pcps.jamtugether.audio.sound.pool.base.BaseSoundPool;
import de.pcps.jamtugether.model.sound.drums.DrumsSound;

public class Drums extends Instrument {

    public static final int MIN_PITCH = 0;
    public static final int MAX_PITCH = 100;
    public static final int PITCH_RANGE = MAX_PITCH - MIN_PITCH;

    @Nullable
    private static Drums instance;

    public Drums() {
        super(1, R.string.instrument_drums, R.string.play_drums_help, "drums", "drums");
    }

    @RawRes
    @Override
    public int getSoundResource(int pitch) {
        return DrumsSound.from(pitch).getResource();
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
            soundPool.playSoundRes(DrumsSound.SNARE.getResource());
        }
    }

    public void playKick() {
        if (soundPool != null) {
            soundPool.playSoundRes(DrumsSound.KICK.getResource());
        }
    }

    public void playHat() {
        if (soundPool != null) {
            soundPool.playSoundRes(DrumsSound.HAT.getResource());
        }
    }

    public void playCymbal() {
        if (soundPool != null) {
            soundPool.playSoundRes(DrumsSound.CYMBAL.getResource());
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
