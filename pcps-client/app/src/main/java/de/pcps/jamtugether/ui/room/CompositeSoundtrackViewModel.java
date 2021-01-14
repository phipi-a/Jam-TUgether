package de.pcps.jamtugether.ui.room;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

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

    public CompositeSoundtrackViewModel() {
        AppInjector.inject(this);

        soundtrackRepository.startFetchingSoundtracks(false);
        compositeSoundtrack = soundtrackRepository.getCompositeSoundtrack();
    }

    @NonNull
    public LiveData<CompositeSoundtrack> getCompositeSoundtrack() {
        return compositeSoundtrack;
    }
}
