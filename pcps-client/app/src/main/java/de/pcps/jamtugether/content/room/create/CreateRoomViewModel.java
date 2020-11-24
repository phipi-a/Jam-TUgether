package de.pcps.jamtugether.content.room.create;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import de.pcps.jamtugether.R;

public class CreateRoomViewModel extends AndroidViewModel {

    private int roomID;

    @NonNull
    private final MutableLiveData<Boolean> navigateToAdminRoom = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<String> passwordInputError = new MutableLiveData<>(null);

    public CreateRoomViewModel(@NonNull Application application) {
        super(application);
    }

    public void onCreateRoomButtonClicked(@NonNull String password) {
        Context context = getApplication().getApplicationContext();

        if(password.isEmpty()) {
            passwordInputError.setValue(context.getString(R.string.password_input_empty));
            return;
        }

        if(!passwordFormatCorrect(password)) {
            passwordInputError.setValue(context.getString(R.string.password_format_incorrect));
            return;
        }
        passwordInputError.setValue(null);

        roomID = createRoom(password);
        navigateToAdminRoom.setValue(true);
    }

    private boolean passwordFormatCorrect(@NonNull String password) {
        // todo
        return true;
    }

    private int createRoom(@NonNull String password) {
        // todo
        return 1;
    }

    public void onNavigatedToAdminRoom() {
        navigateToAdminRoom.setValue(false);
    }

    public int getRoomID() {
        return roomID;
    }

    @NonNull
    public LiveData<Boolean> getNavigateToAdminRoom() {
        return navigateToAdminRoom;
    }

    @NonNull
    public LiveData<String> getPasswordInputError() {
        return passwordInputError;
    }
}


