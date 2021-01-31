package de.pcps.jamtugether.ui.room;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import javax.inject.Inject;

import de.pcps.jamtugether.api.JamCallback;
import de.pcps.jamtugether.api.errors.base.Error;
import de.pcps.jamtugether.api.repositories.RoomRepository;
import de.pcps.jamtugether.api.repositories.SoundtrackRepository;
import de.pcps.jamtugether.api.responses.room.RemoveAdminResponse;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.model.User;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.storage.db.SoundtrackNumbersDatabase;

public class RoomViewModel extends ViewModel {

    @Inject
    RoomRepository roomRepository;

    @Inject
    SoundtrackRepository soundtrackRepository;

    @Inject
    SoundtrackNumbersDatabase soundtrackNumbersDatabase;

    @NonNull
    private final MutableLiveData<Error> networkError = new MutableLiveData<>(null);

    @NonNull
    private final MutableLiveData<Boolean> showLeaveRoomConfirmationDialog = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>(false);

    private final boolean userEnteredAsAdmin;

    public RoomViewModel(int roomID, @NonNull String password, @NonNull User user, @NonNull String token, boolean userIsAdmin) {
        AppInjector.inject(this);
        roomRepository.onUserEnteredRoom(roomID, password, user, token, userIsAdmin);
        this.userEnteredAsAdmin = userIsAdmin;

        roomRepository.startFetchingAdminStatus();
        soundtrackRepository.startFetchingComposition();
    }

    public void onLeaveRoomConfirmationDialogShown() {
        showLeaveRoomConfirmationDialog.setValue(false);
    }

    public void onLeaveRoomConfirmationButtonClicked() {
        navigateBack.setValue(true);
        onUserLeftRoom();
    }

    private void onUserLeftRoom() {
        roomRepository.onUserLeftRoom();

        Boolean userIsAdmin = getUserIsAdmin().getValue();
        if (userIsAdmin != null && userIsAdmin) {
            onAdminLeft();
        }
    }

    private void onAdminLeft() {
        roomRepository.removeAdmin(new JamCallback<RemoveAdminResponse>() {
            @Override
            public void onSuccess(@NonNull RemoveAdminResponse response) {
            }

            @Override
            public void onError(@NonNull Error error) {
                networkError.setValue(error);
            }
        });
    }

    public void handleBackPressed() {
        showLeaveRoomConfirmationDialog.setValue(true);
    }

    public void onNetworkErrorShown() {
        networkError.setValue(null);
    }

    public void onNavigatedBack() {
        this.navigateBack.setValue(false);
    }

    /**
     * @return 0 if musician view should be shown when user enters room
     * 1 if soundtrack overview should be shown when user enters room
     */
    public int getInitialTabPosition() {
        List<SingleSoundtrack> soundtracks = soundtrackRepository.getAllSoundtracks().getValue();
        if (soundtracks != null && !soundtracks.isEmpty()) {
            return 0;
        }
        return 1;
    }

    @NonNull
    public LiveData<String> getToken() {
        return roomRepository.getToken();
    }

    @NonNull
    public LiveData<Boolean> getUserIsAdmin() {
        return roomRepository.getUserIsAdmin();
    }

    @NonNull
    public LiveData<Error> getNetworkError() {
        return networkError;
    }

    @NonNull
    public LiveData<Boolean> getShowUserBecameAdminSnackbar() {
        return Transformations.map(getUserIsAdmin(), userIsAdmin -> userIsAdmin && !userEnteredAsAdmin);
    }

    @NonNull
    public LiveData<Boolean> getShowLeaveRoomConfirmationDialog() {
        return showLeaveRoomConfirmationDialog;
    }

    @NonNull
    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }

    public static class Factory implements ViewModelProvider.Factory {

        private final int roomID;

        @NonNull
        private final String password;

        @NonNull
        private final User user;

        @NonNull
        private final String token;

        private final boolean userIsAdmin;

        public Factory(int roomID, @NonNull String password, @NonNull User user, @NonNull String token, boolean userIsAdmin) {
            this.roomID = roomID;
            this.user = user;
            this.password = password;
            this.token = token;
            this.userIsAdmin = userIsAdmin;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(RoomViewModel.class)) {
                return (T) new RoomViewModel(roomID, password, user, token, userIsAdmin);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
