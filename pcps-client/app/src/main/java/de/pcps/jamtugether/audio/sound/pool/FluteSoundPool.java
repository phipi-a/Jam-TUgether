package de.pcps.jamtugether.audio.sound.pool;

import android.content.Context;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.audio.sound.pool.base.BaseSoundPool;
import de.pcps.jamtugether.model.sound.flute.FluteSound;

public class FluteSoundPool extends BaseSoundPool {

    public FluteSoundPool(@NonNull Context context) {
        super(context, 1, FluteSound.values());
    }
}
