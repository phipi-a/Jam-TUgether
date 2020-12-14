package de.pcps.jamtugether.content.room;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class RoomViewModel extends ViewModel implements AdminStatusChangeCallback {

    private boolean admin;

    @NonNull
    private final MutableLiveData<Boolean> showLeaveRoomConfirmationDialog = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>(false);

    public RoomViewModel(boolean admin) {
        this.admin = admin;
    }

    @Override
    public void onAdminStatusChanged(boolean admin) {
        this.admin = admin;
    }

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

        private final boolean admin;

        public Factory(boolean admin) {
            this.admin = admin;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(RoomViewModel.class)) {
                return (T) new RoomViewModel(admin);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
