package de.pcps.jamtugether.model.sound;

import androidx.annotation.RawRes;

public interface SoundResource {

    @RawRes
    int getResource();

    int getPitch();

    int getDuration();
}
