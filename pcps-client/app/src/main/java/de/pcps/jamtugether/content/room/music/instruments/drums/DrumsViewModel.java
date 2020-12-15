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
    public LiveData<Boolean> getCymbalClicked() {
        return cymbalClicked;
    }

    @NonNull
    public LiveData<Boolean> getSnareClicked() {
        return snareClicked;
    }

    @NonNull
    public LiveData<Boolean> getHatClicked() {
        return hatClicked;
    }

    @NonNull
    private final MutableLiveData<Boolean> kickClicked = new MutableLiveData<>(false);
    public void OnKickClicked(){
        kickClicked.setValue(true);
    }

    @NonNull
    private final MutableLiveData<Boolean> cymbalClicked = new MutableLiveData<>(false);
    public void OnCymbalClicked(){
        cymbalClicked.setValue(true);
    }

    @NonNull
    private final MutableLiveData<Boolean> snareClicked = new MutableLiveData<>(false);
    public void OnSnareClicked(){
        cymbalClicked.setValue(true);
    }

    @NonNull
    private final MutableLiveData<Boolean> hatClicked = new MutableLiveData<>(false);
    public void OnHatClicked(){
        cymbalClicked.setValue(true);
    }
}
