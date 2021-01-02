package de.pcps.jamtugether.audio.sound.pool;

import android.content.Context;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.audio.instrument.flute.Flute;
import de.pcps.jamtugether.audio.sound.pool.base.BaseSoundPool;

public class FluteSoundPool extends BaseSoundPool {

    public FluteSoundPool(@NonNull Context context) {
        super(context, 1, Flute.FLUTE_SOUND);
    }

    @Override
    public int play(int soundID, float pitch) {
        return soundPool.play(soundID, volume, volume, 0, 0, calculatePitch((int) pitch));
    }

    @Override
    public float calculatePitch(int pitchPercentage) {
        return pitchPercentage / 100.0f * Flute.PITCH_MULTIPLIER;
    }
}
