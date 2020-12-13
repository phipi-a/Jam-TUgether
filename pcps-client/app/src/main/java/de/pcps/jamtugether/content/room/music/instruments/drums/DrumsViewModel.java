package de.pcps.jamtugether.content.room.music.instruments.drums;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DrumsViewModel extends ViewModel {
    @NonNull
    public LiveData<Boolean> getKickClicked() {
        return kickClicked;
    }

    @NonNull
    private final MutableLiveData<Boolean> kickClicked = new MutableLiveData<>(false);
    public void OnKickClicked(){
        kickClicked.setValue(true);
    }
}
