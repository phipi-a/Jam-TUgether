package de.pcps.jamtugether.audio.soundpool;

import android.content.Context;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.shaker.Shaker;
import de.pcps.jamtugether.audio.soundpool.base.BaseSoundPool;

// todo
public class ShakerSoundPool extends BaseSoundPool {

    public ShakerSoundPool(@NonNull Context context) {
        super(context, 1, Shaker.SHAKER_SOUND);
    }

    @Override
    public int play(int soundID, float pitch) {
        return 0;
    }

    @Override
    public float calculatePitch(int pitchPercentage) {
        return 0;
    }
}
