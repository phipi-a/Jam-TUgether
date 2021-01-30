package de.pcps.jamtugether.audio.instrument.base;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.pcps.jamtugether.audio.instrument.drums.Drums;
import de.pcps.jamtugether.audio.instrument.flute.Flute;
import de.pcps.jamtugether.audio.instrument.piano.Piano;
import de.pcps.jamtugether.audio.instrument.shaker.Shaker;

public class Instruments {

    @NonNull
    public static final Flute FLUTE = Flute.getInstance();

    @NonNull
    public static final Drums DRUMS = Drums.getInstance();

    @NonNull
    public static final Shaker SHAKER = Shaker.getInstance();

    @NonNull
    public static final Piano PIANO = Piano.getInstance();

    @NonNull
    public static final Instrument[] ARRAY = {FLUTE, DRUMS, SHAKER, PIANO};

    @NonNull
    public static final List<Instrument> LIST = Arrays.asList(ARRAY);

    @NonNull
    public static final Instrument DEFAULT = FLUTE;

    @NonNull
    private static final HashMap<String, Instrument> preferenceMap = new HashMap<>();

    @NonNull
    private static final HashMap<String, Instrument> serverMap = new HashMap<>();

    static {
        for (Instrument instrument : ARRAY) {
            preferenceMap.put(instrument.getPreferenceValue(), instrument);
            serverMap.put(instrument.getServerString(), instrument);
        }
    }

    @NonNull
    public static Instrument fromPreferences(@NonNull String preferenceValue) {
        Instrument instrument = preferenceMap.get(preferenceValue);
        return instrument != null ? instrument : DEFAULT;
    }

    @NonNull
    public static Instrument fromServer(@NonNull String serverString) {
        Instrument instrument = serverMap.get(serverString);
        return instrument != null ? instrument : DEFAULT;
    }
}
