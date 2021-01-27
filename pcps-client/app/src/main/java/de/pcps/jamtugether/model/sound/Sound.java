package de.pcps.jamtugether.model.sound;

import com.squareup.moshi.Json;

import java.util.Objects;

public class Sound {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sound sound = (Sound) o;
        return startTime == sound.startTime &&
                endTime == sound.endTime &&
                pitch == sound.pitch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, pitch);
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
