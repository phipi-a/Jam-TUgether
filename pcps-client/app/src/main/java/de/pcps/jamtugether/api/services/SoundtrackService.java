package de.pcps.jamtugether.api.services;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.api.responses.DeleteSoundtrackResponse;
import de.pcps.jamtugether.api.responses.PushSoundtrackResponse;
import de.pcps.jamtugether.api.responses.SoundtrackListResponse;
import de.pcps.jamtugether.content.soundtrack.Soundtrack;
import retrofit2.Call;

// todo add actual endpoints with correct response classes
public interface SoundtrackService {

    Call<SoundtrackListResponse> getAllSoundtracks(int roomID);

    Call<PushSoundtrackResponse> pushSoundtrack(@NonNull Soundtrack soundtrack, int roomID);

    Call<DeleteSoundtrackResponse> deleteSoundtrack(@NonNull Soundtrack soundtrack);
}
