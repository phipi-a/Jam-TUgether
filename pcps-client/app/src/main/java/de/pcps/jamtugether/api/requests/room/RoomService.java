package de.pcps.jamtugether.api.requests.room;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.api.requests.room.create.CreateRoomResponse;
import de.pcps.jamtugether.api.requests.room.delete.DeleteRoomResponse;
import de.pcps.jamtugether.api.requests.room.join.JoinRoomResponse;
import de.pcps.jamtugether.api.requests.room.beat.UpdateBeatResponse;
import de.pcps.jamtugether.api.requests.room.create.CreateRoomBody;
import de.pcps.jamtugether.api.requests.room.delete.DeleteRoomBody;
import de.pcps.jamtugether.api.requests.room.join.JoinRoomBody;
import de.pcps.jamtugether.api.requests.room.beat.UpdateBeatBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RoomService {

    @NonNull
    @POST("create-room")
    Call<CreateRoomResponse> createRoom(@Body @NonNull CreateRoomBody body);

    @NonNull
    @POST("login")
    Call<JoinRoomResponse> joinRoom(@Body @NonNull JoinRoomBody body);

    @NonNull
    @HTTP(method = "DELETE", path = "room", hasBody = true)
    Call<DeleteRoomResponse> deleteRoom(@Header("Authorization") @NonNull String token, @Body @NonNull DeleteRoomBody body);

    @NonNull
    @DELETE("room/{roomID}/admin")
    Call<RemoveAdminResponse> removeAdmin(@Header("Authorization") @NonNull String token, @Path("roomID") int roomID);

    @NonNull
    @GET("room/{roomID}/admin")
    Call<AdminStatusResponse> getAdminStatus(@Header("Authorization") @NonNull String token, @Path("roomID") int roomID);

    @NonNull
    @POST("room/{roomID}/beat")
    Call<UpdateBeatResponse> updateBeat(@Header("Authorization") @NonNull String token, @Path("roomID") int roomID, @Body @NonNull UpdateBeatBody body);
}

