package de.pcps.jamtugether.utils;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

public class SoundUtils {

    public static int getSoundDuration(@RawRes int soundRes, @NonNull Context context) {
        return MediaPlayer.create(context, soundRes).getDuration();
    }
}
