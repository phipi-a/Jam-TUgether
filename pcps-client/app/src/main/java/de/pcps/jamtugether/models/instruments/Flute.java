package de.pcps.jamtugether.models.instruments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.pcps.jamtugether.R;

// todo
public class Flute extends Instrument {

    @Nullable
    private static Flute instance;

    public Flute() {
        super(0, R.string.instrument_flute, R.string.play_flute_help, "flute", "flute");
    }

    @NonNull
    public static Flute getInstance() {
        if (instance == null) {
            instance = new Flute();
        }
        return instance;
    }
}
