package de.pcps.jamtugether.ui.menu;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MenuViewModel extends ViewModel {

    @NonNull
    private final MutableLiveData<Boolean> navigateToSettings = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> navigateToCreateRoom = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> navigateToJoinRoom = new MutableLiveData<>(false);

    public void onSettingsButtonClicked() {
        navigateToSettings.setValue(true);
    }

    public void onCreateRoomButtonClicked() {
        navigateToCreateRoom.setValue(true);
    }

    public void onJoinRoomButtonClicked() {
        navigateToJoinRoom.setValue(true);
    }

    public void onNavigatedToSettings() {
        navigateToSettings.setValue(false);
    }

    public void onNavigatedToCreateRoom() {
        navigateToCreateRoom.setValue(false);
    }

    public void onNavigatedToJoinRoom() {
        navigateToJoinRoom.setValue(false);
    }

    @NonNull
    public LiveData<Boolean> getNavigateToSettings() {
        return navigateToSettings;
    }

    @NonNull
    public LiveData<Boolean> getNavigateToCreateRoom() {
        return navigateToCreateRoom;
    }

    @NonNull
    public LiveData<Boolean> getNavigateToJoinRoom() {
        return navigateToJoinRoom;
    }
}
