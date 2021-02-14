package de.pcps.jamtugether.ui.menu.join;

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
import de.pcps.jamtugether.api.errors.RoomDeletedError;
import de.pcps.jamtugether.api.errors.base.Error;
import de.pcps.jamtugether.api.errors.PasswordTooLargeError;
import de.pcps.jamtugether.api.errors.UnauthorizedAccessError;
import de.pcps.jamtugether.api.repositories.RoomRepository;
import de.pcps.jamtugether.api.repositories.SoundtrackRepository;
import de.pcps.jamtugether.api.requests.room.join.JoinRoomResponse;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.model.Composition;
import de.pcps.jamtugether.model.User;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.utils.StringUtils;

public class JoinRoomViewModel extends ViewModel {

    @Inject
    Application application;

    @Inject
    RoomRepository roomRepository;

    @Inject
    SoundtrackRepository soundtrackRepository;

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

    public void onNameInfoButtonClicked() {
        showNameInfoDialog.setValue(true);
    }

    public void onJoinRoomButtonClicked(@NonNull String userName, @NonNull String roomIDString, @NonNull String password) {
        Context context = application.getApplicationContext();

        boolean emptyUserName = false;
        boolean emptyRoom = false;
        boolean emptyPassword = false;

        if (StringUtils.isEmpty(userName)) {
            nameInputError.setValue(context.getString(R.string.name_input_empty));
            emptyUserName = true;
        }

        if (StringUtils.isEmpty(roomIDString)) {
            roomInputError.setValue(context.getString(R.string.room_input_empty));
            emptyRoom = true;
        }

        if (StringUtils.isEmpty(password)) {
            passwordInputError.setValue(context.getString(R.string.password_input_empty));
            emptyPassword = true;
        }

        if (emptyUserName || emptyRoom || emptyPassword) {
            if (!emptyUserName) {
                nameInputError.setValue(null);
            }
            if (!emptyRoom) {
                roomInputError.setValue(null);
            }
            if (!emptyPassword) {
                passwordInputError.setValue(null);
            }
            return;
        }

        try {
            roomID = Integer.parseInt(roomIDString);
        } catch (Exception e) {
            roomInputError.setValue(context.getString(R.string.room_invalid));
            return;
        }

        this.password = password;
        joinRoom(roomID, userName, password);
    }

    private void joinRoom(int roomID, @NonNull String userName, @NonNull String password) {
        progressBarVisibility.setValue(View.VISIBLE);

        roomRepository.joinRoom(roomID, password, new JamCallback<JoinRoomResponse>() {
            @Override
            public void onSuccess(@NonNull JoinRoomResponse response) {
                int userID = response.getUserID();
                user = new User(userID, userName);

                token = response.getToken();

                // fetch soundtracks before navigating to room fragment in order to decide in time
                // which tab should be active when user enters room
                // active tab (musician view or overview) depends on whether composition is empty or not
                soundtrackRepository.getComposition(roomID, token, new JamCallback<Composition>() {
                    @Override
                    public void onSuccess(@NonNull Composition response) {
                        progressBarVisibility.setValue(View.INVISIBLE);
                        for (SingleSoundtrack soundtrack : response.getSoundtracks()) {
                            soundtrack.loadSounds(application.getApplicationContext());
                        }
                        soundtrackRepository.setBeat(response.getBeat());
                        soundtrackRepository.setSoundtracks(response.getSoundtracks());
                        navigateToRoom.setValue(true);
                    }

                    @Override
                    public void onError(@NonNull Error error) {
                        progressBarVisibility.setValue(View.INVISIBLE);
                        navigateToRoom.setValue(true);
                    }
                });
            }

            @Override
            public void onError(@NonNull Error error) {
                progressBarVisibility.setValue(View.INVISIBLE);

                Context context = application.getApplicationContext();

                String roomOrPasswordInvalidMessage = context.getString(R.string.room_or_password_invalid_message);

                if (error instanceof UnauthorizedAccessError || error instanceof RoomDeletedError) {
                    roomInputError.setValue(roomOrPasswordInvalidMessage);
                    passwordInputError.setValue(roomOrPasswordInvalidMessage);
                    return;
                }

                if (error instanceof PasswordTooLargeError) {
                    roomInputError.setValue(roomOrPasswordInvalidMessage);
                    passwordInputError.setValue(roomOrPasswordInvalidMessage);
                    return;
                }
                roomInputError.setValue(null);
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

    public void onNavigatedToRegularRoom() {
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
    public String getToken() {
        return token;
    }

    @Nullable
    public String getPassword() {
        return password;
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
