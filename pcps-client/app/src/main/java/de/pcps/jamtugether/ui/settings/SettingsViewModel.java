package de.pcps.jamtugether.ui.settings;

import androidx.annotation.NonNull;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.instrument.base.Instruments;
import de.pcps.jamtugether.storage.Preferences;

public class SettingsViewModel extends ViewModel implements Instrument.OnSelectionListener {

    @Inject
    Preferences preferences;

    @NonNull
    private final MutableLiveData<Boolean> navigateToOnBoarding = new MutableLiveData<>(false);

    public SettingsViewModel() {
        AppInjector.inject(this);
    }

    public void onBoardingButtonClicked() {
        navigateToOnBoarding.setValue(true);
    }

    @Override
    public void onInstrumentSelected(@NonNull Instrument instrument) {
        preferences.setMainInstrument(instrument);
    }

    public void onNavigatedToOnBoarding() {
        navigateToOnBoarding.setValue(false);
    }

    @NonNull
    public Instrument getMainInstrument() {
        return preferences.getMainInstrument();
    }

    @NonNull
    public List<Instrument> getInstruments() {
        return Instruments.LIST;
    }

    @NonNull
    public LiveData<Boolean> getNavigateToOnBoarding() {
        return navigateToOnBoarding;
    }
}
