package de.pcps.jamtugether.content.room.users.admin;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AdminRoomOverviewViewModel extends ViewModel {

    private final int roomID;

    public AdminRoomOverviewViewModel(int roomID) {
        this.roomID = roomID;
    }

    public int getRoomID() {
        return roomID;
    }

    static class Factory implements ViewModelProvider.Factory {

        private final int roomID;

        public Factory(int roomID) {
            this.roomID = roomID;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(AdminRoomOverviewViewModel.class)) {
                return (T) new AdminRoomOverviewViewModel(roomID);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}