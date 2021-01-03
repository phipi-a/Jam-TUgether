package de.pcps.jamtugether.api.services.soundtrack;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.model.Composition;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SoundtrackService {

    @NonNull
    @GET("room/{roomID}")
    Call<Composition> getComposition(@Header("Authorization") @NonNull String token, @Path("roomID") int roomID);

    @NonNull
    @POST("room/{roomID}")
    Call<String> uploadSoundtrack(@Header("Authorization") @NonNull String token, @Path("roomID") int roomID, @Body @NonNull SingleSoundtrack soundtrack);
}