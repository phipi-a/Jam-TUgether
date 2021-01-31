package de.pcps.jamtugether.ui.onboarding.items;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.instrument.base.Instruments;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.storage.Preferences;

public class ChooseMainInstrumentViewModel extends ViewModel implements Instrument.OnSelectionListener {

    @Inject
    Preferences preferences;

    public ChooseMainInstrumentViewModel() {
        AppInjector.inject(this);
    }

    @Override
    public void onInstrumentSelected(@NonNull Instrument instrument) {
        preferences.setMainInstrument(instrument);
    }

    @NonNull
    public Instrument getMainInstrument() {
        return preferences.getMainInstrument();
    }

    @NonNull
    public List<Instrument> getInstruments() {
        return Instruments.LIST;
    }
}
