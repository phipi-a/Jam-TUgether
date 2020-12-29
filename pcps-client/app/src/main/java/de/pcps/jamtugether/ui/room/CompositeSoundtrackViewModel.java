package de.pcps.jamtugether.ui.room;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import de.pcps.jamtugether.api.repositories.SoundtrackRepository;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;

public class CompositeSoundtrackViewModel extends ViewModel {

    @Inject
    SoundtrackRepository soundtrackRepository;

    @NonNull
    private final MutableLiveData<CompositeSoundtrack> compositeSoundtrack = new MutableLiveData<>();

    private CompositeSoundtrack previousCompositeSoundtrack;

    public CompositeSoundtrackViewModel(int roomID) {
        AppInjector.inject(this);

        soundtrackRepository.fetchSoundtracks(roomID);
    }

    public void observeSoundtrackRepository(@NonNull LifecycleOwner lifecycleOwner) {
        // when soundtracks are updated, UI attributes are being reset
        // which is why the previous UI state has to be set manually
        soundtrackRepository.getCompositeSoundtrack().observe(lifecycleOwner, compositeSoundtrack -> {
            if(previousCompositeSoundtrack != null) {
                compositeSoundtrack.setVolume(previousCompositeSoundtrack.getVolume());
                compositeSoundtrack.setState(previousCompositeSoundtrack.getState().getValue());
                compositeSoundtrack.setProgress(previousCompositeSoundtrack.getProgress().getValue());
            }
            previousCompositeSoundtrack = compositeSoundtrack;
            this.compositeSoundtrack.setValue(compositeSoundtrack);
        });
    }

    @NonNull
    public LiveData<CompositeSoundtrack> getCompositeSoundtrack() {
        return compositeSoundtrack;
    }

    public static class Factory implements ViewModelProvider.Factory {

        private final int roomID;

        public Factory(int roomID) {
            this.roomID = roomID;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(CompositeSoundtrackViewModel.class)) {
                return (T) new CompositeSoundtrackViewModel(roomID);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
