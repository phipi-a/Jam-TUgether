package de.pcps.jamtugether.model.sound.flute;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

import java.util.HashMap;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.sound.SoundResource;

public enum FluteSound implements SoundResource {

    C(R.raw.flute_c),
    C_SHARP(R.raw.flute_c_sharp),
    D(R.raw.flute_d),
    D_SHARP(R.raw.flute_d_sharp),
    E(R.raw.flute_e),
    F(R.raw.flute_f),
    F_SHARP(R.raw.flute_f_sharp),
    G(R.raw.flute_g),
    G_SHARP(R.raw.flute_g_sharp),
    A(R.raw.flute_a),
    A_SHARP(R.raw.flute_a_sharp),
    B(R.raw.flute_b),
    C_HIGH(R.raw.flute_c_high);

    @NonNull
    public static final FluteSound DEFAULT = C;

    @NonNull
    private static final HashMap<Integer, FluteSound> pitchMap = new HashMap<>();

    static {
        for (FluteSound fluteSound : values()) {
            pitchMap.put(fluteSound.getPitch(), fluteSound);
        }
    }

    @RawRes
    private final int soundResource;

    private int duration;

    FluteSound(@RawRes int soundResource) {
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
    public static FluteSound from(int pitch) {
        FluteSound fluteSound = pitchMap.get(pitch);
        return fluteSound != null ? fluteSound : DEFAULT;
    }
}
