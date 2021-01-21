package de.pcps.jamtugether.ui.onboarding.instrument;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.instrument.base.Instruments;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.storage.Preferences;

public class ChooseInstrumentViewModel extends ViewModel implements Instrument.OnClickListener {

    @Inject
    Preferences preferences;

    public ChooseInstrumentViewModel() {
        AppInjector.inject(this);
    }

    @Override
    public void onInstrumentClicked(@NonNull Instrument instrument) {
        preferences.setMainInstrument(instrument);
    }

    @NonNull
    public List<Instrument> getInstruments() {
        return Instruments.LIST;
    }
}
