package de.pcps.jamtugether.ui.room.music.soundtrack;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import javax.inject.Inject;

import de.pcps.jamtugether.api.Constants;
import de.pcps.jamtugether.api.errors.base.Error;
import de.pcps.jamtugether.api.repositories.RoomRepository;
import de.pcps.jamtugether.api.repositories.SoundtrackRepository;
import de.pcps.jamtugether.audio.instrument.drums.Drums;
import de.pcps.jamtugether.audio.instrument.flute.Flute;
import de.pcps.jamtugether.audio.instrument.shaker.Shaker;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.instrument.base.Instruments;
import de.pcps.jamtugether.audio.player.SoundtrackController;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.storage.Preferences;
import de.pcps.jamtugether.ui.room.music.MusicianViewViewModel;
import de.pcps.jamtugether.utils.TimeUtils;
import timber.log.Timber;

public class OwnSoundtrackViewModel extends ViewModel implements Instrument.OnSelectionListener {

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

    @NonNull
    private final MusicianViewViewModel musicianViewViewModel;

    @NonNull
    private Instrument currentInstrument;

    @NonNull
    private final MutableLiveData<Boolean> showFluteHelpDialog = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> showShakerHelpDialog = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> showDrumsHelpDialog = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> showPianoHelpDialog = new MutableLiveData<>(false);

    @NonNull
    private final MediatorLiveData<Error> showNetworkErrorMediator = new MediatorLiveData<>();

    private boolean soundtrackRepositoryErrorShown;

    public OwnSoundtrackViewModel(@NonNull Instrument.OnChangeCallback instrumentOnChangeCallback, @NonNull MusicianViewViewModel musicianViewViewModel) {
        AppInjector.inject(this);
        this.instrumentOnChangeCallback = instrumentOnChangeCallback;
        this.musicianViewViewModel = musicianViewViewModel;

        showNetworkErrorMediator.addSource(soundtrackRepository.getShowNetworkError(), networkError -> {
            if (networkError != null && !soundtrackRepositoryErrorShown) {
                showNetworkErrorMediator.setValue(networkError);
                soundtrackRepositoryErrorShown = true;
            }
        });

        Instrument mainInstrument = preferences.getMainInstrument();
        instrumentOnChangeCallback.onInstrumentChanged(mainInstrument);
        currentInstrument = mainInstrument;
    }

    @Override
    public void onInstrumentSelected(@NonNull Instrument instrument) {
        if (instrument != currentInstrument) {
            instrumentOnChangeCallback.onInstrumentChanged(instrument);
            currentInstrument = instrument;
        }
    }

    public void onHelpButtonClicked() {
        if (currentInstrument == Flute.getInstance()) {
            showFluteHelpDialog.setValue(true);
        } else if (currentInstrument == Drums.getInstance()) {
            showDrumsHelpDialog.setValue(true);
        } else if (currentInstrument == Shaker.getInstance()) {
            showShakerHelpDialog.setValue(true);
        } else {
            showPianoHelpDialog.setValue(true);
        }
    }

    public void onExpandButtonClicked() {
        Boolean soundtracksExpanded = musicianViewViewModel.getSoundtracksExpanded().getValue();
        if (soundtracksExpanded == null) {
            return;
        }
        musicianViewViewModel.setSoundtracksExpanded(!soundtracksExpanded);
    }

    public void onFluteHelpDialogShown() {
        showFluteHelpDialog.setValue(false);
    }

    public void onShakerHelpDialogShown() {
        showShakerHelpDialog.setValue(false);
    }

    public void onDrumsHelpDialogShown() {
        showDrumsHelpDialog.setValue(false);
    }

    public void onPianoHelpDialogShown() {
        showPianoHelpDialog.setValue(false);
    }

    public void onNetworkErrorShown() {
        soundtrackRepository.onNetworkErrorShown();
    }

    @Override
    protected void onCleared() {
        showNetworkErrorMediator.removeSource(soundtrackRepository.getShowNetworkError());
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

    @NonNull
    public LiveData<Boolean> getShowFluteHelpDialog() {
        return showFluteHelpDialog;
    }

    @NonNull
    public LiveData<Boolean> getShowShakerHelpDialog() {
        return showShakerHelpDialog;
    }

    @NonNull
    public LiveData<Boolean> getShowDrumsHelpDialog() {
        return showDrumsHelpDialog;
    }

    @NonNull
    public LiveData<Boolean> getShowPianoHelpDialog() {
        return showPianoHelpDialog;
    }

    @NonNull
    public LiveData<Integer> getSoundtracksVisibility() {
        return Transformations.map(musicianViewViewModel.getSoundtracksExpanded(), soundtracksExpanded -> soundtracksExpanded ? View.VISIBLE : View.GONE);
    }

    @NonNull
    public LiveData<CompositeSoundtrack> getCompositeSoundtrack() {
        return soundtrackRepository.getCompositeSoundtrack();
    }

    @NonNull
    public LiveData<Error> getShowNetworkError() {
        return showNetworkErrorMediator;
    }

    @NonNull
    public LiveData<Integer> getCountDownProgress() {
        return Transformations.map(soundtrackRepository.getCountDownTimerMillis(), this::calculateProgress);
    }

    private int calculateProgress(long millis) {
        return (int) ((Constants.SOUNDTRACK_FETCHING_INTERVAL - millis + TimeUtils.ONE_SECOND) / (double) Constants.SOUNDTRACK_FETCHING_INTERVAL * 100);
    }

    static class Factory implements ViewModelProvider.Factory {

        @NonNull
        private final Instrument.OnChangeCallback instrumentOnChangeCallback;

        @NonNull
        private final MusicianViewViewModel musicianViewViewModel;

        public Factory(@NonNull Instrument.OnChangeCallback instrumentOnChangeCallback, @NonNull MusicianViewViewModel musicianViewViewModel) {
            this.instrumentOnChangeCallback = instrumentOnChangeCallback;
            this.musicianViewViewModel = musicianViewViewModel;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(OwnSoundtrackViewModel.class)) {
                return (T) new OwnSoundtrackViewModel(instrumentOnChangeCallback, musicianViewViewModel);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
