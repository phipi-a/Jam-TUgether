package de.pcps.jamtugether.ui.room.music;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.audio.instrument.Drums;
import de.pcps.jamtugether.audio.instrument.Flute;
import de.pcps.jamtugether.audio.instrument.Shaker;
import de.pcps.jamtugether.audio.instrument.base.Instrument;

public class MusicianViewViewModel extends ViewModel implements Instrument.OnChangeCallback {

    private final int roomID;

    @NonNull
    private final String token;

    @NonNull
    private final MutableLiveData<Boolean> showFluteFragment = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> showDrumsFragment = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> showShakerFragment = new MutableLiveData<>(false);

    public MusicianViewViewModel(int roomID, @NonNull String token) {
        this.roomID = roomID;
        this.token = token;
    }

    @Override
    public void onInstrumentChanged(@NonNull Instrument instrument) {
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

    public int getRoomID() {
        return roomID;
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

    public static class Factory implements ViewModelProvider.Factory {

        private final int roomID;

        @NonNull
        private final String token;

        public Factory(int roomID, @NonNull String token) {
            this.roomID = roomID;
            this.token = token;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(MusicianViewViewModel.class)) {
                return (T) new MusicianViewViewModel(roomID, token);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
