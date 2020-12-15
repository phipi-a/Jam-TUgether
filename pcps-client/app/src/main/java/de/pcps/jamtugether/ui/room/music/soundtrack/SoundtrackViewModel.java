package de.pcps.jamtugether.ui.room.music.soundtrack;

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
import de.pcps.jamtugether.model.music.soundtrack.Soundtrack;
import de.pcps.jamtugether.model.music.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.music.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.model.instrument.Instrument;
import de.pcps.jamtugether.model.instrument.Instruments;
import de.pcps.jamtugether.storage.Preferences;

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

    @NonNull
    private final MutableLiveData<List<SingleSoundtrack>> allSoundtracks = new MutableLiveData<>(generateTestSoundtracks());

    @NonNull
    private final MutableLiveData<SingleSoundtrack> ownSoundtrack = new MutableLiveData<>(generateTestOwnSoundtrack());

    public SoundtrackViewModel(int roomID, @NonNull Instrument.OnChangeCallback onChangeCallback) {
        AppInjector.inject(this);
        this.roomID = roomID;
        this.onChangeCallback = onChangeCallback;

        Instrument mainInstrument = preferences.getMainInstrument();
        onChangeCallback.onInstrumentChanged(mainInstrument);
        updateHelpDialogData(mainInstrument);
        currentInstrument = mainInstrument;
    }

    @NonNull
    private List<SingleSoundtrack> generateTestSoundtracks() {
        List<SingleSoundtrack> list = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            list.add(new SingleSoundtrack(i));
        }
        return list;
    }

    @NonNull
    private SingleSoundtrack generateTestOwnSoundtrack() {
        return new SingleSoundtrack(0);
    }

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

    private void fetchAllSoundtracks() {
        // todo get all soundtracks from server and update current list after
    }

    public void onHelpButtonClicked() {
        showHelpDialog.setValue(true);
    }

    public void onHelpDialogShown() {
        showHelpDialog.setValue(false);
    }

    @NonNull
    public Instrument getCurrentInstrument() {
        return currentInstrument;
    }

    @NonNull
    public List<Instrument> getInstruments() {
        return Arrays.asList(Instruments.LIST);
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

    @NonNull
    private LiveData<List<SingleSoundtrack>> getAllSoundtracks() {
        return allSoundtracks;
    }

    @NonNull
    public LiveData<CompositeSoundtrack> getCompositeSoundtrack() {
        return Transformations.map(getAllSoundtracks(), CompositeSoundtrack::from);
    }

    @NonNull
    public LiveData<SingleSoundtrack> getOwnSoundtrack() {
        return ownSoundtrack;
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
