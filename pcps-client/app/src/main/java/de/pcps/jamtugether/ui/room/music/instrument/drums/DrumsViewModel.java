package de.pcps.jamtugether.ui.room.music.instrument.drums;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.audio.instrument.drums.Drums;
import de.pcps.jamtugether.model.Sound;
import de.pcps.jamtugether.audio.instrument.drums.DrumsSound;
import de.pcps.jamtugether.ui.room.music.OnOwnSoundtrackChangedCallback;
import de.pcps.jamtugether.ui.room.music.instrument.InstrumentViewModel;

public class DrumsViewModel extends InstrumentViewModel {

    @NonNull
    private static final Drums drums = Drums.getInstance();

    public DrumsViewModel(@NonNull OnOwnSoundtrackChangedCallback callback) {
        super(drums, callback);
    }

    public void onSnareClicked() {
        drums.playSnare();
        onElementPlayed(DrumsSound.SNARE);
    }

    public void onKickClicked() {
        drums.playKick();
        onElementPlayed(DrumsSound.KICK);
    }

    public void onHatClicked() {
        drums.playHat();
        onElementPlayed(DrumsSound.HAT);
    }

    public void onCymbalClicked() {
        drums.playCymbal();
        onElementPlayed(DrumsSound.CYMBAL);
    }

    private void onElementPlayed(DrumsSound drumsSound) {
        if (!recordingSoundtrack) {
            return;
        }
        int soundDuration = drumsSound.getDuration();
        int startTimeMillis = (int) (System.currentTimeMillis() - startedMillis);
        int endTimeMillis = startTimeMillis + soundDuration;
        if (ownSoundtrack != null) {
            ownSoundtrack.addSound(new Sound(startTimeMillis, endTimeMillis, drumsSound.getPitch()));
        }
    }

    @Override
    protected void finishRecordingSoundtrack(boolean loop) {
        super.finishRecordingSoundtrack(loop);
        drums.stop();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        drums.stop();
    }

    static class Factory implements ViewModelProvider.Factory {

        @NonNull
        private final OnOwnSoundtrackChangedCallback callback;

        public Factory(@NonNull OnOwnSoundtrackChangedCallback callback) {
            this.callback = callback;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(DrumsViewModel.class)) {
                return (T) new DrumsViewModel(callback);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
