package de.pcps.jamtugether.content.room.users.admin;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AdminRoomOverviewViewModel extends AndroidViewModel {

    private final int roomID;

    public AdminRoomOverviewViewModel(@NonNull Application application, int roomID) {
        super(application);
        this.roomID = roomID;
    }

    public int getRoomID() {
        return roomID;
    }

    static class Factory implements ViewModelProvider.Factory {

        @NonNull
        private final Application application;

        private final int roomID;

        public Factory(@NonNull Application application, int roomID) {
            this.application = application;
            this.roomID = roomID;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(AdminRoomOverviewViewModel.class)) {
                return (T) new AdminRoomOverviewViewModel(application, roomID);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
