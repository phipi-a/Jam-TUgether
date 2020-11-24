package de.pcps.jamtugether.content.room.users;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.base.dagger.AppInjector;
import de.pcps.jamtugether.content.instrument.Instrument;
import de.pcps.jamtugether.storage.Preferences;

public class MusicianViewViewModel extends AndroidViewModel implements Instrument.ClickListener {

    @Inject
    Preferences preferences;

    private final int roomID;

    private String helpDialogTitle;
    private String helpDialogMessage;

    @NonNull
    private final MutableLiveData<Instrument> selectedInstrument;

    @NonNull
    private final MutableLiveData<Boolean> showHelpDialog = new MutableLiveData<>(false);

    public MusicianViewViewModel(@NonNull Application application, int roomID) {
        super(application);
        this.roomID = roomID;

        AppInjector.inject(this);

        Instrument mainInstrument = preferences.getMainInstrument();
        selectedInstrument = new MutableLiveData<>(mainInstrument);
        updateHelpDialogData(mainInstrument);
    }

    @Override
    public void onInstrumentClicked(@NonNull Instrument instrument) {
        selectedInstrument.setValue(instrument);
        updateHelpDialogData(instrument);
    }

    private void updateHelpDialogData(@NonNull Instrument instrument) {
        Context context = getApplication().getApplicationContext();
        String instrumentName = context.getString(instrument.getName());
        helpDialogTitle = context.getString(R.string.play_instrument, instrumentName);
        helpDialogMessage = context.getString(instrument.getHelpMessage());
    }

    @NonNull
    public Instrument getMainInstrument() {
        return preferences.getMainInstrument();
    }

    public int getRoomID() {
        return roomID;
    }

    public String getHelpDialogTitle() {
        return helpDialogTitle;
    }

    public String getHelpDialogMessage() {
        return helpDialogMessage;
    }

    @NonNull
    public List<Instrument> getInstruments() {
        return Arrays.asList(Instrument.values());
    }

    public void onHelpButtonClicked() {
        showHelpDialog.setValue(true);
    }

    public void onHelpDialogShown() {
        showHelpDialog.setValue(false);
    }

    @NonNull
    public LiveData<Instrument> getSelectedInstrument() {
        return selectedInstrument;
    }

    @NonNull
    public LiveData<Boolean> getShowHelpDialog() {
        return showHelpDialog;
    }

    static class Factory implements ViewModelProvider.Factory {

        @NonNull
        private final Application application;

        private final int roomID;

        public Factory(@NonNull Application application, int roomID) {
            this.application = application;
            this.roomID = roomID;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(MusicianViewViewModel.class)) {
                return (T) new MusicianViewViewModel(application, roomID);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
