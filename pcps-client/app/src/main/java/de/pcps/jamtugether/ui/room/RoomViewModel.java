package de.pcps.jamtugether.ui.room;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import de.pcps.jamtugether.api.repositories.SoundtrackRepository;
import de.pcps.jamtugether.audio.player.SoundtrackController;
import de.pcps.jamtugether.di.AppInjector;

public class RoomViewModel extends ViewModel implements UserStatusChangeCallback {

    @Inject
    SoundtrackController soundtrackController;

    @Inject
    SoundtrackRepository soundtrackRepository;

    private final int roomID;

    private boolean userIsAdmin;

    @NonNull
    private final MutableLiveData<Boolean> showLeaveRoomConfirmationDialog = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>(false);

    public RoomViewModel(int roomID, boolean userIsAdmin) {
        AppInjector.inject(this);
        this.roomID = roomID;
        this.userIsAdmin = userIsAdmin;
    }

    public void handleBackPressed() {
        showLeaveRoomConfirmationDialog.setValue(true);
    }

    @Override
    public void onUserStatusChanged(boolean admin) {
        this.userIsAdmin = admin;
    }

    public void onLeaveRoomConfirmationDialogShown() {
        showLeaveRoomConfirmationDialog.setValue(false);
    }

    public void onLeaveRoomConfirmationButtonClicked() {
        navigateBack.setValue(true);
        soundtrackRepository.onUserLeftRoom();
        onUserLeft();
    }

    private void onUserLeft() {
        soundtrackController.stopPlayers();
        // todo tell sever
        //  add admin info
    }

    public void onNavigatedBack() {
        this.navigateBack.setValue(false);
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

        private final boolean userIsAdmin;

        public Factory(int roomID, boolean userIsAdmin) {
            this.roomID = roomID;
            this.userIsAdmin = userIsAdmin;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(RoomViewModel.class)) {
                return (T) new RoomViewModel(roomID, userIsAdmin);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
