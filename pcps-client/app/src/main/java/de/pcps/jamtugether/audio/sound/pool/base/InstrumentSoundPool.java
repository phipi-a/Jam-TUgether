package de.pcps.jamtugether.audio.sound.pool.base;

import android.content.Context;

import androidx.annotation.NonNull;

public abstract class InstrumentSoundPool extends BaseSoundPool {

    public InstrumentSoundPool(@NonNull Context context, int maxStreams, @NonNull Integer... soundResIDs) {
        super(context, maxStreams, soundResIDs);
    }

    public abstract float calculatePitch(int pitchPercentage);

}
