package de.pcps.jamtugether.content.room.join;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class JoinRoomViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> navigateToNormalRoom = new MutableLiveData<>(false);

    public JoinRoomViewModel(@NonNull Application application) {
        super(application);
    }

    public void onJoinRoomButtonClicked(@NonNull String roomIdString, @NonNull String password) {
        if(roomIdString.isEmpty()) {
            // todo error dialog: empty room id
            return;
        }

        int roomId = Integer.parseInt(roomIdString);

        if(!roomExists(roomId)) {
            // todo error dialog: room doesn't exist
            return;
        }

        if(password.isEmpty()) {
            // todo error dialog: empty password
            return;
        }

        if(passwordCorrect(roomId, password)) {
            navigateToNormalRoom.setValue(true);
        }
    }

    private boolean roomExists(int roomId) {
        // todo
        return true;
    }

    private boolean passwordCorrect(int roomId, @NonNull String password) {
        // todo
        return true;
    }

    public void onNavigatedToNormalRoom() {
        navigateToNormalRoom.setValue(false);
    }

    public LiveData<Boolean> getNavigateToNormalRoom() {
        return navigateToNormalRoom;
    }
}
