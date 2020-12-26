package de.pcps.jamtugether.api.repositories;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.api.Constants;
import de.pcps.jamtugether.api.errors.base.Error;
import de.pcps.jamtugether.api.services.soundtrack.SoundtrackService;
import de.pcps.jamtugether.model.sound.Sound;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.utils.TimeUtils;

// todo add calls
@Singleton
public class SoundtrackRepository {

    @NonNull
    private final SoundtrackService soundtrackService;

    private int currentRoomID;

    private boolean fetching;

    @NonNull
    private final MutableLiveData<List<SingleSoundtrack>> allSoundtracks = new MutableLiveData<>();

    @NonNull
    private final MutableLiveData<Error> networkError = new MutableLiveData<>();

    @Inject
    public SoundtrackRepository(@NonNull SoundtrackService soundtrackService) {
        this.soundtrackService = soundtrackService;
    }

    public void fetchSoundtracks(int currentRoomID) {
        this.currentRoomID = currentRoomID;
        fetchSoundtracks();
        if(!fetching) {
            startFetchingSoundtracks();
            fetching = true;
        }
    }

    private void startFetchingSoundtracks() {
        Handler handler = new Handler();
        new Runnable() {

            @Override
            public void run() {
                fetchSoundtracks();
                handler.postDelayed(this, Constants.SOUNDTRACK_FETCHING_INTERVAL);
            }
        }.run();
    }

    private void fetchSoundtracks() {
        allSoundtracks.setValue(generateTestSoundtracks());
    }

    @NonNull
    private List<SingleSoundtrack> generateTestSoundtracks() {
        Random random = new Random();
        List<SingleSoundtrack> list = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            List<Sound> soundSequence = new ArrayList<>();
            int soundAmount = random.nextInt(30)+10;
            String[] instruments = {"flute", "drums", "shaker"};
            String serverString = "flute";
            for(int j = 0; j < soundAmount; j++) {
                int pitch = random.nextInt(Sound.PITCH_RANGE+1);
                int element = 0;
                soundSequence.add(new Sound(serverString, element, (int) TimeUtils.ONE_SECOND * j, (int) TimeUtils.ONE_SECOND * (j+1), pitch));
            }
            list.add(new SingleSoundtrack(i, soundSequence));
        }
        return list;
    }

    // updates soundtracks locally so the change can be visible immediately
    public void updateAllSoundtracks(@NonNull List<SingleSoundtrack> soundtracks) {
        allSoundtracks.setValue(soundtracks);
    }

    public void onNetworkErrorShown() {
        networkError.setValue(null);
    }

    @NonNull
    public LiveData<List<SingleSoundtrack>> getAllSoundtracks() {
        return allSoundtracks;
    }

    @NonNull
    public LiveData<CompositeSoundtrack> getCompositeSoundtrack() {
        return Transformations.map(getAllSoundtracks(), CompositeSoundtrack::from);
    }

    @NonNull
    public LiveData<Error> getNetworkError() {
        return networkError;
    }
}
