package de.pcps.jamtugether.api.repositories;

import androidx.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.api.BaseCallback;
import de.pcps.jamtugether.content.soundtrack.Soundtrack;

// todo uncomment when sound track service is done
@Singleton
public class SoundtrackRepository {

    //@Inject
    //SoundtrackService soundtrackService;

    @Inject
    public SoundtrackRepository() {}

    public void getAllSoundtracks(int roomID, @NonNull BaseCallback<List<Soundtrack>> callback) {
        //Call<List<Soundtrack>> call = soundtrackService.getAllSoundtracks(roomID);
        //call.enqueue(callback);
    }

    public void pushSoundtrack(@NonNull Soundtrack soundtrack, int roomID, @NonNull BaseCallback<Boolean> callback) {
        //Call<Boolean> call = soundtrackService.publishSoundtrack(soundtrack, roomID);
        //call.enqueue(callback);
    }
}
