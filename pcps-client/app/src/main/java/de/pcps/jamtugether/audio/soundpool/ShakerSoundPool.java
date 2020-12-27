package de.pcps.jamtugether.audio.soundpool;

import android.content.Context;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.soundpool.base.BaseSoundPool;

// todo
public class ShakerSoundPool extends BaseSoundPool {

    public ShakerSoundPool(@NonNull Context context) {
        super(context, 1, R.raw.flute_sound);
    }

    @Override
    public int play(int soundID, float pitch, int length) {
        return 0;
    }
}
