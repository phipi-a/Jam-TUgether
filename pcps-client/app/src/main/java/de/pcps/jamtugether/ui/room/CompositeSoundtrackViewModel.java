package de.pcps.jamtugether.ui.room;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import de.pcps.jamtugether.api.repositories.SoundtrackRepository;
import de.pcps.jamtugether.audio.player.SoundtrackController;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;

public class CompositeSoundtrackViewModel extends ViewModel {

    @Inject
    SoundtrackRepository soundtrackRepository;

    @Inject
    SoundtrackController soundtrackController;

    @NonNull
    private final LiveData<CompositeSoundtrack> compositeSoundtrack;

    public CompositeSoundtrackViewModel(int roomID, int userID, @NonNull String token) {
        AppInjector.inject(this);

        soundtrackRepository.fetchSoundtracks(roomID, userID, token, false);
        compositeSoundtrack = soundtrackRepository.getCompositeSoundtrack();
    }

    @NonNull
    public LiveData<CompositeSoundtrack> getCompositeSoundtrack() {
        return compositeSoundtrack;
    }

    public static class Factory implements ViewModelProvider.Factory {

        private final int roomID;
        private final int userID;

        @NonNull
        private final String token;

        public Factory(int roomID, int userID, @NonNull String token) {
            this.roomID = roomID;
            this.userID = userID;
            this.token = token;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(CompositeSoundtrackViewModel.class)) {
                return (T) new CompositeSoundtrackViewModel(roomID, userID, token);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
