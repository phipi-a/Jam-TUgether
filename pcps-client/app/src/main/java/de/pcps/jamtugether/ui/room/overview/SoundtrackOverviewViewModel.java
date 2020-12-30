package de.pcps.jamtugether.ui.room.overview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import de.pcps.jamtugether.audio.player.SoundtrackController;
import de.pcps.jamtugether.audio.player.composite.CompositeSoundtrackPlayer;
import de.pcps.jamtugether.audio.player.single.SingleSoundtrackPlayer;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import de.pcps.jamtugether.ui.room.UserStatusChangeCallback;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;

public class SoundtrackOverviewViewModel extends ViewModel implements SingleSoundtrack.OnDeleteListener {

    @Inject
    Application application;

    @Inject
    RoomRepository roomRepository;

    @Inject
    SoundtrackRepository soundtrackRepository;

    @Inject
    SingleSoundtrackPlayer singleSoundtrackPlayer;

    @Inject
    CompositeSoundtrackPlayer compositeSoundtrackPlayer;

    @Inject
    SoundtrackController soundtrackController;

    private final int roomID;

    @NonNull
    private final String password;

    @NonNull
    private final String token;

    @NonNull
    private final UserStatusChangeCallback userStatusChangeCallback;

    @NonNull
    private final MutableLiveData<Boolean> userIsAdmin;

    @NonNull
    private final MutableLiveData<Error> networkError = new MutableLiveData<>();

    @NonNull
    private final MutableLiveData<Boolean> showSoundtrackDeletionConfirmDialog = new MutableLiveData<>();

    @NonNull
    private final MutableLiveData<Boolean> showRoomDeletionConfirmDialog = new MutableLiveData<>();

    @NonNull
    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();

    @NonNull
    private final List<SingleSoundtrack> previousSoundtracks = new ArrayList<>();

    @Nullable
    private SingleSoundtrack soundtrackToBeDeleted;

    public SoundtrackOverviewViewModel(int roomID, @NonNull String password, @NonNull String token, boolean userIsAdmin, @NonNull UserStatusChangeCallback userStatusChangeCallback) {
        AppInjector.inject(this);
        this.roomID = roomID;
        this.password = password;
        this.token = token;
        this.userIsAdmin = new MutableLiveData<>(userIsAdmin);
        this.userStatusChangeCallback = userStatusChangeCallback;
    }

    public void onNewSoundtracks(@NonNull List<SingleSoundtrack> newSoundtracks) {
        if(!previousSoundtracks.isEmpty()) {
            // as soon as a soundtrack changes it needs to stop playing
            // soundtracks that didn't change keep playing after refresh
            List<SingleSoundtrack> keepPlayingList = getSameSoundtracks(newSoundtracks);
            singleSoundtrackPlayer.stopExcept(keepPlayingList);

            if(keepPlayingList.size() < previousSoundtracks.size()) { // at least one soundtrack changed after fetching
                compositeSoundtrackPlayer.stop();
            }
        }

        this.previousSoundtracks.clear();
        this.previousSoundtracks.addAll(newSoundtracks);
    }

    /**
     * @return a list of soundtracks that weren't updated
     */
    private List<SingleSoundtrack> getSameSoundtracks(@NonNull List<SingleSoundtrack> newSoundtracks) {
        List<SingleSoundtrack> sameSoundtracks = new ArrayList<>();

        for(SingleSoundtrack newSoundtrack : newSoundtracks) {
            if(!(wasUpdated(newSoundtrack))) {
                sameSoundtracks.add(newSoundtrack);
            }
        }
        return sameSoundtracks;
    }

    private boolean wasUpdated(@NonNull SingleSoundtrack soundtrack) {
        for(SingleSoundtrack singleSoundtrack : previousSoundtracks) {
            if(soundtrack.getUserID() == singleSoundtrack.getUserID()) {
                if(!(soundtrack.getSoundSequence().equals(singleSoundtrack.getSoundSequence()))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onDeleteSoundtrackButtonClicked(@NonNull SingleSoundtrack soundtrack) {
        soundtrackToBeDeleted = soundtrack;
        showSoundtrackDeletionConfirmDialog.setValue(true);
    }

    private void deleteSoundtrack(@NonNull SingleSoundtrack soundtrack) {
        List<SingleSoundtrack> soundtracks = getAllSoundtracks().getValue();
        if (soundtracks == null || !soundtracks.contains(soundtrack)) {
            return;
        }

        // delete from local list
        List<SingleSoundtrack> newList = new ArrayList<>();
        for (SingleSoundtrack singleSoundtrack : soundtracks) {
            if (singleSoundtrack != soundtrack) {
                newList.add(singleSoundtrack);
            }
        }
        soundtrackRepository.updateAllSoundtracks(newList);

        // todo tell server

        soundtrackToBeDeleted = null;
    }

    private void deleteRoom() {
        roomRepository.deleteRoom(roomID, password, token, new JamCallback<DeleteRoomResponse>() {
            @Override
            public void onSuccess(@NonNull DeleteRoomResponse response) {
                navigateBack.setValue(true);
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

    public void onSoundtrackRepositoryNetworkErrorShown() {
        soundtrackRepository.onNetworkErrorShown();
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

    public void onRoomDeleted() {
        navigateBack.setValue(false);
        soundtrackController.stopPlayers();
    }

    public int getRoomID() {
        return roomID;
    }

    @NonNull
    public Soundtrack.OnChangeCallback getSoundtrackOnChangeCallback() {
        return soundtrackController;
    }

    @NonNull
    public LiveData<Boolean> getUserIsAdmin() {
        return userIsAdmin;
    }

    @NonNull
    public LiveData<Boolean> getShowSoundtrackDeletionConfirmDialog() {
        return showSoundtrackDeletionConfirmDialog;
    }

    @NonNull
    public LiveData<Boolean> getShowRoomDeletionConfirmDialog() {
        return showRoomDeletionConfirmDialog;
    }

    @NonNull
    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }

    @NonNull
    public LiveData<List<SingleSoundtrack>> getAllSoundtracks() {
        return soundtrackRepository.getAllSoundtracks();
    }

    @NonNull
    public LiveData<Error> getSoundtrackRepositoryNetworkError() {
        return soundtrackRepository.getNetworkError();
    }

    @NonNull
    public LiveData<Error> getNetworkError() {
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
        private final UserStatusChangeCallback userStatusChangeCallback;

        public Factory(int roomID, @NonNull String password, @NonNull String token, boolean admin, @NonNull UserStatusChangeCallback userStatusChangeCallback) {
            this.roomID = roomID;
            this.password = password;
            this.token = token;
            this.admin = admin;
            this.userStatusChangeCallback = userStatusChangeCallback;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(SoundtrackOverviewViewModel.class)) {
                return (T) new SoundtrackOverviewViewModel(roomID, password, token, admin, userStatusChangeCallback);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
