package de.pcps.jamtugether.model.music.soundpool;

import android.content.Context;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.music.soundpool.base.BaseSoundPool;

public class FluteSoundPool extends BaseSoundPool {

    public FluteSoundPool(@NonNull Context context) {
        super(context, 1, R.raw.flute_sound);
    }

    @Override
    public int play(int soundID, float pitch, int length) {
        return soundPool.play(soundID, volume, volume, 1, 99, pitch);
    }
}
