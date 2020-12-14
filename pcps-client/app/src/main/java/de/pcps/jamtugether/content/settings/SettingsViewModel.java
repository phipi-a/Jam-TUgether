package de.pcps.jamtugether.content.settings;

import androidx.annotation.NonNull;

import androidx.lifecycle.ViewModel;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import de.pcps.jamtugether.dagger.AppInjector;
import de.pcps.jamtugether.models.instruments.Instrument;
import de.pcps.jamtugether.models.instruments.Instruments;
import de.pcps.jamtugether.storage.Preferences;

public class SettingsViewModel extends ViewModel implements Instrument.ClickListener {

    @Inject
    Preferences preferences;

    public SettingsViewModel() {
        AppInjector.inject(this);
    }

    @Override
    public void onInstrumentClicked(@NonNull Instrument instrument) {
        preferences.setMainInstrument(instrument);
    }

    @NonNull
    public Instrument getMainInstrument() {
        return preferences.getMainInstrument();
    }

    @NonNull
    public List<Instrument> getInstruments() {
        return Arrays.asList(Instruments.LIST);
    }
}