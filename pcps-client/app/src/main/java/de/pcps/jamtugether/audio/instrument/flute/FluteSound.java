package de.pcps.jamtugether.audio.instrument.flute;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.annotation.StringRes;

import java.util.HashMap;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.sound.model.SoundResource;

public enum FluteSound implements SoundResource {

    C(R.raw.flute_c, R.string.music_note_c),
    C_SHARP(R.raw.flute_c_sharp, R.string.music_note_c_sharp),
    D(R.raw.flute_d, R.string.music_note_d),
    D_SHARP(R.raw.flute_d_sharp, R.string.music_note_d_sharp),
    E(R.raw.flute_e, R.string.music_note_e),
    F(R.raw.flute_f, R.string.music_note_f),
    F_SHARP(R.raw.flute_f_sharp, R.string.music_note_f_sharp),
    G(R.raw.flute_g, R.string.music_note_g),
    G_SHARP(R.raw.flute_g_sharp, R.string.music_note_g_sharp),
    A(R.raw.flute_a, R.string.music_note_a),
    A_SHARP(R.raw.flute_a_sharp, R.string.music_note_a_sharp),
    B(R.raw.flute_b, R.string.music_note_b),
    C_HIGH(R.raw.flute_c_high, R.string.music_note_c_high);

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

    @StringRes
    private final int labelResource;

    private int duration;

    FluteSound(@RawRes int soundResource, @StringRes int labelResource) {
        this.soundResource = soundResource;
        this.labelResource = labelResource;
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
    public String getLabel(@NonNull Context context) {
        return context.getString(labelResource);
    }

    @NonNull
    public static FluteSound from(int pitch) {
        FluteSound fluteSound = pitchMap.get(pitch);
        return fluteSound != null ? fluteSound : DEFAULT;
    }
}
