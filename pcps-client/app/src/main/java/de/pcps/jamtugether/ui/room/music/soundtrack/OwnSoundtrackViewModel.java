package de.pcps.jamtugether.ui.room.music.soundtrack;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import javax.inject.Inject;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.api.Constants;
import de.pcps.jamtugether.api.errors.base.Error;
import de.pcps.jamtugether.api.repositories.RoomRepository;
import de.pcps.jamtugether.api.repositories.SoundtrackRepository;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.instrument.base.Instruments;
import de.pcps.jamtugether.audio.player.SoundtrackController;
import de.pcps.jamtugether.storage.Preferences;
import de.pcps.jamtugether.utils.TimeUtils;

public class OwnSoundtrackViewModel extends ViewModel implements Instrument.ClickListener {

    @Inject
    Application application;

    @Inject
    Preferences preferences;

    @Inject
    SoundtrackRepository soundtrackRepository;

    @Inject
    RoomRepository roomRepository;

    @Inject
    SoundtrackController soundtrackController;

    @NonNull
    private final Instrument.OnChangeCallback instrumentOnChangeCallback;

    @Nullable
    private String helpDialogTitle;

    @Nullable
    private String helpDialogMessage;

    @NonNull
    private Instrument currentInstrument;

    @NonNull
    private final MutableLiveData<Boolean> showHelpDialog = new MutableLiveData<>(false);

    public OwnSoundtrackViewModel(@NonNull Instrument.OnChangeCallback instrumentOnChangeCallback) {
        AppInjector.inject(this);
        this.instrumentOnChangeCallback = instrumentOnChangeCallback;

        Instrument mainInstrument = preferences.getMainInstrument();
        instrumentOnChangeCallback.onInstrumentChanged(mainInstrument);
        updateHelpDialogData(mainInstrument);
        currentInstrument = mainInstrument;
    }

    @Override
    public void onInstrumentClicked(@NonNull Instrument instrument) {
        if (instrument != currentInstrument) {
            instrumentOnChangeCallback.onInstrumentChanged(instrument);
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

    @Nullable
    public Integer getRoomID() {
        return roomRepository.getRoomID();
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
    public LiveData<Boolean> getShowHelpDialog() {
        return showHelpDialog;
    }

    @NonNull
    public LiveData<Error> getSoundtrackRepositoryNetworkError() {
        return soundtrackRepository.getCompositionNetworkError();
    }

    @NonNull
    public LiveData<CompositeSoundtrack> getCompositeSoundtrack() {
        return soundtrackRepository.getCompositeSoundtrack();
    }

    @NonNull
    public LiveData<Integer> getCountDownProgress() {
        return Transformations.map(soundtrackRepository.getCountDownTimerMillis(), this::calculateProgress);
    }

    private int calculateProgress(long millis) {
        return (int) ((Constants.SOUNDTRACK_FETCHING_INTERVAL - millis) / (double) Constants.SOUNDTRACK_FETCHING_INTERVAL * 100);
    }

    static class Factory implements ViewModelProvider.Factory {

        @NonNull
        private final Instrument.OnChangeCallback instrumentOnChangeCallback;

        public Factory(@NonNull Instrument.OnChangeCallback instrumentOnChangeCallback) {
            this.instrumentOnChangeCallback = instrumentOnChangeCallback;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(OwnSoundtrackViewModel.class)) {
                return (T) new OwnSoundtrackViewModel(instrumentOnChangeCallback);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
