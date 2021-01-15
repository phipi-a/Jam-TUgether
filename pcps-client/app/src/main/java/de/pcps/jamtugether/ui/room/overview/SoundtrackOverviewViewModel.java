package de.pcps.jamtugether.ui.room.overview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import de.pcps.jamtugether.api.Constants;
import de.pcps.jamtugether.api.JamCallback;
import de.pcps.jamtugether.api.errors.base.Error;
import de.pcps.jamtugether.api.repositories.RoomRepository;
import de.pcps.jamtugether.api.repositories.SoundtrackRepository;
import de.pcps.jamtugether.api.responses.room.DeleteRoomResponse;
import de.pcps.jamtugether.api.responses.room.DeleteTrackResponse;
import de.pcps.jamtugether.model.User;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.storage.db.SoundtrackNumbersDatabase;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.ui.room.SoundtracksFetchingCountDownProvider;
import de.pcps.jamtugether.utils.TimeUtils;

public class SoundtrackOverviewViewModel extends ViewModel implements SingleSoundtrack.OnDeleteListener, SoundtracksFetchingCountDownProvider {

    @Inject
    SoundtrackRepository soundtrackRepository;

    @Inject
    RoomRepository roomRepository;

    @Inject
    SoundtrackNumbersDatabase soundtrackNumbersDatabase;

    @NonNull
    private final MutableLiveData<Error> networkError = new MutableLiveData<>(null);

    @NonNull
    private final MutableLiveData<Boolean> showSoundtrackDeletionConfirmDialog = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> showRoomDeletionConfirmDialog = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>(false);

    @Nullable
    private SingleSoundtrack soundtrackToBeDeleted;

    private boolean compositionNetworkErrorShown;

    public SoundtrackOverviewViewModel() {
        AppInjector.inject(this);
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
        soundtrackNumbersDatabase.onSoundtrackDeleted(soundtrack);

        soundtrackRepository.deleteSoundtrack(soundtrack, new JamCallback<DeleteTrackResponse>() {
            @Override
            public void onSuccess(@NonNull DeleteTrackResponse response) {
                // delete from local list in order to be visible immediately
                soundtracks.remove(soundtrack);
                soundtrackRepository.onSoundtracksChanged(soundtracks);
            }

            @Override
            public void onError(@NonNull Error error) {
                networkError.setValue(error);
            }
        });

        soundtrackToBeDeleted = null;
    }

    private void deleteRoom() {
        roomRepository.deleteRoom(new JamCallback<DeleteRoomResponse>() {
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

    public void onDeleteRoomButtonClicked() {
        showRoomDeletionConfirmDialog.setValue(true);
    }

    public void onSoundtrackDeletionConfirmButtonClicked() {
        if (soundtrackToBeDeleted != null) {
            deleteSoundtrack(soundtrackToBeDeleted);
        }
    }

    public void onRoomDeletionConfirmButtonClicked() {
        deleteRoom();
    }

    public void onCompositionNetworkErrorShown() {
        soundtrackRepository.onCompositionNetworkErrorShown();
        compositionNetworkErrorShown = true;
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
        roomRepository.onUserLeftRoom();
    }

    public void onNavigatedBack() {
        navigateBack.setValue(false);
    }

    @Nullable
    public Integer getRoomID() {
        return roomRepository.getRoomID();
    }

    @Nullable
    public Integer getUserID() {
        User user = roomRepository.getUser();
        return user == null ? null : user.getID();
    }

    @NonNull
    public LiveData<Boolean> getUserIsAdmin() {
        return roomRepository.getUserIsAdmin();
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
    public LiveData<CompositeSoundtrack> getCompositeSoundtrack() {
        return soundtrackRepository.getCompositeSoundtrack();
    }

    @NonNull
    public LiveData<Integer> getProgressBarVisibility() {
        return Transformations.map(soundtrackRepository.getIsFetchingComposition(), isFetchingComposition -> isFetchingComposition ? View.VISIBLE : View.GONE);
    }

    @NonNull
    public LiveData<Error> getCompositionNetworkError() {
        return soundtrackRepository.getCompositionNetworkError();
    }

    @NonNull
    public LiveData<Error> getNetworkError() {
        return networkError;
    }

    @NonNull
    @Override
    public LiveData<Integer> getProgress() {
        return Transformations.map(soundtrackRepository.getCountDownTimerMillis(), this::calculateProgress);
    }

    private int calculateProgress(long millis) {
        return (int) ((Constants.SOUNDTRACK_FETCHING_INTERVAL - millis) / (double) Constants.SOUNDTRACK_FETCHING_INTERVAL * 100);
    }

    @NonNull
    @Override
    public LiveData<String> getCountDownText() {
        return Transformations.map(soundtrackRepository.getCountDownTimerMillis(), TimeUtils::formatTimerSecondsSimple);
    }

    public boolean getCompositionNetworkErrorShown() {
        return compositionNetworkErrorShown;
    }
}
