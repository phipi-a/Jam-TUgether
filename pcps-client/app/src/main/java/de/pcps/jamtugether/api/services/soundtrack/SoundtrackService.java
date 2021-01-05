package de.pcps.jamtugether.api.services.soundtrack;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.api.responses.room.DeleteTrackResponse;
import de.pcps.jamtugether.api.services.room.bodies.DeleteTrackBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.Header;

public interface SoundtrackService {

    @NonNull
    @HTTP(method = "DELETE", path = "room", hasBody = true)
    Call<DeleteTrackResponse> deleteTrack(@Header("Authorization") @NonNull String token, @Body @NonNull DeleteTrackBody body);
}