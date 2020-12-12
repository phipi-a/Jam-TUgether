package de.pcps.jamtugether.content.room;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RoomViewModel extends ViewModel {

    @NonNull
    private final MutableLiveData<Boolean> showLeaveRoomConfirmationDialog = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>(false);

    public void handleBackPressed() {
        showLeaveRoomConfirmationDialog.setValue(true);
    }

    public void onLeaveRoomConfirmationDialogShown() {
        showLeaveRoomConfirmationDialog.setValue(false);
    }

    public void onLeaveRoomConfirmationButtonClicked() {
        navigateBack.setValue(true);
        onUserLeft();
    }

    private void onUserLeft() {
        // todo tell sever
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
}
