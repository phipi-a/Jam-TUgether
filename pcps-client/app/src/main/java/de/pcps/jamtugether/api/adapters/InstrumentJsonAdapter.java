package de.pcps.jamtugether.api.adapters;

import androidx.annotation.NonNull;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.instrument.base.Instruments;

public class InstrumentJsonAdapter {

    @NonNull
    @FromJson
    Instrument fromJson(@NonNull String instrument) {
        return Instruments.fromServer(instrument);
    }

    @NonNull
    @ToJson
    String toJson(@NonNull Instrument instrument) {
        return instrument.getServerString();
    }
}
