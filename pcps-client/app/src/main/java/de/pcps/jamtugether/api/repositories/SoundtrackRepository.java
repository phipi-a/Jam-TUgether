package de.pcps.jamtugether.api.repositories;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.api.Constants;
import de.pcps.jamtugether.api.errors.base.Error;
import de.pcps.jamtugether.api.services.soundtrack.SoundtrackService;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.instrument.drums.Drums;
import de.pcps.jamtugether.audio.instrument.flute.Flute;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;

@Singleton
public class SoundtrackRepository {

    @NonNull
    private final SoundtrackService soundtrackService;

    @NonNull
    private final Context context;

    private int currentRoomID;

    private boolean fetching;

    @NonNull
    private final MutableLiveData<List<SingleSoundtrack>> allSoundtracks = new MutableLiveData<>();

    @NonNull
    private final MutableLiveData<Error> networkError = new MutableLiveData<>();

    @Inject
    public SoundtrackRepository(@NonNull SoundtrackService soundtrackService, @NonNull Context context) {
        this.soundtrackService = soundtrackService;
        this.context = context;
    }

    public void fetchSoundtracks(int currentRoomID) {
        this.currentRoomID = currentRoomID;
        fetchSoundtracks();
        if (!fetching) {
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
        List<SingleSoundtrack> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Instrument instrument = i % 2 == 0 ? Flute.getInstance() : Drums.getInstance();
            SingleSoundtrack singleSoundtrack = instrument.generateSoundtrack(i);
            singleSoundtrack.loadSounds(context);
            list.add(singleSoundtrack);
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
        return Transformations.map(getAllSoundtracks(), allSoundtracks -> CompositeSoundtrack.from(allSoundtracks, context));
    }

    @NonNull
    public LiveData<Error> getNetworkError() {
        return networkError;
    }
}
