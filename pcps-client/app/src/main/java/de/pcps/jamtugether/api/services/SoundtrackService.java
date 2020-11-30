package de.pcps.jamtugether.api.services;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.api.responses.PushSoundtrackResponse;
import de.pcps.jamtugether.api.responses.SoundtrackListResponse;
import de.pcps.jamtugether.content.soundtrack.Soundtrack;
import retrofit2.Call;
import retrofit2.http.GET;

// todo add actual endpoints with correct response classes
public interface SoundtrackService {

    Call<SoundtrackListResponse> getAllSoundtracks(int roomID);

    Call<PushSoundtrackResponse> pushSoundtrack(@NonNull Soundtrack soundtrack, int roomID);
}
