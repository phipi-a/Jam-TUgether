package de.pcps.jamtugether.content.welcome;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import de.pcps.jamtugether.dagger.AppInjector;
import de.pcps.jamtugether.models.instrument.Instrument;
import de.pcps.jamtugether.models.instrument.Instruments;
import de.pcps.jamtugether.storage.Preferences;

public class WelcomeViewModel extends ViewModel implements Instrument.ClickListener {

    @Inject
    Preferences preferences;

    @NonNull
    private final MutableLiveData<Boolean> navigateToMenu = new MutableLiveData<>(false);

    public WelcomeViewModel() {
        AppInjector.inject(this);
    }

    @Override
    public void onInstrumentClicked(@NonNull Instrument instrument) {
        preferences.setMainInstrument(instrument);
        preferences.setUserNeverChoseInstrument(false);
        navigateToMenu.setValue(true);
    }

    @NonNull
    public List<Instrument> getInstruments() {
        return Arrays.asList(Instruments.LIST);
    }

    public void onNavigatedToMenu() {
        navigateToMenu.setValue(false);
    }

    @NonNull
    public LiveData<Boolean> getNavigateToMenu() {
        return navigateToMenu;
    }
}
