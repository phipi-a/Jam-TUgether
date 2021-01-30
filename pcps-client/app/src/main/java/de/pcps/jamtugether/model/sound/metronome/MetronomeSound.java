package de.pcps.jamtugether.model.sound.metronome;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

import java.util.HashMap;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.sound.SoundResource;
import de.pcps.jamtugether.model.sound.piano.PianoSound;

public enum MetronomeSound implements SoundResource {
    METRONOME(R.raw.metronome),
    METRONOME_UP(R.raw.metronome_up);

    @RawRes
    private final int soundResource;

    private int duration;

    MetronomeSound(@RawRes int soundResource) {
        this.soundResource = soundResource;
    }

    @NonNull
    public static final MetronomeSound DEFAULT = METRONOME;

    @NonNull
    private static final HashMap<Integer, MetronomeSound> pitchMap = new HashMap<>();

    static {
        for (MetronomeSound metronomeSound : values()) {
            pitchMap.put(metronomeSound.getPitch(), metronomeSound);
        }
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
    public static MetronomeSound from(int pitch) {
        MetronomeSound pianoSound = pitchMap.get(pitch);
        return pianoSound != null ? pianoSound : DEFAULT;
    }
}
