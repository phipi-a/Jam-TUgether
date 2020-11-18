package de.pcps.jamtugether.content.main_instrument;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;

import de.pcps.jamtugether.base.dagger.AppInjector;
import de.pcps.jamtugether.content.main_instrument.instrument.Instrument;
import de.pcps.jamtugether.storage.Preferences;

public class MainInstrumentViewModel extends AndroidViewModel implements Instrument.ClickListener {

    @Inject
    Preferences preferences;

    private final MutableLiveData<Boolean> navigateToMenu = new MutableLiveData<>(false);

    @NonNull
    public LiveData<Boolean> getNavigateToMenu() {
        return navigateToMenu;
    }

    public MainInstrumentViewModel(@NonNull Application application) {
        super(application);
        AppInjector.inject(this);
    }

    @Override
    public void onInstrumentClicked(@NonNull Instrument instrument) {
        preferences.setMainInstrument(instrument);
        preferences.setUserNeverChoseInstrument(false);
        navigateToMenu.setValue(true);
    }

    public void onNavigatedToMenu() {
        navigateToMenu.setValue(false);
    }
}
