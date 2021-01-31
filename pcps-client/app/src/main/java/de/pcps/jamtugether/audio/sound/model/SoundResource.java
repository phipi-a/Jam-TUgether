package de.pcps.jamtugether.audio.sound.model;

import androidx.annotation.RawRes;

public interface SoundResource {

    @RawRes
    int getResource();

    int getPitch();

    int getDuration();
}
