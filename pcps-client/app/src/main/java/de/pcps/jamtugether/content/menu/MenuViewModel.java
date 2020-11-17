package de.pcps.jamtugether.content.menu;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MenuViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> navigateToCreateRoom = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> navigateToJoinRoom = new MutableLiveData<>(false);

    public LiveData<Boolean> getNavigateToCreateRoom() {
        return navigateToCreateRoom;
    }

    public LiveData<Boolean> getNavigateToJoinRoom() {
        return navigateToJoinRoom;
    }

    public MenuViewModel(@NonNull Application application) {
        super(application);
    }

    public void onCreateRoomButtonClicked() {
        navigateToCreateRoom.setValue(true);
    }

    public void onJoinRoomButtonClicked() {
        navigateToJoinRoom.setValue(true);
    }

    public void onNavigatedToCreateRoom() {
        navigateToCreateRoom.setValue(false);
    }

    public void onNavigatedToJoinRoom() {
        navigateToJoinRoom.setValue(false);
    }
}
