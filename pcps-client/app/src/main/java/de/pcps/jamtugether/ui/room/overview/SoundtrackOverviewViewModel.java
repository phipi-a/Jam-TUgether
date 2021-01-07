package de.pcps.jamtugether.ui.room.overview;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import de.pcps.jamtugether.api.responses.room.DeleteTrackResponse;
import de.pcps.jamtugether.audio.player.SoundtrackController;
import de.pcps.jamtugether.audio.player.composite.CompositeSoundtrackPlayer;
import de.pcps.jamtugether.audio.player.single.SingleSoundtrackPlayer;
import de.pcps.jamtugether.storage.db.LatestSoundtracksDatabase;
import de.pcps.jamtugether.storage.db.SoundtrackNumbersDatabase;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import timber.log.Timber;

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

    @Inject
    SoundtrackNumbersDatabase soundtrackNumbersDatabase;

    @Inject
    LatestSoundtracksDatabase latestSoundtracksDatabase;

    private final int roomID;

    @NonNull
    private final String password;

    @NonNull
    private String token;

    @NonNull
    private final MutableLiveData<Error> networkError = new MutableLiveData<>(null);

    @NonNull
    private final MutableLiveData<Boolean> showSoundtrackDeletionConfirmDialog = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> showRoomDeletionConfirmDialog = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>(false);

    @NonNull
    private final List<SingleSoundtrack> previousSoundtracks = new ArrayList<>();

    @Nullable
    private SingleSoundtrack soundtrackToBeDeleted;

    public SoundtrackOverviewViewModel(int roomID, @NonNull String password, @NonNull String token) {
        AppInjector.inject(this);
        this.roomID = roomID;
        this.password = password;
        this.token = token;
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

        soundtrackNumbersDatabase.onSoundtrackDeleted(soundtrack);

        soundtrackRepository.deleteSoundtrack(token, roomID, soundtrack, new JamCallback<DeleteTrackResponse>() {
            @Override
            public void onSuccess(@NonNull DeleteTrackResponse response) {
                Timber.d("onSuccess() soundtrack deleted");
            }

            @Override
            public void onError(@NonNull Error error) {
                networkError.setValue(error);
            }
        });

        soundtrackToBeDeleted = null;
    }

    private void deleteRoom() {
        roomRepository.deleteRoom(roomID, password, token, new JamCallback<DeleteRoomResponse>() {
            @Override
            public void onSuccess(@NonNull DeleteRoomResponse response) {
                onRoomDeleted();
                navigateBack.setValue(true);
            }

            @Override
            public void onError(@NonNull Error error) {
                networkError.setValue(error);
            }
        });
    }

    public void onTokenChanged(@NonNull String token) {
        this.token = token;
    }

    public void onDeleteRoomButtonClicked() {
        showRoomDeletionConfirmDialog.setValue(true);
    }

    public void onSoundtrackDeletionConfirmButtonClicked() {
        if(soundtrackToBeDeleted != null) {
            deleteSoundtrack(soundtrackToBeDeleted);
        }
    }

    public void onRoomDeletionConfirmButtonClicked() {
        deleteRoom();
    }

    public void onSoundtrackRepositoryNetworkErrorShown() {
        soundtrackRepository.onCompositionNetworkErrorShown();
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

    private void onRoomDeleted() {
        soundtrackController.stopPlayers();
        soundtrackNumbersDatabase.onUserLeftRoom();
        latestSoundtracksDatabase.onUserLeftRoom();
    }

    public void onNavigatedBack() {
        navigateBack.setValue(false);
    }

    public int getRoomID() {
        return roomID;
    }

    @NonNull
    public Soundtrack.OnChangeCallback getSoundtrackOnChangeCallback() {
        return soundtrackController;
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
    public LiveData<Integer> getProgressBarVisibility() {
        return Transformations.map(soundtrackRepository.getShowCompositionIsLoading(), showLoading -> showLoading ? View.VISIBLE : View.INVISIBLE);
    }

    @NonNull
    public LiveData<Error> getSoundtrackRepositoryNetworkError() {
        return soundtrackRepository.getCompositionNetworkError();
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

        public Factory(int roomID, @NonNull String password, @NonNull String token) {
            this.roomID = roomID;
            this.password = password;
            this.token = token;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(SoundtrackOverviewViewModel.class)) {
                return (T) new SoundtrackOverviewViewModel(roomID, password, token);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
