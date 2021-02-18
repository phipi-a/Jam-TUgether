package de.pcps.jamtugether.ui.room.music;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.pcps.jamtugether.audio.instrument.drums.Drums;
import de.pcps.jamtugether.audio.instrument.flute.Flute;
import de.pcps.jamtugether.audio.instrument.piano.Piano;
import de.pcps.jamtugether.audio.instrument.shaker.Shaker;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;

public class MusicianViewViewModel extends ViewModel implements Instrument.OnChangeCallback, OnOwnSoundtrackChangedCallback {

    @NonNull
    private static final SingleSoundtrack EMPTY_OWN_SOUNDTRACK = new SingleSoundtrack("");

    @NonNull
    private final MutableLiveData<Boolean> showFluteFragment = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> showDrumsFragment = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> showShakerFragment = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> showPianoFragment = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<SingleSoundtrack> ownSoundtrack = new MutableLiveData<>(EMPTY_OWN_SOUNDTRACK);

    @NonNull
    private final MutableLiveData<Boolean> soundtracksExpanded = new MutableLiveData<>(false);

    @Override
    public void onInstrumentChanged(@NonNull Instrument instrument) {
        ownSoundtrack.setValue(EMPTY_OWN_SOUNDTRACK);
        if (instrument == Flute.getInstance()) {
            showFluteFragment.setValue(true);
        } else if (instrument == Drums.getInstance()) {
            showDrumsFragment.setValue(true);
        } else if (instrument == Shaker.getInstance()) {
            showShakerFragment.setValue(true);
        } else if (instrument == Piano.getInstance()) {
            showPianoFragment.setValue(true);
        }
    }

    @Override
    public void onOwnSoundtrackChanged(@NonNull SingleSoundtrack ownSoundtrack) {
        this.ownSoundtrack.setValue(ownSoundtrack);
    }

    public void setSoundtracksExpanded(boolean expanded) {
        soundtracksExpanded.setValue(expanded);
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

    public void onPianoFragmentShown() {
        showPianoFragment.setValue(false);
    }

    @NonNull
    public LiveData<Boolean> getShowFluteFragment() {
        return showFluteFragment;
    }

    @NonNull
    public LiveData<Boolean> getShowDrumsFragment() {
        return showDrumsFragment;
    }

    @NonNull
    public LiveData<Boolean> getShowShakerFragment() {
        return showShakerFragment;
    }

    @NonNull
    public LiveData<Boolean> getShowPianoFragment() {
        return showPianoFragment;
    }

    @NonNull
    public LiveData<SingleSoundtrack> getOwnSoundtrack() {
        return ownSoundtrack;
    }

    @NonNull
    public LiveData<Boolean> getSoundtracksExpanded() {
        return soundtracksExpanded;
    }
}
