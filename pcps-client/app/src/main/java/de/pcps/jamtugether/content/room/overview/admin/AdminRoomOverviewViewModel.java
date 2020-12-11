package de.pcps.jamtugether.content.room.overview.admin;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.api.BaseCallback;
import de.pcps.jamtugether.api.errors.Error;
import de.pcps.jamtugether.api.responses.room.DeleteRoomResponse;
import de.pcps.jamtugether.content.room.overview.RoomOverviewViewModel;
import de.pcps.jamtugether.models.music.soundtrack.SingleSoundtrack;

public class AdminRoomOverviewViewModel extends RoomOverviewViewModel implements SingleSoundtrack.OnDeleteListener {

    @NonNull
    private final String password;

    @NonNull
    private final MutableLiveData<Boolean> showRoomDeletionConfirmDialog = new MutableLiveData<>();

    public AdminRoomOverviewViewModel(int roomID, @NonNull String password, @NonNull String token) {
        super(roomID, token);
        this.password = password;
    }

    @Override
    public void onDeleteButtonClicked(@NonNull SingleSoundtrack soundtrack) {
        deleteSoundtrack(soundtrack);
    }

    private void deleteSoundtrack(@NonNull SingleSoundtrack soundtrack) {
        if(allSoundtracks.getValue() == null || !allSoundtracks.getValue().contains(soundtrack)) {
            return;
        }
        // delete in local list
        allSoundtracks.getValue().remove(soundtrack);
        // todo tell server
    }

    public void onDeleteRoomButtonClicked() {
        showRoomDeletionConfirmDialog.setValue(true);
    }

    public void onRoomDeletionConfirmDialogShown() {
        showRoomDeletionConfirmDialog.setValue(false);
    }

    public void onRoomDeletionConfirmButtonClicked() {
        deleteRoom();
    }

    private void deleteRoom() {
        roomRepository.deleteRoom(roomID, password, token, new BaseCallback<DeleteRoomResponse>() {
            @Override
            public void onSuccess(@NonNull DeleteRoomResponse response) {
                leaveRoom.setValue(true);
            }

            @Override
            public void onError(@NonNull Error error) {
                networkError.setValue(error);
            }
        });
    }

    @NonNull
    public LiveData<Boolean> getShowRoomDeletionConfirmDialog() {
        return showRoomDeletionConfirmDialog;
    }

    static class Factory implements ViewModelProvider.Factory {

        private final int roomID;

        @NonNull
        private final String password;

        @NonNull
        private final String token;

        public Factory(int roomID, @NonNull String password, @NonNull String token) {
            this.roomID = roomID;
            this.password = password;
            this.token = token;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(AdminRoomOverviewViewModel.class)) {
                return (T) new AdminRoomOverviewViewModel(roomID, password, token);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
