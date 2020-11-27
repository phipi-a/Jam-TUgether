package de.pcps.jamtugether.content.room.join;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.api.BaseCallback;
import de.pcps.jamtugether.api.Error;
import de.pcps.jamtugether.api.repositories.RoomRepository;
import de.pcps.jamtugether.api.responses.JoinRoomResponse;
import de.pcps.jamtugether.dagger.AppInjector;

public class JoinRoomViewModel extends ViewModel {

    @Inject
    Application application;

    @Inject
    RoomRepository roomRepository;

    private int roomID;

    @NonNull
    private final MutableLiveData<Boolean> navigateToRegularRoom = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<String> roomInputError = new MutableLiveData<>(null);

    @NonNull
    private final MutableLiveData<String> passwordInputError = new MutableLiveData<>(null);

    @NonNull
    private final MutableLiveData<Error> networkError = new MutableLiveData<>(null);

    public JoinRoomViewModel() {
        AppInjector.inject(this);
    }

    public void onJoinRoomButtonClicked(@NonNull String roomIDString, @NonNull String password) {
        Context context = application.getApplicationContext();

        boolean emptyRoom = false;
        boolean emptyPassword = false;

        if(roomIDString.isEmpty()) {
            roomInputError.setValue(context.getString(R.string.room_input_empty));
            emptyRoom = true;
        }

        if(password.isEmpty()) {
            passwordInputError.setValue(context.getString(R.string.password_input_empty));
            emptyPassword = true;
        }

        if(emptyRoom || emptyPassword) {
            if(!emptyRoom) {
                roomInputError.setValue(null);
            }
            if(!emptyPassword) {
                passwordInputError.setValue(null);
            }
            return;
        }

        try {
            roomID = Integer.parseInt(roomIDString);
        } catch (Exception e) {
            // number is higher than 'int'
            roomInputError.setValue(context.getString(R.string.room_doesnt_exit)); // todo check if this message is ok
            return;
        }

        joinRoom(roomID, password);
    }

    private void joinRoom(int roomID, @NonNull String password) {
        Context context = application.getApplicationContext();

        roomRepository.joinRoom(roomID, password, new BaseCallback<JoinRoomResponse>() {
            @Override
            public void onResponse(JoinRoomResponse response) {
                if(!response.roomExists()) {
                    roomInputError.setValue(context.getString(R.string.room_doesnt_exit));
                    return;
                }
                roomInputError.setValue(null);

                if(!response.passwordCorrect()) {
                    passwordInputError.setValue(context.getString(R.string.password_incorrect));
                    return;
                }

                passwordInputError.setValue(null);
                navigateToRegularRoom.setValue(true);
            }

            @Override
            public void onError(@NonNull Error error) {
                networkError.setValue(error);
            }
        });

        // todo remove this line when room service is done
        navigateToRegularRoom.setValue(true);
    }

    public void onNetworkErrorShown() {
        networkError.setValue(null);
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

    @NonNull
    public LiveData<Error> getNetworkError() {
        return networkError;
    }
}
