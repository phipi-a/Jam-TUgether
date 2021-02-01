package de.pcps.jamtugether.audio.instrument.piano;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.sound.OnSoundPlayedCallback;
import de.pcps.jamtugether.audio.sound.BaseSoundPool;

public class Piano extends Instrument {

    public static final int MIN_PITCH = 0;
    public static final int MAX_PITCH = 100;
    public static final int PITCH_RANGE = MAX_PITCH - MIN_PITCH;

    @Nullable
    private static Piano instance;

    public Piano() {
        super(3, R.string.instrument_piano, "piano", "piano");
    }

    @Override
    public int getSoundResource(int pitch) {
        return PianoSound.from(pitch).getResource();
    }

    @NonNull
    @Override
    public BaseSoundPool createSoundPool(@NonNull Context context) {
        return new PianoSoundPool(context);
    }

    @Override
    public boolean soundsNeedToBeStopped() {
        return true;
    }

    @Override
    public boolean soundsNeedToBeResumed() {
        return true;
    }

    public void play(int pitch, @NonNull OnSoundPlayedCallback callback) {
        if (soundPool != null) {
            soundPool.playSoundRes(getSoundResource(pitch), callback);
        } else {
            callback.onSoundPlayed(0);
        }
    }

    public void stopSound(int streamID) {
        if (soundPool != null) {
            soundPool.stopSound(streamID);
        }
    }

    @NonNull
    public static Piano getInstance() {
        if (instance == null) {
            instance = new Piano();
        }
        return instance;
    }

    public interface OnKeyListener {
        void onKeyPressed(int pitch);

        void onKeyReleased(int pitch);
    }
}
