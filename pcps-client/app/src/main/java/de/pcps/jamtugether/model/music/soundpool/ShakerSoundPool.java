package de.pcps.jamtugether.model.music.soundpool;

import android.content.Context;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.model.music.soundpool.base.BaseSoundPool;

// todo
public class ShakerSoundPool extends BaseSoundPool {

    public ShakerSoundPool(@NonNull Context context, int maxStreams, @NonNull Integer... soundResIDs) {
        super(context, maxStreams, soundResIDs);
    }

    @Override
    public int play(int soundID, float pitch, int length) {
        return 0;
    }
}
