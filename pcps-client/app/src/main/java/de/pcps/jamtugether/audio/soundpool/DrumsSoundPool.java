package de.pcps.jamtugether.audio.soundpool;

import android.content.Context;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.drums.Drums;
import de.pcps.jamtugether.audio.soundpool.base.BaseSoundPool;
import timber.log.Timber;

public class DrumsSoundPool extends BaseSoundPool {

    /*
     this is necessary in order to play a lot of
     sounds in a short period of time because these sounds
     start before another one ends
     */;
    private static final int SOUND_POOL_MAX_STREAMS = 100;

    public DrumsSoundPool(@NonNull Context context) {
        super(context, SOUND_POOL_MAX_STREAMS, Drums.SNARE, Drums.KICK, Drums.HAT, Drums.CYMBAL);
    }

    @Override
    public int play(int soundID, float pitch) {
        return soundPool.play(soundID, volume, volume, 0, 0, calculatePitch((int) pitch));
    }

    @Override
    public float calculatePitch(int pitchPercentage) {
        return 1;
    }
}