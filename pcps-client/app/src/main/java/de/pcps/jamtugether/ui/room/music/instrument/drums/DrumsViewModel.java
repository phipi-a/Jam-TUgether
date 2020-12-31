package de.pcps.jamtugether.ui.room.music.instrument.drums;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import de.pcps.jamtugether.audio.instrument.drums.Drums;
import de.pcps.jamtugether.audio.SoundResource;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.model.sound.ServerSound;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.ui.room.music.OnOwnSoundtrackChangedCallback;

public class DrumsViewModel extends ViewModel {

    @Inject
    Application application;

    private final int roomID;
    private final int userID;

    @NonNull
    private final OnOwnSoundtrackChangedCallback callback;

    @NonNull
    private final Drums drums = Drums.getInstance();

    private SingleSoundtrack ownSoundtrack;

    @NonNull
    private final MutableLiveData<Boolean> startedCreatingOwnSoundtrack = new MutableLiveData<>(false);

    private long startedMillis;

    public DrumsViewModel(int userID, int roomID, @NonNull OnOwnSoundtrackChangedCallback callback) {
        AppInjector.inject(this);
        this.userID = userID;
        this.roomID = roomID;
        this.callback = callback;
    }

    public void onCreateOwnSoundtrackButtonClicked() {
        boolean started = startedCreatingOwnSoundtrack.getValue();
        if(started) {
            callback.onOwnSoundtrackChanged(ownSoundtrack);
            startedCreatingOwnSoundtrack.setValue(false);
        } else {
            ownSoundtrack = new SingleSoundtrack(userID, Drums.getInstance());
            ownSoundtrack.loadSounds(application.getApplicationContext());
            startedCreatingOwnSoundtrack.setValue(true);
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
        if(!startedCreatingOwnSoundtrack.getValue()) {
            return;
        }
        if(ownSoundtrack.isEmpty()) {
            startedMillis = System.currentTimeMillis();
        }
        int soundDuration = soundResource.getDuration();
        int startTimeMillis = (int) (System.currentTimeMillis() - startedMillis);
        int endTimeMillis = startTimeMillis + soundDuration;
        ownSoundtrack.addSound(new ServerSound(roomID, userID, Drums.getInstance(), element, startTimeMillis, endTimeMillis, 50));
    }

    @NonNull
    public LiveData<Boolean> getStartedCreatingOwnSoundtrack() {
        return startedCreatingOwnSoundtrack;
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
