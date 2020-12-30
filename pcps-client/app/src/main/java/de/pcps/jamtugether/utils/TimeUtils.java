package de.pcps.jamtugether.utils;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

public class TimeUtils {

    public static final long ONE_SECOND = 1000;
    public static final long TEN_SECONDS = ONE_SECOND * 10;
    public static final long ONE_MINUTE = ONE_SECOND * 60;
    public static final long ONE_HOUR = ONE_MINUTE * 60;

    public static int getSoundDuration(@RawRes int soundRes, @NonNull Context context) {
        return MediaPlayer.create(context, soundRes).getDuration();
    }
}
