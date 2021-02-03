package de.pcps.jamtugether.api.requests.soundtrack;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.api.requests.room.soundtrack.DeleteSoundtrackResponse;
import de.pcps.jamtugether.api.requests.room.soundtrack.DeleteSoundtrackBody;
import de.pcps.jamtugether.model.Composition;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SoundtrackService {

    @NonNull
    @GET("room/{roomID}")
    Call<Composition> getComposition(@Header("Authorization") @NonNull String token, @Path("roomID") int roomID);

    @NonNull
    @POST("room/{roomID}")
    Call<UploadSoundtracksResponse> uploadSoundtracks(@Header("Authorization") @NonNull String token, @Path("roomID") int roomID, @Body @NonNull UploadSoundtracksBody body);

    @NonNull
    @HTTP(method = "DELETE", path = "room/{roomID}", hasBody = true)
    Call<DeleteSoundtrackResponse> deleteSoundtrack(@Header("Authorization") @NonNull String token, @Path("roomID") int roomID, @Body @NonNull DeleteSoundtrackBody body);
}