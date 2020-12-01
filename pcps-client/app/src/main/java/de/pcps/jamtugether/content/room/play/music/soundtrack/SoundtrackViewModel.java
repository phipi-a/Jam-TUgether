package de.pcps.jamtugether.content.room.play.music.soundtrack;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.content.instrument.Instrument;
import de.pcps.jamtugether.content.soundtrack.Soundtrack;
import de.pcps.jamtugether.dagger.AppInjector;
import de.pcps.jamtugether.storage.Preferences;

import static de.pcps.jamtugether.content.instrument.Instrument.values;

public class SoundtrackViewModel extends ViewModel implements Instrument.ClickListener, Soundtrack.OnChangeListener {

    @Inject
    Application application;

    @Inject
    Preferences preferences;

    private final int roomID;

    @NonNull
    private final Instrument.OnChangeCallback onChangeCallback;

    private String helpDialogTitle;
    private String helpDialogMessage;

    private Instrument currentInstrument;

    @NonNull
    private final MutableLiveData<Boolean> showHelpDialog = new MutableLiveData<>(false);

    public SoundtrackViewModel(int roomID, @NonNull Instrument.OnChangeCallback onChangeCallback) {
        AppInjector.inject(this);
        this.roomID = roomID;
        this.onChangeCallback = onChangeCallback;

        Instrument mainInstrument = preferences.getMainInstrument();
        onChangeCallback.onInstrumentChanged(mainInstrument);
        updateHelpDialogData(mainInstrument);
        currentInstrument = mainInstrument;
    }
    //

    @NonNull
    private List<Soundtrack> generateTestSoundtracks() {
        List<Soundtrack> list = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            list.add(new Soundtrack(i));
        }
        return list;
    }
    @NonNull
    private final MutableLiveData<List<Soundtrack>> allSoundtracks = new MutableLiveData<>(generateTestSoundtracks());

    @NonNull
    public LiveData<List<Soundtrack>> getAllSoundtracks() {
        return allSoundtracks;
    }

    @NonNull
    public LiveData<Soundtrack> getCompositeSoundtrack() {
        return Transformations.map(getAllSoundtracks(), Soundtrack::compositeFrom);
    }
    @NonNull
    private final MutableLiveData<List<Soundtrack>> ownSoundtracks = new MutableLiveData<>(generateTestSoundtracks());
    @NonNull
    public LiveData<List<Soundtrack>> getOwnSoundtracks() {
        return ownSoundtracks;
    }

    @NonNull
    public LiveData<Soundtrack> getOwnSoundtrack() {
        return Transformations.map(getOwnSoundtracks(), Soundtrack::ownFrom);
    }
    //

    @Override
    public void onInstrumentClicked(@NonNull Instrument instrument) {
        if(instrument != currentInstrument) {
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

    @Override
    public void onVolumeChanged(@NonNull Soundtrack soundtrack, float volume) {
        soundtrack.setVolume(volume);
    }

    @Override
    public void onPlayPauseButtonClicked(@NonNull Soundtrack soundtrack) { }

    @Override
    public void onStopButtonClicked(@NonNull Soundtrack soundtrack) { }

    @Override
    public void onFastForwardButtonClicked(@NonNull Soundtrack soundtrack) { }

    @Override
    public void onFastRewindButtonClicked(@NonNull Soundtrack soundtrack) { }

    @Override
    public void onDeleteButtonClicked(@NonNull Soundtrack soundtrack) { /* wil never be called */ }

    @NonNull
    public Instrument getCurrentInstrument() {
        return currentInstrument;
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
    public LiveData<Boolean> getShowHelpDialog() {
        return showHelpDialog;
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
            if (modelClass.isAssignableFrom(SoundtrackViewModel.class)) {
                return (T) new SoundtrackViewModel(roomID, onChangeCallback);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
