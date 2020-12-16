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
import de.pcps.jamtugether.api.JamCallback;
import de.pcps.jamtugether.api.responses.soundtrack.PushSoundtrackResponse;
import de.pcps.jamtugether.api.responses.soundtrack.SoundtrackListResponse;
import de.pcps.jamtugether.model.instrument.base.Instrument;
import de.pcps.jamtugether.model.instrument.base.Instruments;
import de.pcps.jamtugether.model.music.sound.Sound;
import de.pcps.jamtugether.model.music.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.music.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.utils.TimeUtils;

// todo uncomment when sound track service is done
@Singleton
public class SoundtrackRepository {

    //@Inject
    //SoundtrackService soundtrackService;

    private int currentRoomID;

    private boolean fetching;

    @NonNull
    private final MutableLiveData<List<SingleSoundtrack>> allSoundtracks = new MutableLiveData<>();

    @Inject
    public SoundtrackRepository() { }

    public void getAllSoundtracks(int roomID, @NonNull JamCallback<SoundtrackListResponse> callback) {
        //Call<SoundtrackListResponse> call = soundtrackService.getAllSoundtracks(roomID);
        //call.enqueue(callback);
    }

    public void pushSoundtrack(@NonNull SingleSoundtrack singleSoundtrack, int roomID, @NonNull JamCallback<PushSoundtrackResponse> callback) {
        //Call<PushSoundtrackResponse> call = soundtrackService.publishSoundtrack(soundtrack, roomID);
        //call.enqueue(callback);
    }

    public void deleteSoundtrack(@NonNull SingleSoundtrack singleSoundtrack, int roomID, @NonNull JamCallback<PushSoundtrackResponse> callback) {
        //Call<DeleteSoundtrackResponse> call = soundtrackService.deleteSoundtrack(soundtrack, roomID);
        //call.enqueue(callback);
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

    // updates soundtracks locally so the change can be visible immediately
    public void updateAllSoundtracks(@NonNull List<SingleSoundtrack> soundtracks) {
        allSoundtracks.setValue(soundtracks);
    }
}
