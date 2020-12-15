package de.pcps.jamtugether.ui.menu.create;

import android.app.Application;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.api.JamCallback;
import de.pcps.jamtugether.api.errors.base.Error;
import de.pcps.jamtugether.api.errors.PasswordTooLargeError;
import de.pcps.jamtugether.api.repositories.RoomRepository;
import de.pcps.jamtugether.api.responses.room.CreateRoomResponse;
import de.pcps.jamtugether.di.AppInjector;

public class CreateRoomViewModel extends ViewModel {

    @Inject
    Application application;
    
    @Inject
    RoomRepository roomRepository;
    
    private int roomID;

    private String password;

    private String token;

    @NonNull
    private final MutableLiveData<Boolean> navigateToAdminRoom = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<String> passwordInputError = new MutableLiveData<>(null);
    
    @NonNull
    private final MutableLiveData<Error> networkError = new MutableLiveData<>(null);

    @NonNull
    private final MutableLiveData<Integer> progressBarVisibility = new MutableLiveData<>(View.INVISIBLE);

    public CreateRoomViewModel() {
        AppInjector.inject(this);
    }

    public void onCreateRoomButtonClicked(@NonNull String password) {
        Context context = application.getApplicationContext();

        if(password.isEmpty()) {
            passwordInputError.setValue(context.getString(R.string.password_input_empty));
            return;
        }
        passwordInputError.setValue(null);

        this.password = password;
        createRoom(password);
    }

    private void createRoom(@NonNull String password) {
        progressBarVisibility.setValue(View.VISIBLE);

        roomRepository.createRoom(password, new JamCallback<CreateRoomResponse>() {
            @Override
            public void onSuccess(@NonNull CreateRoomResponse response) {
                progressBarVisibility.setValue(View.INVISIBLE);
                roomID = response.getRoomID();
                token = response.getToken();
                navigateToAdminRoom.setValue(true);
            }

            @Override
            public void onError(@NonNull Error error) {
                progressBarVisibility.setValue(View.INVISIBLE);

                Context context = application.getApplicationContext();

                if(error instanceof PasswordTooLargeError) {
                    passwordInputError.setValue(context.getString(error.getMessage()));
                    return;
                }
                passwordInputError.setValue(null);

                networkError.setValue(error);
            }
        });
    }

    public void onNetworkErrorShown() {
        networkError.setValue(null);
    }

    public void onNavigatedToAdminRoom() {
        navigateToAdminRoom.setValue(false);
    }

    public int getRoomID() {
        return roomID;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    @NonNull
    public LiveData<Boolean> getNavigateToAdminRoom() {
        return navigateToAdminRoom;
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
    public MutableLiveData<Integer> getProgressBarVisibility() {
        return progressBarVisibility;
    }
}


