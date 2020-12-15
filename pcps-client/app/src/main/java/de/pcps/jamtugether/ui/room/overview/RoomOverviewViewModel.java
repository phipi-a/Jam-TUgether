package de.pcps.jamtugether.ui.room.overview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
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
import de.pcps.jamtugether.ui.room.AdminStatusChangeCallback;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.model.music.soundtrack.base.Soundtrack;
import de.pcps.jamtugether.model.music.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.music.soundtrack.SingleSoundtrack;

public class RoomOverviewViewModel extends ViewModel implements Soundtrack.OnChangeListener, SingleSoundtrack.OnDeleteListener {

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
    private final AdminStatusChangeCallback adminStatusChangeCallback;

    @NonNull
    private final MutableLiveData<Boolean> admin;

    @NonNull
    private final MutableLiveData<List<SingleSoundtrack>> allSoundtracks = new MutableLiveData<>(generateTestSoundtracks());

    @NonNull
    private final MutableLiveData<Error> networkError = new MutableLiveData<>();

    @NonNull
    private final MutableLiveData<Boolean> showSoundtrackDeletionConfirmDialog = new MutableLiveData<>();

    @NonNull
    private final MutableLiveData<Boolean> showRoomDeletionConfirmDialog = new MutableLiveData<>();

    @NonNull
    private final MutableLiveData<Boolean> leaveRoom = new MutableLiveData<>();

    private SingleSoundtrack soundtrackToBeDeleted;

    public RoomOverviewViewModel(int roomID, @NonNull String password, @NonNull String token, boolean admin, @NonNull AdminStatusChangeCallback adminStatusChangeCallback) {
        AppInjector.inject(this);
        this.roomID = roomID;
        this.password = password;
        this.token = token;
        this.admin = new MutableLiveData<>(admin);
        this.adminStatusChangeCallback = adminStatusChangeCallback;
    }

    @NonNull
    private List<SingleSoundtrack> generateTestSoundtracks() {
        List<SingleSoundtrack> list = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            list.add(new SingleSoundtrack(i));
        }
        return list;
    }

    private void fetchAllSoundtracks() {
        // todo get all soundtracks from server and update current list after
    }

    @Override
    public void onVolumeChanged(@NonNull Soundtrack soundtrack, float volume) {
        soundtrack.setVolume(volume);
    }

    @Override
    public void onPlayPauseButtonClicked(@NonNull Soundtrack soundtrack) { }

    @Override
    public void onStopButtonClicked(@NonNull Soundtrack soundtrack) { }

    @Override
    public void onFastForwardButtonClicked(@NonNull Soundtrack soundtrack) { }

    @Override
    public void onFastRewindButtonClicked(@NonNull Soundtrack soundtrack) { }

    @Override
    public void onDeleteButtonClicked(@NonNull SingleSoundtrack soundtrack) {
        soundtrackToBeDeleted = soundtrack;
        showSoundtrackDeletionConfirmDialog.setValue(true);
    }

    private void deleteSoundtrack(@NonNull SingleSoundtrack soundtrack) {
        List<SingleSoundtrack> soundtracks = allSoundtracks.getValue();
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
        allSoundtracks.setValue(newList);

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
        return Transformations.map(getAllSoundtracks(), CompositeSoundtrack::from);
    }

    @NonNull
    public LiveData<List<SingleSoundtrack>> getAllSoundtracks() {
        return allSoundtracks;
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
        private final AdminStatusChangeCallback adminStatusChangeCallback;

        public Factory(int roomID, @NonNull String password, @NonNull String token, boolean admin, @NonNull AdminStatusChangeCallback adminStatusChangeCallback) {
            this.roomID = roomID;
            this.password = password;
            this.token = token;
            this.admin = admin;
            this.adminStatusChangeCallback = adminStatusChangeCallback;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(RoomOverviewViewModel.class)) {
                return (T) new RoomOverviewViewModel(roomID, password, token, admin, adminStatusChangeCallback);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
