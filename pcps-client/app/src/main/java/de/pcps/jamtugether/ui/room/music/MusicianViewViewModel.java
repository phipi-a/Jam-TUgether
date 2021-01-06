package de.pcps.jamtugether.ui.room.music;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.pcps.jamtugether.audio.instrument.drums.Drums;
import de.pcps.jamtugether.audio.instrument.flute.Flute;
import de.pcps.jamtugether.audio.instrument.shaker.Shaker;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;

public class MusicianViewViewModel extends ViewModel implements Instrument.OnChangeCallback, OnOwnSoundtrackChangedCallback {

    @NonNull
    private static final SingleSoundtrack EMPTY_SOUNDTRACK = new SingleSoundtrack();

    @NonNull
    private final MutableLiveData<Boolean> showFluteFragment = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> showDrumsFragment = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> showShakerFragment = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<SingleSoundtrack> ownSoundtrack = new MutableLiveData<>(EMPTY_SOUNDTRACK);

    @Override
    public void onInstrumentChanged(@NonNull Instrument instrument) {
        ownSoundtrack.setValue(EMPTY_SOUNDTRACK);
        if (instrument == Flute.getInstance()) {
            showFluteFragment.setValue(true);
        } else if (instrument == Drums.getInstance()) {
            showDrumsFragment.setValue(true);
        } else if (instrument == Shaker.getInstance()) {
            showShakerFragment.setValue(true);
        }
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
    public LiveData<SingleSoundtrack> getOwnSoundtrack() {
        return ownSoundtrack;
    }

    @Override
    public void onOwnSoundtrackChanged(@NonNull SingleSoundtrack ownSoundtrack) {
        this.ownSoundtrack.setValue(ownSoundtrack);
    }
}
