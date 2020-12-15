package de.pcps.jamtugether.api.services.soundtrack;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.api.responses.soundtrack.DeleteSoundtrackResponse;
import de.pcps.jamtugether.api.responses.soundtrack.PushSoundtrackResponse;
import de.pcps.jamtugether.api.responses.soundtrack.SoundtrackListResponse;
import de.pcps.jamtugether.model.music.soundtrack.SingleSoundtrack;
import retrofit2.Call;

// todo add actual endpoints with correct response classes
public interface SoundtrackService {

    Call<SoundtrackListResponse> getAllSoundtracks(int roomID);

    Call<PushSoundtrackResponse> pushSoundtrack(@NonNull SingleSoundtrack singleSoundtrack, int roomID);

    Call<DeleteSoundtrackResponse> deleteSoundtrack(@NonNull SingleSoundtrack singleSoundtrack);
}