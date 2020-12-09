package de.pcps.jamtugether.api.services.soundtrack;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.api.responses.soundtrack.DeleteSoundtrackResponse;
import de.pcps.jamtugether.api.responses.soundtrack.PushSoundtrackResponse;
import de.pcps.jamtugether.api.responses.soundtrack.SoundtrackListResponse;
import de.pcps.jamtugether.models.soundtrack.Soundtrack;
import retrofit2.Call;

// todo add actual endpoints with correct response classes
public interface SoundtrackService {

    Call<SoundtrackListResponse> getAllSoundtracks(int roomID);

    Call<PushSoundtrackResponse> pushSoundtrack(@NonNull Soundtrack soundtrack, int roomID);

    Call<DeleteSoundtrackResponse> deleteSoundtrack(@NonNull Soundtrack soundtrack);
}
