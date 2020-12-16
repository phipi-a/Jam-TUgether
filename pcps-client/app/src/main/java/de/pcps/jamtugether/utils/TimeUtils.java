package de.pcps.jamtugether.utils;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;

public class TimeUtils {

    public static final long ONE_SECOND = 1000;

    public static int getSoundDuration(int sound, @NonNull Context context) { // todo maybe find a better long term solution
        return MediaPlayer.create(context, sound).getDuration();
    }
}
