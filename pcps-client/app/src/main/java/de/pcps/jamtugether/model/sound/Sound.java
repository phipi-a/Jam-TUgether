package de.pcps.jamtugether.model.sound;

import com.squareup.moshi.Json;

public class Sound {

    public static final int MIN_PITCH = 0;
    public static final int MAX_PITCH = 100;
    public static final int PITCH_RANGE = MAX_PITCH - MIN_PITCH;

    @Json(name = "starttime")
    private final int startTime;

    @Json(name = "endtime")
    private final int endTime;

    private final int pitch;

    public Sound(int startTime, int endTime, int pitch) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.pitch = pitch;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public int getPitch() {
        return pitch;
    }
}
