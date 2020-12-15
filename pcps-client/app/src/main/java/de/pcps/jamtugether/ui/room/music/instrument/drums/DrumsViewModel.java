package de.pcps.jamtugether.ui.room.music.instrument.drums;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import de.pcps.jamtugether.model.instrument.Drums;

public class DrumsViewModel extends AndroidViewModel {

    private final Drums drums = Drums.getInstance();

    public DrumsViewModel(@NonNull Application application) {
        super(application);
        drums.prepare(application.getApplicationContext());
    }

    public void OnKickClicked(){
        drums.playKick();
    }


    public void OnCymbalClicked(){
        drums.playCymbal();
    }


    public void OnSnareClicked(){
        drums.playSnare();
    }


    public void OnHatClicked(){
        drums.playHat();
    }
}
