package de.pcps.jamtugether.content.menu.create;

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
import de.pcps.jamtugether.api.repositories.RoomRepository;
import de.pcps.jamtugether.api.responses.CreateRoomResponse;
import de.pcps.jamtugether.dagger.AppInjector;

public class CreateRoomViewModel extends ViewModel {

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

        createRoom(password);
    }

    private void createRoom(@NonNull String password) {
        progressBarVisibility.setValue(View.VISIBLE);

        roomRepository.createRoom(password, new BaseCallback<CreateRoomResponse>() {
            @Override
            public void onResponse(@NonNull CreateRoomResponse response) {
                progressBarVisibility.setValue(View.INVISIBLE);
                roomID = response.getRoomID();
                navigateToAdminRoom.setValue(true);
            }

            @Override
            public void onError(@NonNull Error error) {
                progressBarVisibility.setValue(View.INVISIBLE);
                networkError.setValue(error);
            }
        });

        // todo remove these 3 lines when room service is done
        roomID = 1;
        navigateToAdminRoom.setValue(true);
        progressBarVisibility.setValue(View.INVISIBLE);
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

    @NonNull
    public MutableLiveData<Integer> getProgressBarVisibility() {
        return progressBarVisibility;
    }
}


