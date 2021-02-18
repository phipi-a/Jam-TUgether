package de.pcps.jamtugether.ui.menu.create;

import android.app.Application;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.api.JamCallback;
import de.pcps.jamtugether.api.errors.base.Error;
import de.pcps.jamtugether.api.errors.PasswordTooLargeError;
import de.pcps.jamtugether.api.repositories.RoomRepository;
import de.pcps.jamtugether.api.requests.room.create.CreateRoomResponse;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.model.User;
import de.pcps.jamtugether.utils.StringUtils;
import timber.log.Timber;

public class CreateRoomViewModel extends ViewModel {

    @Inject
    Application application;

    @Inject
    RoomRepository roomRepository;

    private int roomID;

    @Nullable
    private User user;

    @Nullable
    private String password;

    @Nullable
    private String token;

    @NonNull
    private final MutableLiveData<Boolean> showNameInfoDialog = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> navigateToRoom = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<String> nameInputError = new MutableLiveData<>(null);

    @NonNull
    private final MutableLiveData<String> passwordInputError = new MutableLiveData<>(null);

    @NonNull
    private final MutableLiveData<Error> networkError = new MutableLiveData<>(null);

    @NonNull
    private final MutableLiveData<Integer> progressBarVisibility = new MutableLiveData<>(View.INVISIBLE);

    public CreateRoomViewModel() {
        AppInjector.inject(this);
    }

    public void onNameInfoButtonClicked() {
        showNameInfoDialog.setValue(true);
    }

    public void onCreateRoomButtonClicked(@NonNull String userName, @NonNull String password) {
        Context context = application.getApplicationContext();

        boolean emptyUserName = false;
        boolean emptyPassword = false;

        if (StringUtils.isEmpty(userName)) {
            nameInputError.setValue(context.getString(R.string.name_input_empty));
            emptyUserName = true;
        }

        if (StringUtils.isEmpty(password)) {
            passwordInputError.setValue(context.getString(R.string.password_input_empty));
            emptyPassword = true;
        }

        if (emptyUserName || emptyPassword) {
            if (!emptyUserName) {
                nameInputError.setValue(null);
            }
            if (!emptyPassword) {
                passwordInputError.setValue(null);
            }
            return;
        }

        this.password = password;
        createRoom(userName, password);
    }

    private void createRoom(@NonNull String userName, @NonNull String password) {
        progressBarVisibility.setValue(View.VISIBLE);

        roomRepository.createRoom(password, new JamCallback<CreateRoomResponse>() {
            @Override
            public void onSuccess(@NonNull CreateRoomResponse response) {
                progressBarVisibility.setValue(View.INVISIBLE);

                roomID = response.getRoomID();

                int userID = response.getUserID();
                user = new User(userID, userName);

                token = response.getToken();

                navigateToRoom.setValue(true);
            }

            @Override
            public void onError(@NonNull Error error) {
                progressBarVisibility.setValue(View.INVISIBLE);

                Context context = application.getApplicationContext();

                if (error instanceof PasswordTooLargeError) {
                    passwordInputError.setValue(context.getString(error.getMessage()));
                    return;
                }
                passwordInputError.setValue(null);

                networkError.setValue(error);
            }
        });
    }

    public void onNameInfoDialogShown() {
        showNameInfoDialog.setValue(false);
    }

    public void onNetworkErrorShown() {
        networkError.setValue(null);
    }

    public void onNavigatedToAdminRoom() {
        navigateToRoom.setValue(false);
    }

    public int getRoomID() {
        return roomID;
    }

    @Nullable
    public User getUser() {
        return user;
    }

    @Nullable
    public String getPassword() {
        return password;
    }

    @Nullable
    public String getToken() {
        return token;
    }

    @NonNull
    public LiveData<Boolean> getShowNameInfoDialog() {
        return showNameInfoDialog;
    }

    @NonNull
    public LiveData<Boolean> getNavigateToRoom() {
        return navigateToRoom;
    }

    @NonNull
    public LiveData<String> getNameInputError() {
        return nameInputError;
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


