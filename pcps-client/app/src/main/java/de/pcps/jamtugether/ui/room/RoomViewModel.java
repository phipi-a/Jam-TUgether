package de.pcps.jamtugether.ui.room;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import de.pcps.jamtugether.api.JamCallback;
import de.pcps.jamtugether.api.errors.base.Error;
import de.pcps.jamtugether.api.repositories.RoomRepository;
import de.pcps.jamtugether.api.repositories.SoundtrackRepository;
import de.pcps.jamtugether.api.responses.room.RemoveAdminResponse;
import de.pcps.jamtugether.audio.player.SoundtrackController;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.storage.db.LatestSoundtracksDatabase;
import de.pcps.jamtugether.storage.db.SoundtrackNumbersDatabase;
import timber.log.Timber;

public class RoomViewModel extends ViewModel {

    @Inject
    SoundtrackController soundtrackController;

    @Inject
    RoomRepository roomRepository;

    @Inject
    SoundtrackRepository soundtrackRepository;

    @Inject
    SoundtrackNumbersDatabase soundtrackNumbersDatabase;

    @Inject
    LatestSoundtracksDatabase latestSoundtracksDatabase;

    private final int roomID;

    @NonNull
    private final MutableLiveData<Error> networkError = new MutableLiveData<>(null);

    @NonNull
    private final MutableLiveData<Boolean> showLeaveRoomConfirmationDialog = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>(false);

    public RoomViewModel(int roomID, @NonNull String token, boolean userIsAdmin) {
        AppInjector.inject(this);
        this.roomID = roomID;

        roomRepository.updateInfo(token, userIsAdmin);
        roomRepository.fetchAdminStatus(roomID, token);
    }

    public void onLeaveRoomConfirmationDialogShown() {
        showLeaveRoomConfirmationDialog.setValue(false);
    }

    public void onLeaveRoomConfirmationButtonClicked() {
        navigateBack.setValue(true);
        onUserLeft();
    }

    private void onUserLeft() {
        soundtrackController.stopPlayers();
        Boolean userIsAdmin = getUserIsAdmin().getValue();
        if (userIsAdmin != null && userIsAdmin) {
            onAdminLeft();
        }
        roomRepository.onUserLeftRoom();
        soundtrackRepository.onUserLeftRoom();
        soundtrackNumbersDatabase.onUserLeftRoom();
        latestSoundtracksDatabase.onUserLeftRoom();
    }

    private void onAdminLeft() {
        String token = roomRepository.getCurrentToken().getValue();
        if (token == null) {
            return;
        }
        roomRepository.removeAdmin(roomID, token, new JamCallback<RemoveAdminResponse>() {
            @Override
            public void onSuccess(@NonNull RemoveAdminResponse response) {
                Timber.d("onSuccess()");
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

    @NonNull
    public LiveData<String> getToken() {
        return roomRepository.getCurrentToken();
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
        private final String token;

        private final boolean userIsAdmin;

        public Factory(int roomID, @NonNull String token, boolean userIsAdmin) {
            this.roomID = roomID;
            this.token = token;
            this.userIsAdmin = userIsAdmin;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(RoomViewModel.class)) {
                return (T) new RoomViewModel(roomID, token, userIsAdmin);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
