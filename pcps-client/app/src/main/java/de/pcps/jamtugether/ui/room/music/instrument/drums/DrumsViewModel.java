package de.pcps.jamtugether.ui.room.music.instrument.drums;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import de.pcps.jamtugether.audio.instrument.drums.Drums;

public class DrumsViewModel extends ViewModel {

    @NonNull
    private final Drums drums = Drums.getInstance();

    public void onSnareClicked() {
        drums.playSnare();
    }

    public void onKickClicked() {
        drums.playKick();
    }

    public void onHatClicked() {
        drums.playHat();
    }

    public void onCymbalClicked() {
        drums.playCymbal();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        drums.stop();
    }
}
