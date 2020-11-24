package de.pcps.jamtugether.content.room.users.regular;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class RegularRoomOverviewViewModel extends AndroidViewModel {

    private final int roomID;

    public RegularRoomOverviewViewModel(@NonNull Application application, int roomID) {
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
            if (modelClass.isAssignableFrom(RegularRoomOverviewViewModel.class)) {
                return (T) new RegularRoomOverviewViewModel(application, roomID);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
