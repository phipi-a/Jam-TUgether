package de.pcps.jamtugether.content.room.create;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import de.pcps.jamtugether.R;

public class CreateRoomViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> navigateToAdminRoom = new MutableLiveData<>(false);

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

    public LiveData<String> getPasswordInputError() {
        return passwordInputError;
    }
}


