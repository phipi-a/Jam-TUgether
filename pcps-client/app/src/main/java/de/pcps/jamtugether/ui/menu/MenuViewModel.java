package de.pcps.jamtugether.ui.menu;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.model.User;
import de.pcps.jamtugether.ui.room.RoomViewModel;

public class MenuViewModel extends ViewModel {

    @NonNull
    private final MutableLiveData<Boolean> navigateToSettings = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> navigateToCreateRoom = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> navigateToJoinRoom = new MutableLiveData<>(false);

    @NonNull
    public LiveData<Integer> getShowErrorMessage() {
        return showErrorMessage;
    }

    @NonNull
    private final MutableLiveData<Integer> showErrorMessage = new MutableLiveData<>(-1);

    public void onSettingsButtonClicked() {
        navigateToSettings.setValue(true);
    }

    public void onCreateRoomButtonClicked() {
        navigateToCreateRoom.setValue(true);
    }

    public void onJoinRoomButtonClicked() {
        navigateToJoinRoom.setValue(true);
    }

    public void onNavigatedToSettings() {
        navigateToSettings.setValue(false);
    }

    public void onNavigatedToCreateRoom() {
        navigateToCreateRoom.setValue(false);
    }

    public void onNavigatedToJoinRoom() {
        navigateToJoinRoom.setValue(false);
    }

    public void onErrorMessageSnackbarShown() {
        showErrorMessage.setValue(-1);
    }

    public void onGetErrorMessage(int errorMessage) {
        showErrorMessage.setValue(errorMessage);
    }

    @NonNull
    public LiveData<Boolean> getNavigateToSettings() {
        return navigateToSettings;
    }

    @NonNull
    public LiveData<Boolean> getNavigateToCreateRoom() {
        return navigateToCreateRoom;
    }

    @NonNull
    public LiveData<Boolean> getNavigateToJoinRoom() {
        return navigateToJoinRoom;
    }
    public static class Factory implements ViewModelProvider.Factory {

        private final int errorMessage;

        public Factory(int errorMessage) {
            this.errorMessage=errorMessage;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(MenuViewModel.class)) {
                return (T) new MenuViewModel(errorMessage);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }




    public MenuViewModel(int errorMessage) {
        AppInjector.inject(this);
        if (errorMessage!=-1) {
            onGetErrorMessage(errorMessage);
        }
    }


}
