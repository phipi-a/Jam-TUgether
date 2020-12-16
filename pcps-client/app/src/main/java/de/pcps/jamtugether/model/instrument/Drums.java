package de.pcps.jamtugether.model.instrument;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.instrument.base.Instrument;
import de.pcps.jamtugether.model.music.sound.Sound;

// todo
public class Drums extends Instrument {

    @Nullable
    private static Drums instance;

    public Drums() {
        super(1, R.string.instrument_drums, R.string.play_drums_help, "drums", "drums");
    }

    @NonNull
    public static Drums getInstance() {
        if(instance == null) {
            instance = new Drums();
        }
        return instance;
    }

    @Override
    public void play(@NonNull Sound sound) {
        if(sound.getInstrument() != this) {
            return;
        }
        // todo
    }
}
