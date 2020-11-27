package de.pcps.jamtugether.content.room.join;

import android.app.Application;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.api.BaseCallback;
import de.pcps.jamtugether.api.errors.Error;
import de.pcps.jamtugether.api.errors.UnauthorizedAccessError;
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

    @NonNull
    private final MutableLiveData<Integer> progressBarVisibility = new MutableLiveData<>(View.INVISIBLE);

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
            roomInputError.setValue(context.getString(R.string.room_doesnt_exist)); // todo check if this message is ok
            return;
        }

        joinRoom(roomID, password);
    }

    private void joinRoom(int roomID, @NonNull String password) {
        progressBarVisibility.setValue(View.VISIBLE);

        roomRepository.joinRoom(roomID, password, new BaseCallback<JoinRoomResponse>() {
            @Override
            public void onResponse(JoinRoomResponse response) {
                progressBarVisibility.setValue(View.INVISIBLE);
                navigateToRegularRoom.setValue(true);
            }

            @Override
            public void onError(@NonNull Error error) {
                progressBarVisibility.setValue(View.INVISIBLE);

                Context context = application.getApplicationContext();

                if(error instanceof UnauthorizedAccessError) {
                    roomInputError.setValue(context.getString(error.getMessage()));
                    passwordInputError.setValue(context.getString(error.getMessage()));
                    return;
                }
                networkError.setValue(error);

                roomInputError.setValue(null);
                passwordInputError.setValue(null);
            }
        });

        // todo remove these 2 lines when room service is done
        navigateToRegularRoom.setValue(true);
        progressBarVisibility.setValue(View.INVISIBLE);
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

    @NonNull
    public LiveData<Integer> getProgressBarVisibility() {
        return progressBarVisibility;
    }
}
