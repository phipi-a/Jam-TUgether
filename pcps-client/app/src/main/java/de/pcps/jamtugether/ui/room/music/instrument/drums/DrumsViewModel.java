package de.pcps.jamtugether.ui.room.music.instrument.drums;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.model.instrument.Drums;

public class DrumsViewModel extends ViewModel {

    @Inject
    Drums drums;

    public DrumsViewModel() {
        AppInjector.inject(this);
    }

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
