package de.pcps.jamtugether.content.settings;

import android.app.Application;

import androidx.annotation.NonNull;

import androidx.lifecycle.AndroidViewModel;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import de.pcps.jamtugether.base.dagger.AppInjector;
import de.pcps.jamtugether.content.instrument.Instrument;
import de.pcps.jamtugether.storage.Preferences;

public class SettingsViewModel extends AndroidViewModel implements Instrument.ClickListener {

    @Inject
    Preferences preferences;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
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
        return Arrays.asList(Instrument.values());
    }
}
