package de.pcps.jamtugether.model.sound.drums;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

import java.util.HashMap;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.sound.SoundResource;

public enum DrumsSound implements SoundResource {
    KICK(R.raw.drum_kick),
    SNARE(R.raw.drum_snare),
    HAT(R.raw.drum_hat),
    CYMBAL(R.raw.drum_cymbal);

    @NonNull
    private static final DrumsSound DEFAULT = KICK;

    @NonNull
    private static final HashMap<Integer, DrumsSound> pitchMap = new HashMap<>();

    static {
        for (DrumsSound drumsSound : values()) {
            pitchMap.put(drumsSound.getPitch(), drumsSound);
        }
    }

    @RawRes
    private final int soundResource;

    private int duration;

    DrumsSound(@RawRes int soundResource) {
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
    public int getPitch() {
        return ordinal();
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @NonNull
    public static DrumsSound from(int pitch) {
        DrumsSound drumsSound = pitchMap.get(pitch);
        return drumsSound != null ? drumsSound : DEFAULT;
    }
}
