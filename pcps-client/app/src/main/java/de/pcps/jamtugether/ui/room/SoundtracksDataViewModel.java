package de.pcps.jamtugether.ui.room;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import de.pcps.jamtugether.api.Constants;
import de.pcps.jamtugether.api.repositories.SoundtrackRepository;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.model.instrument.base.Instrument;
import de.pcps.jamtugether.model.instrument.base.Instruments;
import de.pcps.jamtugether.model.music.sound.Sound;
import de.pcps.jamtugether.model.music.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.music.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.utils.TimeUtils;

// view model that fetches soundtrack data regularly
// and provides the according live data
public class SoundtracksDataViewModel extends ViewModel {

    @Inject
    SoundtrackRepository soundtrackRepository;

    private final int roomID;

    @NonNull
    private final MutableLiveData<List<SingleSoundtrack>> allSoundtracks = new MutableLiveData<>();

    public SoundtracksDataViewModel(int roomID) {
        AppInjector.inject(this);
        this.roomID = roomID;
        startFetching();
    }

    private void startFetching() {
        Handler handler = new Handler();
        new Runnable() {

            @Override
            public void run() {
                fetch();
                handler.postDelayed(this, Constants.SOUNDTRACK_FETCHING_INTERVAL);
            }
        }.run();
    }

    public void fetch() {
        // todo
        allSoundtracks.setValue(generateTestSoundtracks());
    }

    @NonNull
    private List<SingleSoundtrack> generateTestSoundtracks() {
        Random random = new Random();
        List<SingleSoundtrack> list = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            List<Sound> soundSequence = new ArrayList<>();
            int soundAmount = random.nextInt(30)+10;
            Instrument instrument = Instruments.LIST[i % Instruments.LIST.length];
            for(int j = 0; j < soundAmount; j++) {
                int pitch = random.nextInt(Sound.PITCH_RANGE+1);
                soundSequence.add(new Sound(instrument.getServerString(), (int) TimeUtils.ONE_SECOND * j, (int) TimeUtils.ONE_SECOND * (j+1), pitch));
            }
            list.add(new SingleSoundtrack(i, soundSequence));
        }
        return list;
    }

    @NonNull
    public LiveData<List<SingleSoundtrack>> getAllSoundtracks() {
        return allSoundtracks;
    }

    @NonNull
    public LiveData<CompositeSoundtrack> getCompositeSoundtrack() {
        return Transformations.map(getAllSoundtracks(), CompositeSoundtrack::from);
    }

    public void updateAllSoundtracks(List<SingleSoundtrack> soundtracks) {
        allSoundtracks.setValue(soundtracks);
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
            if (modelClass.isAssignableFrom(SoundtracksDataViewModel.class)) {
                return (T) new SoundtracksDataViewModel(roomID);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
