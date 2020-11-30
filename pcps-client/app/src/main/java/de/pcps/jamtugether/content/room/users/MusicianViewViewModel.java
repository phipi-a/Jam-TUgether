package de.pcps.jamtugether.content.room.users;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.dagger.AppInjector;
import de.pcps.jamtugether.content.instrument.Instrument;
import de.pcps.jamtugether.storage.Preferences;

import static de.pcps.jamtugether.content.instrument.Instrument.*;

public class MusicianViewViewModel extends ViewModel implements ClickListener {

    @Inject
    Application application;

    @Inject
    Preferences preferences;

    private final int roomID;

    private String helpDialogTitle;
    private String helpDialogMessage;

    @NonNull
    private final MutableLiveData<Boolean> showHelpDialog = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> showFluteFragment = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> showDrumsFragment = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> showShakerFragment = new MutableLiveData<>(false);

    public MusicianViewViewModel(int roomID) {
        AppInjector.inject(this);
        this.roomID = roomID;

        Instrument mainInstrument = preferences.getMainInstrument();
        updateHelpDialogData(mainInstrument);
    }

    @Override
    public void onInstrumentClicked(@NonNull Instrument instrument) {
        switch (instrument) {
            case FLUTE:
                showFluteFragment.setValue(true);
                break;
            case DRUMS:
                showDrumsFragment.setValue(true);
                break;
            case SHAKER:
                showShakerFragment.setValue(true);
                break;
        }

        updateHelpDialogData(instrument);
    }

    private void updateHelpDialogData(@NonNull Instrument instrument) {
        Context context = application.getApplicationContext();

        String instrumentName = context.getString(instrument.getName());
        helpDialogTitle = context.getString(R.string.play_instrument_format, instrumentName);
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
        return Arrays.asList(values());
    }

    public void onHelpButtonClicked() {
        showHelpDialog.setValue(true);
    }

    public void onHelpDialogShown() {
        showHelpDialog.setValue(false);
    }

    public void onFluteFragmentShown() {
        showFluteFragment.setValue(false);
    }

    public void onDrumsFragmentShown() {
        showDrumsFragment.setValue(false);
    }

    public void onShakerFragmentShown() {
        showShakerFragment.setValue(false);
    }

    @NonNull
    public LiveData<Boolean> getShowHelpDialog() {
        return showHelpDialog;
    }

    @NonNull
    public MutableLiveData<Boolean> getShowFluteFragment() {
        return showFluteFragment;
    }

    @NonNull
    public MutableLiveData<Boolean> getShowDrumsFragment() {
        return showDrumsFragment;
    }

    @NonNull
    public MutableLiveData<Boolean> getShowShakerFragment() {
        return showShakerFragment;
    }

    static class Factory implements ViewModelProvider.Factory {

        private final int roomID;

        public Factory(int roomID) {
            this.roomID = roomID;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(MusicianViewViewModel.class)) {
                return (T) new MusicianViewViewModel(roomID);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

}
