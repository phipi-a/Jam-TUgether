package de.pcps.jamtugether.model.sound;

import androidx.annotation.RawRes;

import de.pcps.jamtugether.R;

public enum SoundResource {

    FLUTE(R.raw.flute_sound),

    SNARE(R.raw.drum_snare),
    KICK(R.raw.drum_kick),
    HAT(R.raw.drum_hat),
    CYMBAL(R.raw.drum_cymbal),

    SHAKER(R.raw.flute_sound); // todo

    @RawRes
    private final int resource;

    private int duration;

    SoundResource(@RawRes int resource) {
        this.resource = resource;
    }

    @RawRes
    public int getResource() {
        return resource;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}
