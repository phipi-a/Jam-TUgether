package de.pcps.jamtugether.content.room.create;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class CreateRoomViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> navigateToJamRoom = new MutableLiveData<>(false);

    public CreateRoomViewModel(@NonNull Application application) {
        super(application);
    }

    public void onCreateRoomButtonClicked() {
        navigateToJamRoom.setValue(true);
    }

    public void onNavigatedToJamRoom() {
        navigateToJamRoom.setValue(false);
    }

    public LiveData<Boolean> getNavigateToJamRoom() {
        return navigateToJamRoom;
    }
}


