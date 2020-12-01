package de.pcps.jamtugether.content.room.users.music;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.content.instrument.Instrument;

public class MusicianViewViewModel extends ViewModel implements Instrument.OnChangeCallback {

    private final int roomID;

    @NonNull
    private final MutableLiveData<Boolean> showFluteFragment = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> showDrumsFragment = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> showShakerFragment = new MutableLiveData<>(false);

    public MusicianViewViewModel(int roomID) {
        this.roomID = roomID;
    }

    @Override
    public void onInstrumentChanged(@NonNull Instrument instrument) {
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
