package de.pcps.jamtugether.ui.room.overview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.pcps.jamtugether.api.Constants;
import de.pcps.jamtugether.api.JamCallback;
import de.pcps.jamtugether.api.errors.base.Error;
import de.pcps.jamtugether.api.repositories.RoomRepository;
import de.pcps.jamtugether.api.repositories.SoundtrackRepository;
import de.pcps.jamtugether.api.responses.room.DeleteTrackResponse;
import de.pcps.jamtugether.model.User;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.storage.db.SoundtrackNumbersDatabase;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.utils.TimeUtils;

public class SoundtrackOverviewViewModel extends ViewModel implements SingleSoundtrack.OnDeleteListener {

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
    private final MutableLiveData<Boolean> showNotAdminDialog = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> showAdminSettingsFragment = new MutableLiveData<>(false);

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
                List<SingleSoundtrack> list = new ArrayList<>();
                for (SingleSoundtrack singleSoundtrack : soundtracks) {
                    if (!singleSoundtrack.equals(soundtrack)) {
                        list.add(singleSoundtrack);
                    }
                }
                soundtrackRepository.setSoundtracks(list);
                soundtrackToBeDeleted = null;
            }

            @Override
            public void onError(@NonNull Error error) {
                networkError.setValue(error);
                soundtrackToBeDeleted = null;
            }
        });
    }

    public void onAdminOptionsButtonClicked() {
        Boolean isAdmin = getUserIsAdmin().getValue();
        if(isAdmin != null && isAdmin) {
            showAdminSettingsFragment.setValue(true);
        } else {
            showNotAdminDialog.setValue(true);
        }
    }

    public void onSoundtrackDeletionConfirmButtonClicked() {
        if (soundtrackToBeDeleted != null) {
            deleteSoundtrack(soundtrackToBeDeleted);
        }
    }

    public void onCompositionNetworkErrorShown() {
        soundtrackRepository.onCompositionNetworkErrorShown();
        compositionNetworkErrorShown = true;
    }

    public void onSoundtrackDeletionConfirmDialogShown() {
        showSoundtrackDeletionConfirmDialog.setValue(false);
    }

    public void onNetworkErrorShown() {
        networkError.setValue(null);
    }

    public void onAdminSettingsFragmentShown() {
        showAdminSettingsFragment.setValue(false);
    }

    public void onNotAdminDialogShown() {
        showNotAdminDialog.setValue(false);
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
    public LiveData<Boolean> getShowNotAdminDialog() {
        return showNotAdminDialog;
    }

    @NonNull
    public LiveData<Boolean> getShowAdminSettingsFragment() {
        return showAdminSettingsFragment;
    }

    @NonNull
    public LiveData<Boolean> getShowSoundtrackDeletionConfirmDialog() {
        return showSoundtrackDeletionConfirmDialog;
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
    public LiveData<Error> getNetworkError() {
        return networkError;
    }

    @NonNull
    public LiveData<Error> getCompositionNetworkError() {
        return soundtrackRepository.getCompositionNetworkError();
    }

    @NonNull
    public LiveData<Integer> getCountDownProgress() {
        return Transformations.map(soundtrackRepository.getCountDownTimerMillis(), this::calculateProgress);
    }

    private int calculateProgress(long millis) {
        return (int) ((Constants.SOUNDTRACK_FETCHING_INTERVAL - millis + TimeUtils.ONE_SECOND) / (double) Constants.SOUNDTRACK_FETCHING_INTERVAL * 100);
    }

    public boolean getCompositionNetworkErrorShown() {
        return compositionNetworkErrorShown;
    }
}
