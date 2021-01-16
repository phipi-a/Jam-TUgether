package de.pcps.jamtugether.model.sound.shaker;

import androidx.annotation.RawRes;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.sound.SoundResource;

public enum ShakerSound implements SoundResource {

    SHAKER(R.raw.shaker_sound);

    @RawRes
    private final int soundResource;

    private int duration;

    ShakerSound(@RawRes int soundResource) {
        this.soundResource = soundResource;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public int getResource() {
        return soundResource;
    }

    @Override
    public int getDuration() {
        return duration;
    }
}
