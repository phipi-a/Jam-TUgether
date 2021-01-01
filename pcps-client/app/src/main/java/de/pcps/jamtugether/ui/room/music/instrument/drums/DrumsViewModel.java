package de.pcps.jamtugether.ui.room.music.instrument.drums;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.audio.instrument.drums.Drums;
import de.pcps.jamtugether.audio.SoundResource;
import de.pcps.jamtugether.model.sound.ServerSound;
import de.pcps.jamtugether.ui.room.music.OnOwnSoundtrackChangedCallback;
import de.pcps.jamtugether.ui.room.music.instrument.InstrumentViewModel;

public class DrumsViewModel extends InstrumentViewModel {

    @NonNull
    private static final Drums drums = Drums.getInstance();

    public DrumsViewModel(int roomID, int userID, @NonNull OnOwnSoundtrackChangedCallback callback) {
        super(drums, roomID, userID, callback);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void onPause() {
        if(startedSoundtrackCreation()) {
            finishSoundtrack();
        }
    }

    public void onSnareClicked() {
        drums.playSnare();
        onElementPlayed(0, SoundResource.SNARE);
    }

    public void onKickClicked() {
        drums.playKick();
        onElementPlayed(1, SoundResource.KICK);
    }

    public void onHatClicked() {
        drums.playHat();
        onElementPlayed(2, SoundResource.HAT);
    }

    public void onCymbalClicked() {
        drums.playCymbal();
        onElementPlayed(3, SoundResource.CYMBAL);
    }

    private void onElementPlayed(int element, SoundResource soundResource) {
        if (!timer.isRunning()) {
            return;
        }
        int soundDuration = soundResource.getDuration();
        int startTimeMillis = (int) (System.currentTimeMillis() - startedMillis);
        int endTimeMillis = startTimeMillis + soundDuration;
        if(ownSoundtrack != null) {
            ownSoundtrack.addSound(new ServerSound(roomID, userID, Drums.getInstance(), element, startTimeMillis, endTimeMillis, -1));
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        drums.stop();
    }

    static class Factory implements ViewModelProvider.Factory {

        private final int roomID;
        private final int userID;

        @NonNull
        private final OnOwnSoundtrackChangedCallback callback;

        public Factory(int roomID, int userID, @NonNull OnOwnSoundtrackChangedCallback callback) {
            this.roomID = roomID;
            this.userID = userID;
            this.callback = callback;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(DrumsViewModel.class)) {
                return (T) new DrumsViewModel(roomID, userID, callback);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
