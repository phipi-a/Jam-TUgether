package de.pcps.jamtugether.model.instrument;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.instrument.base.Instrument;

// todo
public class Shaker extends Instrument {

    @Nullable
    private static Shaker instance;

    public Shaker() {
        super(2, R.string.instrument_shaker, R.string.play_shaker_help, "shaker", "shaker");
    }

    @NonNull
    public static Shaker getInstance() {
        if(instance == null) {
            instance = new Shaker();
        }
        return instance;
    }
}
