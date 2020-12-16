package de.pcps.jamtugether.ui.room.music.instrument.drums;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import de.pcps.jamtugether.model.instrument.Drums;

public class DrumsViewModel extends AndroidViewModel {

    private final Drums drums = Drums.getInstance();

    public DrumsViewModel(@NonNull Application application) {
        super(application);
        drums.prepare(application.getApplicationContext());
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

}
