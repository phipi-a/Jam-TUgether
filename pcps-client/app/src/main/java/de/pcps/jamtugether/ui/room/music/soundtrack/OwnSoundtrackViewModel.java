package de.pcps.jamtugether.ui.room.music.soundtrack;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

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
import de.pcps.jamtugether.ui.room.music.MusicianViewViewModel;

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
    private final MusicianViewViewModel musicianViewViewModel;

    @NonNull
    private final Instrument.OnChangeCallback onChangeCallback;

    @Nullable
    private String helpDialogTitle;

    @Nullable
    private String helpDialogMessage;

    @NonNull
    private Instrument currentInstrument;

    @NonNull
    private final MutableLiveData<Boolean> showHelpDialog = new MutableLiveData<>(false);

    public OwnSoundtrackViewModel(int roomID, @NonNull MusicianViewViewModel musicianViewViewModel) {
        AppInjector.inject(this);
        this.roomID = roomID;
        this.musicianViewViewModel = musicianViewViewModel;
        this.onChangeCallback = musicianViewViewModel;

        Instrument mainInstrument = preferences.getMainInstrument();
        onChangeCallback.onInstrumentChanged(mainInstrument);
        updateHelpDialogData(mainInstrument);
        currentInstrument = mainInstrument;
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
        soundtrackRepository.onCompositionNetworkErrorShown();
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

    @Nullable
    public String getHelpDialogTitle() {
        return helpDialogTitle;
    }

    @Nullable
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
        return musicianViewViewModel.getOwnSoundtrack();
    }

    @NonNull
    public LiveData<Error> getSoundtrackRepositoryNetworkError() {
        return soundtrackRepository.getCompositionNetworkError();
    }

    static class Factory implements ViewModelProvider.Factory {

        private final int roomID;

        @NonNull
        private final MusicianViewViewModel musicianViewViewModel;

        public Factory(int roomID, @NonNull MusicianViewViewModel musicianViewViewModel) {
            this.roomID = roomID;
            this.musicianViewViewModel = musicianViewViewModel;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(OwnSoundtrackViewModel.class)) {
                return (T) new OwnSoundtrackViewModel(roomID, musicianViewViewModel);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
