package de.pcps.jamtugether.content.room.join;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import de.pcps.jamtugether.R;

public class JoinRoomViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> navigateToRegularRoom = new MutableLiveData<>(false);

    private final MutableLiveData<String> roomInputError = new MutableLiveData<>(null);
    private final MutableLiveData<String> passwordInputError = new MutableLiveData<>(null);

    public JoinRoomViewModel(@NonNull Application application) {
        super(application);
    }

    public void onJoinRoomButtonClicked(@NonNull String roomIdString, @NonNull String password) {
        Context context = getApplication().getApplicationContext();
        if(roomIdString.isEmpty()) {
            roomInputError.setValue(context.getString(R.string.room_input_empty));
            return;
        }

        int roomId = Integer.parseInt(roomIdString);

        if(!roomExists(roomId)) {
            roomInputError.setValue(context.getString(R.string.room_doesnt_exit));
            return;
        }
        roomInputError.setValue(null);

        if(password.isEmpty()) {
            passwordInputError.setValue(context.getString(R.string.password_input_empty));
            return;
        }
        passwordInputError.setValue(null);

        if(passwordCorrect(roomId, password)) {
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

    public LiveData<Boolean> getNavigateToRegularRoom() {
        return navigateToRegularRoom;
    }

    public LiveData<String> getRoomInputError() {
        return roomInputError;
    }

    public LiveData<String> getPasswordInputError() {
        return passwordInputError;
    }

}
