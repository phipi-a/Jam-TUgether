package de.pcps.jamtugether.utils;

import java.util.Locale;

public class TimeUtils {

    public static final long ONE_SECOND = 1000;
    public static final long TEN_SECONDS = ONE_SECOND * 10;

    public static final long ONE_MINUTE = ONE_SECOND * 60;
    public static final long FIVE_MINUTES = ONE_MINUTE * 5;

    public static final long ONE_HOUR = ONE_MINUTE * 60;

    private static final String TIME_FORMAT_SECONDS_SIMPLE = "%d";
    private static final String TIME_FORMAT_SECONDS_MINUTES = "%02d:%02d";

    public static String formatTimerSecondMinutes(long millis) {
        int minutes = (int) ((millis / ONE_SECOND) / 60);
        int seconds = (int) ((millis / ONE_SECOND) % 60);

        return String.format(Locale.getDefault(), TIME_FORMAT_SECONDS_MINUTES, minutes, seconds);
    }

    public static String formatTimerSecondsSimple(long millis) {
        int seconds = (int) ((millis / ONE_SECOND) % 60);
        return String.format(Locale.getDefault(), TIME_FORMAT_SECONDS_SIMPLE, seconds);
    }

}
