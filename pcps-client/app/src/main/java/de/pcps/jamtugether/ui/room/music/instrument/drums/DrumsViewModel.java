package de.pcps.jamtugether.ui.room.music.instrument.drums;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.audio.instrument.drums.Drums;
import de.pcps.jamtugether.model.sound.Sound;
import de.pcps.jamtugether.model.sound.SoundResource;
import de.pcps.jamtugether.ui.room.music.OnOwnSoundtrackChangedCallback;
import de.pcps.jamtugether.ui.room.music.instrument.InstrumentViewModel;

public class DrumsViewModel extends InstrumentViewModel {

    @NonNull
    private static final Drums drums = Drums.getInstance();

    public DrumsViewModel(int roomID, int userID, @NonNull String token, @NonNull OnOwnSoundtrackChangedCallback callback) {
        super(drums, roomID, userID, token, callback);
    }

    public void onSnareClicked() {
        drums.playSnare();
        onElementPlayed(Drums.SNARE_PITCH, SoundResource.SNARE);
    }

    public void onKickClicked() {
        drums.playKick();
        onElementPlayed(Drums.KICK_PITCH, SoundResource.KICK);
    }

    public void onHatClicked() {
        drums.playHat();
        onElementPlayed(Drums.HAT_PITCH, SoundResource.HAT);
    }

    public void onCymbalClicked() {
        drums.playCymbal();
        onElementPlayed(Drums.CYMBAL_PITCH, SoundResource.CYMBAL);
    }

    private void onElementPlayed(int pitch, SoundResource soundResource) {
        if (!timer.isRunning()) {
            return;
        }
        int soundDuration = soundResource.getDuration();
        int startTimeMillis = (int) (System.currentTimeMillis() - startedMillis);
        int endTimeMillis = startTimeMillis + soundDuration;
        if(ownSoundtrack != null) {
            ownSoundtrack.addSound(new Sound(startTimeMillis, endTimeMillis, pitch));
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
        private final String token;

        @NonNull
        private final OnOwnSoundtrackChangedCallback callback;

        public Factory(int roomID, int userID, @NonNull String token, @NonNull OnOwnSoundtrackChangedCallback callback) {
            this.roomID = roomID;
            this.userID = userID;
            this.token = token;
            this.callback = callback;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(DrumsViewModel.class)) {
                return (T) new DrumsViewModel(roomID, userID, token, callback);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
