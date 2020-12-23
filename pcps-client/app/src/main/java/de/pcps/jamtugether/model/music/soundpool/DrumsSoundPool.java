package de.pcps.jamtugether.model.music.soundpool;

import android.content.Context;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.music.soundpool.base.BaseSoundPool;

public class DrumsSoundPool extends BaseSoundPool {

    /*
     this is necessary in order to play a lot of
     sounds in a short period of time because these sounds
     start before another one ends
     */;
    private static final int SOUND_POOL_MAX_STREAMS = 100;

    public DrumsSoundPool(@NonNull Context context) {
        super(context, SOUND_POOL_MAX_STREAMS, R.raw.drum_snare, R.raw.drum_kick, R.raw.drum_hat, R.raw.drum_cymbal);
    }

    @Override
    public int play(int soundID, float pitch, int length) {
        return soundPool.play(soundID, volume, volume, 0, 0, 1);
    }
}
