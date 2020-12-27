package de.pcps.jamtugether.audio.instrument.base;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.pcps.jamtugether.audio.instrument.Drums;
import de.pcps.jamtugether.audio.instrument.Flute;
import de.pcps.jamtugether.audio.instrument.Shaker;

public class Instruments {

    @NonNull
    public static final Flute FLUTE = Flute.getInstance();

    @NonNull
    public static final Drums DRUMS = Drums.getInstance();

    @NonNull
    public static final Shaker SHAKER = Shaker.getInstance();

    @NonNull
    private static final Instrument[] ARRAY = {FLUTE, DRUMS, SHAKER};

    public static final List<Instrument> LIST = Arrays.asList(ARRAY);

    @NonNull
    public static final Instrument FALLBACK = FLUTE; // todo maybe replace fallback with error message

    @NonNull
    private static final HashMap<String, Instrument> preferenceMap = new HashMap<>();

    @NonNull
    private static final HashMap<String, Instrument> serverMap = new HashMap<>();

    static {
        for(Instrument instrument : ARRAY) {
            preferenceMap.put(instrument.getPreferenceValue(), instrument);
            serverMap.put(instrument.getServerString(), instrument);
        }
    }

    @NonNull
    public static Instrument fromPreferences(@NonNull String preferenceValue) {
        Instrument instrument = preferenceMap.get(preferenceValue);
        return instrument != null ? instrument : FALLBACK;
    }

    @NonNull
    public static Instrument fromServer(@NonNull String serverString) {
        Instrument instrument = serverMap.get(serverString);
        return instrument != null ? instrument : FALLBACK;
    }
}
