package de.pcps.jamtugether.model.sound.drums;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

import java.util.HashMap;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.sound.SoundResource;

public enum DrumsSound implements SoundResource {
    SNARE(R.raw.drum_snare, 5),
    KICK(R.raw.drum_kick, 30),
    HAT(R.raw.drum_hat, 70),
    CYMBAL(R.raw.drum_cymbal, 90);

    @NonNull
    private static final DrumsSound DEFAULT = KICK;

    @NonNull
    private static final HashMap<Integer, DrumsSound> pitchMap = new HashMap<>();

    static {
        for (DrumsSound drumsSound : values()) {
            pitchMap.put(drumsSound.pitch, drumsSound);
        }
    }

    @RawRes
    private final int soundResource;

    private final int pitch;

    private int duration;

    DrumsSound(@RawRes int soundResource, int pitch) {
        this.soundResource = soundResource;
        this.pitch = pitch;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public int getResource() {
        return soundResource;
    }

    public int getPitch() {
        return pitch;
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
