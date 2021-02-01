package de.pcps.jamtugether.ui.room.overview.admin;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import de.pcps.jamtugether.api.JamCallback;
import de.pcps.jamtugether.api.errors.base.Error;
import de.pcps.jamtugether.api.repositories.RoomRepository;
import de.pcps.jamtugether.api.repositories.SoundtrackRepository;
import de.pcps.jamtugether.api.requests.room.delete.DeleteRoomResponse;
import de.pcps.jamtugether.api.requests.room.beat.UpdateBeatResponse;
import de.pcps.jamtugether.audio.metronome.Metronome;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.model.Beat;

public class AdminSettingsViewModel extends ViewModel {

    @Inject
    RoomRepository roomRepository;

    @Inject
    SoundtrackRepository soundtrackRepository;

    @NonNull
    private final MutableLiveData<Boolean> showRoomDeletionConfirmDialog = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Integer> ticksSpinnerSelection;

    @NonNull
    private final MutableLiveData<Integer> tempoSpinnerSelection;

    @NonNull
    private final MutableLiveData<Integer> progressBarVisibility = new MutableLiveData<>(View.INVISIBLE);

    @NonNull
    private final MutableLiveData<Error> networkError = new MutableLiveData<>(null);

    @NonNull
    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>(false);

    public AdminSettingsViewModel() {
        AppInjector.inject(this);

        Beat currentBeat = Metronome.getInstance().getBeat();
        ticksSpinnerSelection = new MutableLiveData<>(currentBeat.getTicksPerTact() - 1);
        tempoSpinnerSelection = new MutableLiveData<>(currentBeat.getTempo() - 1);
    }

    private void deleteRoom() {
        progressBarVisibility.setValue(View.VISIBLE);
        roomRepository.deleteRoom(new JamCallback<DeleteRoomResponse>() {
            @Override
            public void onSuccess(@NonNull DeleteRoomResponse response) {
                progressBarVisibility.setValue(View.INVISIBLE);
                onRoomDeleted();
                navigateBack.setValue(true);
            }

            @Override
            public void onError(@NonNull Error error) {
                progressBarVisibility.setValue(View.INVISIBLE);
                networkError.setValue(error);
            }
        });
    }

    public void onDeleteRoomButtonClicked() {
        showRoomDeletionConfirmDialog.setValue(true);
    }

    public void onRoomDeletionConfirmButtonClicked() {
        deleteRoom();
    }

    public void onMetronomeConfirmButtonClicked(int ticksSpinnerItemPosition, int tempoSpinnerItemPosition) {
        int ticksPerTact = Beat.TICKS_OPTIONS[ticksSpinnerItemPosition];
        int tempo = Beat.TEMPO_OPTIONS[tempoSpinnerItemPosition];
        Beat newBeat = new Beat(ticksPerTact, tempo);
        Beat currentBeat = Metronome.getInstance().getBeat();

        if (!newBeat.equals(currentBeat)) {
            progressBarVisibility.setValue(View.VISIBLE);
            roomRepository.updateBeat(newBeat, new JamCallback<UpdateBeatResponse>() {
                @Override
                public void onSuccess(@NonNull UpdateBeatResponse response) {
                    progressBarVisibility.setValue(View.INVISIBLE);
                    soundtrackRepository.setBeat(newBeat);
                }

                @Override
                public void onError(@NonNull Error error) {
                    progressBarVisibility.setValue(View.INVISIBLE);
                    networkError.setValue(error);
                    Beat currentBeat = Metronome.getInstance().getBeat();
                    ticksSpinnerSelection.setValue(currentBeat.getTicksPerTact() - 1);
                    tempoSpinnerSelection.setValue(currentBeat.getTempo() - 1);
                }
            });
        }
    }

    public void onRoomDeletionConfirmDialogShown() {
        showRoomDeletionConfirmDialog.setValue(false);
    }

    private void onRoomDeleted() {
        roomRepository.onUserLeftRoom();
    }

    public void onNetworkErrorShown() {
        networkError.setValue(null);
    }

    public void onNavigatedBack() {
        navigateBack.setValue(false);
    }

    @NonNull
    public LiveData<Integer> geTicksSpinnerSelection() {
        return ticksSpinnerSelection;
    }

    @NonNull
    public List<Integer> getMetronomeTicksOptions() {
        return Arrays.asList(Beat.TICKS_OPTIONS);
    }

    @NonNull
    public LiveData<Integer> getTempoSpinnerSelection() {
        return tempoSpinnerSelection;
    }

    @NonNull
    public List<Integer> getMetronomeTempoOptions() {
        return Arrays.asList(Beat.TEMPO_OPTIONS);
    }

    @NonNull
    public LiveData<Boolean> getShowRoomDeletionConfirmDialog() {
        return showRoomDeletionConfirmDialog;
    }

    @NonNull
    public LiveData<Integer> getProgressBarVisibility() {
        return progressBarVisibility;
    }

    @NonNull
    public LiveData<Error> getNetworkError() {
        return networkError;
    }

    @NonNull
    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }
}
