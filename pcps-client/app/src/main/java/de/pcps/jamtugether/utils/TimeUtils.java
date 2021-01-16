package de.pcps.jamtugether.utils;

import androidx.annotation.NonNull;

import java.util.Locale;

public class TimeUtils {

    public static final long ONE_SECOND = 1000;
    public static final long TEN_SECONDS = ONE_SECOND * 10;

    public static final long ONE_MINUTE = ONE_SECOND * 60;
    public static final long FIVE_MINUTES = ONE_MINUTE * 5;

    private static final String TIME_FORMAT_SECONDS = "%d";
    private static final String TIME_FORMAT_MINUTES_SECONDS = "%02d:%02d";

    @NonNull
    public static String formatToMinutesSeconds(long millis) {
        int minutes = (int) ((millis / ONE_SECOND) / 60);
        int seconds = (int) ((millis / ONE_SECOND) % 60);

        return String.format(Locale.getDefault(), TIME_FORMAT_MINUTES_SECONDS, minutes, seconds);
    }

    @NonNull
    public static String formatToSeconds(long millis) {
        int seconds = (int) ((millis / ONE_SECOND) % 60);
        return String.format(Locale.getDefault(), TIME_FORMAT_SECONDS, seconds);
    }
}
