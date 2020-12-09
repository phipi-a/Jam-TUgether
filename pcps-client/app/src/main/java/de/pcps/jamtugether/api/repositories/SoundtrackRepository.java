package de.pcps.jamtugether.api.repositories;

import androidx.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.api.BaseCallback;
import de.pcps.jamtugether.api.responses.soundtrack.PushSoundtrackResponse;
import de.pcps.jamtugether.api.responses.soundtrack.SoundtrackListResponse;
import de.pcps.jamtugether.models.music.soundtrack.SingleSoundtrack;

// todo uncomment when sound track service is done
@Singleton
public class SoundtrackRepository {

    //@Inject
    //SoundtrackService soundtrackService;

    @Inject
    public SoundtrackRepository() { }

    public void getAllSoundtracks(int roomID, @NonNull BaseCallback<SoundtrackListResponse> callback) {
        //Call<SoundtrackListResponse> call = soundtrackService.getAllSoundtracks(roomID);
        //call.enqueue(callback);
    }

    public void pushSoundtrack(@NonNull SingleSoundtrack singleSoundtrack, int roomID, @NonNull BaseCallback<PushSoundtrackResponse> callback) {
        //Call<PushSoundtrackResponse> call = soundtrackService.publishSoundtrack(soundtrack, roomID);
        //call.enqueue(callback);
    }

    public void deleteSoundtrack(@NonNull SingleSoundtrack singleSoundtrack, int roomID, @NonNull BaseCallback<PushSoundtrackResponse> callback) {
        //Call<DeleteSoundtrackResponse> call = soundtrackService.deleteSoundtrack(soundtrack, roomID);
        //call.enqueue(callback);
    }
}
