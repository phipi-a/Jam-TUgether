package de.pcps.jamtugether.content.room.join;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import de.pcps.jamtugether.R;

public class JoinRoomViewModel extends AndroidViewModel {

    private int roomID;

    @NonNull
    private final MutableLiveData<Boolean> navigateToRegularRoom = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<String> roomInputError = new MutableLiveData<>(null);

    @NonNull
    private final MutableLiveData<String> passwordInputError = new MutableLiveData<>(null);

    public JoinRoomViewModel(@NonNull Application application) {
        super(application);
    }

    public void onJoinRoomButtonClicked(@NonNull String roomIdString, @NonNull String password) {
        Context context = getApplication().getApplicationContext();

        boolean roomError = false;

        if(roomIdString.isEmpty()) {
            roomInputError.setValue(context.getString(R.string.room_input_empty));
            roomError = true;
        } else {
            try {
                roomID = Integer.parseInt(roomIdString);
            } catch (Exception e) {
                // number is higher than 'int' can store
                roomError = true;
                roomInputError.setValue(context.getString(R.string.room_doesnt_exit)); // todo check if this message is ok
            }

            if(!roomError && !roomExists(roomID)) {
                roomInputError.setValue(context.getString(R.string.room_doesnt_exit));
                roomError = true;
            }
        }

        if(!roomError) {
            roomInputError.setValue(null);
        }

        if(password.isEmpty()) {
            passwordInputError.setValue(context.getString(R.string.password_input_empty));
            return;
        }
        passwordInputError.setValue(null);

        if(roomError) {
            return;
        }

        if(passwordCorrect(roomID, password)) {
            navigateToRegularRoom.setValue(true);
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

    public void onNavigatedToRegularRoom() {
        navigateToRegularRoom.setValue(false);
    }

    public int getRoomID() {
        return roomID;
    }

    @NonNull
    public LiveData<Boolean> getNavigateToRegularRoom() {
        return navigateToRegularRoom;
    }

    @NonNull
    public LiveData<String> getRoomInputError() {
        return roomInputError;
    }

    @NonNull
    public LiveData<String> getPasswordInputError() {
        return passwordInputError;
    }
}
