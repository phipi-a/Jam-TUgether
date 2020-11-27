package de.pcps.jamtugether.content.room.create;

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
import de.pcps.jamtugether.dagger.AppInjector;

public class CreateRoomViewModel extends ViewModel {

    private static final String PASSWORD_PATTERN = ".*"; // todo

    @Inject
    Application application;
    
    @Inject
    RoomRepository roomRepository;
    
    private int roomID;

    @NonNull
    private final MutableLiveData<Boolean> navigateToAdminRoom = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<String> passwordInputError = new MutableLiveData<>(null);
    
    @NonNull
    private final MutableLiveData<Error> networkError = new MutableLiveData<>(null);

    public CreateRoomViewModel() {
        AppInjector.inject(this);
    }

    public void onCreateRoomButtonClicked(@NonNull String password) {
        Context context = application.getApplicationContext();

        if(password.isEmpty()) {
            passwordInputError.setValue(context.getString(R.string.password_input_empty));
            return;
        }

        if(!password.matches(PASSWORD_PATTERN)) {
            passwordInputError.setValue(context.getString(R.string.password_format_incorrect));
            return;
        }
        passwordInputError.setValue(null);

        createRoom(password);
    }

    private void createRoom(@NonNull String password) {
        roomRepository.createRoom(password, new BaseCallback<Integer>() {
            @Override
            public void onResponse(Integer response) {
                roomID = response;
                navigateToAdminRoom.setValue(true);
            }

            @Override
            public void onError(@NonNull Error error) {
                networkError.setValue(error);
            }
        });

        // todo remove these 2 lines when room service is done
        roomID = 1;
        navigateToAdminRoom.setValue(true);
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
}


