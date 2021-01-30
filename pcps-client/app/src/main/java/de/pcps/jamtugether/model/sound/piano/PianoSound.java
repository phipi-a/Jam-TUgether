package de.pcps.jamtugether.model.sound.piano;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.annotation.StringRes;

import java.util.HashMap;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.sound.SoundResource;

public enum PianoSound implements SoundResource {

    C(R.raw.piano_c, R.string.music_note_c),
    C_SHARP(R.raw.piano_c_sharp, R.string.music_note_c_sharp),
    D(R.raw.piano_d, R.string.music_note_d),
    D_SHARP(R.raw.piano_d_sharp, R.string.music_note_d_sharp),
    E(R.raw.piano_e, R.string.music_note_e),
    F(R.raw.piano_f, R.string.music_note_f),
    F_SHARP(R.raw.piano_f_sharp, R.string.music_note_f_sharp),
    G(R.raw.piano_g, R.string.music_note_g),
    G_SHARP(R.raw.piano_g_sharp, R.string.music_note_g_sharp),
    A(R.raw.piano_a, R.string.music_note_a),
    A_SHARP(R.raw.piano_a_sharp, R.string.music_note_a_sharp),
    B(R.raw.piano_b, R.string.music_note_b),
    C_HIGH(R.raw.piano_c_high, R.string.music_note_c_high);

    @NonNull
    public static final PianoSound[] WHITE_TILE_SOUNDS = {C, D, E, F, G, A, B, C_HIGH};

    @NonNull
    public static final PianoSound[] BLACK_TILE_SOUNDS = {C_SHARP, D_SHARP, F_SHARP, G_SHARP, A_SHARP};

    @NonNull
    public static final PianoSound DEFAULT = C;

    @NonNull
    private static final HashMap<Integer, PianoSound> pitchMap = new HashMap<>();

    static {
        for (PianoSound pianoSound : values()) {
            pitchMap.put(pianoSound.getPitch(), pianoSound);
        }
    }

    @RawRes
    private final int soundResource;

    @StringRes
    private final int labelResource;

    private int duration;

    PianoSound(@RawRes int soundResource, @StringRes int labelResource) {
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
    public static PianoSound from(int pitch) {
        PianoSound pianoSound = pitchMap.get(pitch);
        return pianoSound != null ? pianoSound : DEFAULT;
    }
}
