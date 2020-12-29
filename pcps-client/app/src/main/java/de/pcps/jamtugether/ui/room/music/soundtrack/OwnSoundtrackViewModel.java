package de.pcps.jamtugether.ui.room.music.soundtrack;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.api.errors.base.Error;
import de.pcps.jamtugether.api.repositories.SoundtrackRepository;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.instrument.base.Instruments;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import de.pcps.jamtugether.audio.player.SoundtrackController;
import de.pcps.jamtugether.storage.Preferences;

public class OwnSoundtrackViewModel extends ViewModel implements Instrument.ClickListener {

    @Inject
    Application application;

    @Inject
    Preferences preferences;

    @Inject
    SoundtrackRepository soundtrackRepository;

    @Inject
    SoundtrackController soundtrackController;

    private final int roomID;

    @NonNull
    private final Instrument.OnChangeCallback onChangeCallback;

    private String helpDialogTitle;

    private String helpDialogMessage;

    @NonNull
    private Instrument currentInstrument;

    @NonNull
    private final MutableLiveData<Boolean> showHelpDialog = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<SingleSoundtrack> ownSoundtrack = new MutableLiveData<>();

    public OwnSoundtrackViewModel(int roomID, @NonNull Instrument.OnChangeCallback onChangeCallback) {
        AppInjector.inject(this);
        this.roomID = roomID;
        this.onChangeCallback = onChangeCallback;

        Instrument mainInstrument = preferences.getMainInstrument();
        onChangeCallback.onInstrumentChanged(mainInstrument);
        updateHelpDialogData(mainInstrument);
        currentInstrument = mainInstrument;

        onSoundtrackCreated(generateTestOwnSoundtrack());
    }

    public void onSoundtrackCreated(@NonNull SingleSoundtrack ownSoundtrack) {
        this.ownSoundtrack.setValue(ownSoundtrack);
    }

    @NonNull
    private SingleSoundtrack generateTestOwnSoundtrack() {
        return new SingleSoundtrack(0, new ArrayList<>());
    }

    @Override
    public void onInstrumentClicked(@NonNull Instrument instrument) {
        if (instrument != currentInstrument) {
            onChangeCallback.onInstrumentChanged(instrument);
            updateHelpDialogData(instrument);
            currentInstrument = instrument;
        }
    }

    private void updateHelpDialogData(@NonNull Instrument instrument) {
        Context context = application.getApplicationContext();

        String instrumentName = context.getString(instrument.getName());
        helpDialogTitle = context.getString(R.string.play_instrument_format, instrumentName);
        helpDialogMessage = context.getString(instrument.getHelpMessage());
    }

    public void onHelpButtonClicked() {
        showHelpDialog.setValue(true);
    }

    public void onHelpDialogShown() {
        showHelpDialog.setValue(false);
    }

    public void onSoundtrackRepositoryNetworkErrorShown() {
        soundtrackRepository.onNetworkErrorShown();
    }

    @NonNull
    public Instrument getCurrentInstrument() {
        return currentInstrument;
    }

    @NonNull
    public List<Instrument> getInstruments() {
        return Instruments.LIST;
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
    public Soundtrack.OnChangeCallback getSoundtrackOnChangeCallback() {
        return soundtrackController;
    }

    @NonNull
    public LiveData<Boolean> getShowHelpDialog() {
        return showHelpDialog;
    }

    @NonNull
    public LiveData<SingleSoundtrack> getOwnSoundtrack() {
        return ownSoundtrack;
    }

    @NonNull
    public LiveData<Error> getSoundtrackRepositoryNetworkError() {
        return soundtrackRepository.getNetworkError();
    }

    static class Factory implements ViewModelProvider.Factory {

        private final int roomID;

        @NonNull
        private final Instrument.OnChangeCallback onChangeCallback;

        public Factory(int roomID, @NonNull Instrument.OnChangeCallback onChangeCallback) {
            this.roomID = roomID;
            this.onChangeCallback = onChangeCallback;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(OwnSoundtrackViewModel.class)) {
                return (T) new OwnSoundtrackViewModel(roomID, onChangeCallback);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
