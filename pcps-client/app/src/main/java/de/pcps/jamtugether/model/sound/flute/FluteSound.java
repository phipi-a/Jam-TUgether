package de.pcps.jamtugether.model.sound.flute;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

import java.util.HashMap;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.sound.SoundResource;

public enum FluteSound implements SoundResource {

    C(R.raw.flute_sound, 8),
    C_SHARP(R.raw.flute_sound, 15),
    D(R.raw.flute_sound, 22),
    D_SHARP(R.raw.flute_sound, 29),
    E(R.raw.flute_sound, 36),
    F(R.raw.flute_sound, 43),
    F_SHARP(R.raw.flute_sound, 50),
    G(R.raw.flute_sound, 57),
    G_SHARP(R.raw.flute_sound, 64),
    A(R.raw.flute_sound, 71),
    A_SHARP(R.raw.flute_sound, 78),
    B(R.raw.flute_sound, 85),
    C_HIGH(R.raw.flute_sound, 92);

    @NonNull
    public static final FluteSound DEFAULT = C;

    @NonNull
    private static final HashMap<Integer, FluteSound> pitchMap = new HashMap<>();

    static {
        for(FluteSound fluteSound : values()) {
            pitchMap.put(fluteSound.pitch, fluteSound);
        }
    }

    @RawRes
    private final int soundResource;

    private final int pitch;

    private int duration;

    FluteSound(@RawRes int soundResource, int pitch) {
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
    public static FluteSound from(int pitch) {
        FluteSound fluteSound = pitchMap.get(pitch);
        return fluteSound != null ? fluteSound : DEFAULT;
    }
}
