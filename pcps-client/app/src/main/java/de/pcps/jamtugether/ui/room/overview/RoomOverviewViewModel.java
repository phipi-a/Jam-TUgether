package de.pcps.jamtugether.ui.room.overview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.pcps.jamtugether.api.JamCallback;
import de.pcps.jamtugether.api.errors.base.Error;
import de.pcps.jamtugether.api.repositories.RoomRepository;
import de.pcps.jamtugether.api.repositories.SoundtrackRepository;
import de.pcps.jamtugether.api.responses.room.DeleteRoomResponse;
import de.pcps.jamtugether.ui.room.SoundtracksDataViewModel;
import de.pcps.jamtugether.ui.room.UserStatusChangeCallback;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.model.music.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.music.soundtrack.SingleSoundtrack;

public class RoomOverviewViewModel extends ViewModel implements SingleSoundtrack.OnDeleteListener {

    @Inject
    Application application;

    @Inject
    RoomRepository roomRepository;

    @Inject
    SoundtrackRepository soundtrackRepository;

    private final int roomID;

    @NonNull
    private final String password;

    @NonNull
    private final String token;

    @NonNull
    private final SoundtracksDataViewModel soundtracksDataViewModel;

    @NonNull
    private final UserStatusChangeCallback userStatusChangeCallback;

    @NonNull
    private final MutableLiveData<Boolean> admin;

    @NonNull
    private final MutableLiveData<Error> networkError = new MutableLiveData<>();

    @NonNull
    private final MutableLiveData<Boolean> showSoundtrackDeletionConfirmDialog = new MutableLiveData<>();

    @NonNull
    private final MutableLiveData<Boolean> showRoomDeletionConfirmDialog = new MutableLiveData<>();

    @NonNull
    private final MutableLiveData<Boolean> leaveRoom = new MutableLiveData<>();

    private SingleSoundtrack soundtrackToBeDeleted;

    public RoomOverviewViewModel(int roomID, @NonNull String password, @NonNull String token, boolean admin, @NonNull SoundtracksDataViewModel soundtracksDataViewModel, @NonNull UserStatusChangeCallback userStatusChangeCallback) {
        AppInjector.inject(this);
        this.roomID = roomID;
        this.password = password;
        this.token = token;
        this.admin = new MutableLiveData<>(admin);
        this.soundtracksDataViewModel = soundtracksDataViewModel;
        this.userStatusChangeCallback = userStatusChangeCallback;
    }

    @Override
    public void onDeleteSoundtrackButtonClicked(@NonNull SingleSoundtrack soundtrack) {
        soundtrackToBeDeleted = soundtrack;
        showSoundtrackDeletionConfirmDialog.setValue(true);
    }

    private void deleteSoundtrack(@NonNull SingleSoundtrack soundtrack) {
        List<SingleSoundtrack> soundtracks = getAllSoundtracks().getValue();
        if(soundtracks == null || !soundtracks.contains(soundtrack)) {
            return;
        }

        // delete from local list
        List<SingleSoundtrack> newList = new ArrayList<>();
        for(SingleSoundtrack singleSoundtrack : soundtracks) {
            if(singleSoundtrack != soundtrack) {
                newList.add(singleSoundtrack);
            }
        }
        soundtracksDataViewModel.updateAllSoundtracks(newList);

        // todo tell server

        soundtrackToBeDeleted = null;
    }

    private void deleteRoom() {
        roomRepository.deleteRoom(roomID, password, token, new JamCallback<DeleteRoomResponse>() {
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

    public void onDeleteRoomButtonClicked() {
        showRoomDeletionConfirmDialog.setValue(true);
    }

    public void onSoundtrackDeletionConfirmButtonClicked() {
        deleteSoundtrack(soundtrackToBeDeleted);
    }

    public void onRoomDeletionConfirmButtonClicked() {
        deleteRoom();
    }

    public void onNetworkErrorShown() {
        networkError.setValue(null);
    }

    public void onSoundtrackDeletionConfirmDialogShown() {
        showSoundtrackDeletionConfirmDialog.setValue(false);
    }

    public void onRoomDeletionConfirmDialogShown() {
        showRoomDeletionConfirmDialog.setValue(false);
    }

    public void onLeftRoom() {
        leaveRoom.setValue(false);
    }

    public int getRoomID() {
        return roomID;
    }

    @NonNull
    public LiveData<Boolean> getAdmin() {
        return admin;
    }

    @NonNull
    public LiveData<Boolean> getShowSoundtrackDeletionConfirmDialog() {
        return showSoundtrackDeletionConfirmDialog;
    }

    @NonNull
    public LiveData<Boolean> getShowRoomDeletionConfirmDialog() {
        return showRoomDeletionConfirmDialog;
    }

    public LiveData<Boolean> getLeaveRoom() {
        return leaveRoom;
    }

    @NonNull
    public LiveData<CompositeSoundtrack> getCompositeSoundtrack() {
        return soundtracksDataViewModel.getCompositeSoundtrack();
    }

    @NonNull
    public LiveData<List<SingleSoundtrack>> getAllSoundtracks() {
        return soundtracksDataViewModel.getAllSoundtracks();
    }

    @NonNull
    public MutableLiveData<Error> getNetworkError() {
        return networkError;
    }

    static class Factory implements ViewModelProvider.Factory {

        private final int roomID;

        @NonNull
        private final String password;

        @NonNull
        private final String token;

        private final boolean admin;

        @NonNull
        private final SoundtracksDataViewModel soundtracksDataViewModel;

        @NonNull
        private final UserStatusChangeCallback userStatusChangeCallback;

        public Factory(int roomID, @NonNull String password, @NonNull String token, boolean admin, @NonNull SoundtracksDataViewModel soundtracksDataViewModel, @NonNull UserStatusChangeCallback userStatusChangeCallback) {
            this.roomID = roomID;
            this.password = password;
            this.token = token;
            this.admin = admin;
            this.soundtracksDataViewModel = soundtracksDataViewModel;
            this.userStatusChangeCallback = userStatusChangeCallback;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(RoomOverviewViewModel.class)) {
                return (T) new RoomOverviewViewModel(roomID, password, token, admin, soundtracksDataViewModel, userStatusChangeCallback);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
