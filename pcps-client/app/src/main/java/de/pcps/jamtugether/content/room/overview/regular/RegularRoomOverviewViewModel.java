package de.pcps.jamtugether.content.room.overview.regular;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.content.room.overview.RoomOverviewViewModel;

public class RegularRoomOverviewViewModel extends RoomOverviewViewModel {

    public RegularRoomOverviewViewModel(int roomID, @NonNull String token) {
        super(roomID, token);
    }

    static class Factory implements ViewModelProvider.Factory {

        private final int roomID;

        @NonNull
        private final String token;

        public Factory(int roomID, @NonNull String token) {
            this.roomID = roomID;
            this.token = token;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(RegularRoomOverviewViewModel.class)) {
                return (T) new RegularRoomOverviewViewModel(roomID, token);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
