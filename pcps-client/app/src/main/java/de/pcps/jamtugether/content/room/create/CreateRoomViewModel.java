package de.pcps.jamtugether.content.room.create;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class CreateRoomViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> navigateToAdminRoom = new MutableLiveData<>(false);

    public CreateRoomViewModel(@NonNull Application application) {
        super(application);
    }

    public void onCreateRoomButtonClicked(@NonNull String password) {
        if(password.isEmpty()) {
            // todo error dialog: empty password
            return;
        }

        if(!passwordFormatCorrect(password)) {
            // todo error dialog: wrong password format
            return;
        }

        createRoom(password);
        navigateToAdminRoom.setValue(true);
    }


    private boolean passwordFormatCorrect(@NonNull String password) {
        // todo
        return true;
    }

    private void createRoom(@NonNull String password) {
        // todo
    }

    public void onNavigatedToAdminRoom() {
        navigateToAdminRoom.setValue(false);
    }

    public LiveData<Boolean> getNavigateToAdminRoom() {
        return navigateToAdminRoom;
    }
}


