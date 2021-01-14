package de.pcps.jamtugether.api.repositories;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.api.JamCallback;
import de.pcps.jamtugether.api.Constants;
import de.pcps.jamtugether.api.errors.base.Error;
import de.pcps.jamtugether.api.responses.room.AdminStatusResponse;
import de.pcps.jamtugether.api.responses.room.CreateRoomResponse;
import de.pcps.jamtugether.api.responses.room.DeleteRoomResponse;
import de.pcps.jamtugether.api.responses.room.JoinRoomResponse;
import de.pcps.jamtugether.api.responses.room.RemoveAdminResponse;
import de.pcps.jamtugether.api.services.room.RoomService;
import de.pcps.jamtugether.api.services.room.bodies.CreateRoomBody;
import de.pcps.jamtugether.api.services.room.bodies.DeleteRoomBody;
import de.pcps.jamtugether.api.services.room.bodies.JoinRoomBody;
import de.pcps.jamtugether.model.User;
import retrofit2.Call;
import timber.log.Timber;

@Singleton
public class RoomRepository {

    @NonNull
    private final RoomService roomService;

    @NonNull
    private final MutableLiveData<Boolean> userInRoom = new MutableLiveData<>(false);

    @Nullable
    private Integer roomID;

    @Nullable
    private String password;

    @Nullable
    private User user;

    @NonNull
    private final MutableLiveData<String> token = new MutableLiveData<>(null);

    @NonNull
    private final MutableLiveData<Boolean> userIsAdmin = new MutableLiveData<>(false);

    @NonNull
    private final Handler handler = new Handler();

    @Nullable
    private Runnable adminStatusFetchingRunnable;

    @Inject
    public RoomRepository(@NonNull RoomService roomService) {
        this.roomService = roomService;
    }

    public void createRoom(@NonNull String password, @NonNull JamCallback<CreateRoomResponse> callback) {
        CreateRoomBody body = new CreateRoomBody(password);
        Call<CreateRoomResponse> call = roomService.createRoom(body);
        call.enqueue(callback);
    }

    public void joinRoom(int roomID, @NonNull String password, @NonNull JamCallback<JoinRoomResponse> callback) {
        JoinRoomBody body = new JoinRoomBody(roomID, password);
        Call<JoinRoomResponse> call = roomService.joinRoom(body);
        call.enqueue(callback);
    }

    public void deleteRoom(@NonNull JamCallback<DeleteRoomResponse> callback) {
        String token = this.token.getValue();
        if(roomID == null || password == null || token == null) {
            return;
        }
        DeleteRoomBody body = new DeleteRoomBody(roomID, password);
        Call<DeleteRoomResponse> call = roomService.deleteRoom(String.format(Constants.BEARER_TOKEN_FORMAT, token), body);
        call.enqueue(callback);
    }

    public void removeAdmin(@NonNull JamCallback<RemoveAdminResponse> callback) {
        String token = this.token.getValue();
        if (roomID == null || token == null) {
            return;
        }
        Call<RemoveAdminResponse> call = roomService.removeAdmin(String.format(Constants.BEARER_TOKEN_FORMAT, token), roomID);
        call.enqueue(callback);
    }

    private void getAdminStatus(int roomID, @NonNull String token, @NonNull JamCallback<AdminStatusResponse> callback) {
        Call<AdminStatusResponse> call = roomService.getAdminStatus(String.format(Constants.BEARER_TOKEN_FORMAT, token), roomID);
        call.enqueue(callback);
    }

    public void setUserInRoom(boolean userInRoom) {
        this.userInRoom.setValue(userInRoom);
        if (!userInRoom) {
            onUserLeftRoom();
        }
    }

    private void onUserLeftRoom() {
        if (adminStatusFetchingRunnable != null) {
            handler.removeCallbacks(adminStatusFetchingRunnable);
            adminStatusFetchingRunnable = null;
        }
        roomID = null;
        password = null;
        user = null;
        token.setValue(null);
        userIsAdmin.setValue(false);
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public void setUser(@NonNull User user) {
        this.user = user;
    }

    public void setToken(@NonNull String token) {
        this.token.setValue(token);
    }

    public void setUserIsAdmin(boolean userIsAdmin) {
        this.userIsAdmin.setValue(userIsAdmin);
    }

    public void startFetchingAdminStatus() {
        fetchAdminStatus();

        if (adminStatusFetchingRunnable == null) {
            adminStatusFetchingRunnable = new Runnable() {

                @Override
                public void run() {
                    fetchAdminStatus();
                    handler.postDelayed(this, Constants.ADMIN_STATUS_FETCHING_INTERVAL);
                }
            };
            adminStatusFetchingRunnable.run();
        }
    }

    private void fetchAdminStatus() {
        String token = this.token.getValue();

        if (roomID == null || token == null) {
            return;
        }
        getAdminStatus(roomID, token, new JamCallback<AdminStatusResponse>() {

            @Override
            public void onSuccess(@NonNull AdminStatusResponse response) {
                Boolean isAdmin = response.isAdmin();
                String token = response.getToken();

                if (isAdmin != null) {
                    userIsAdmin.setValue(isAdmin);
                    if (isAdmin && token != null) {
                        RoomRepository.this.token.setValue(token);
                    }
                }
            }

            @Override
            public void onError(@NonNull Error error) {
                Timber.d("onError()");
            }
        });
    }

    @NonNull
    public LiveData<Boolean> getUserInRoom() {
        return userInRoom;
    }

    @Nullable
    public Integer getRoomID() {
        return roomID;
    }

    @Nullable
    public String getPassword() {
        return password;
    }

    @Nullable
    public User getUser() {
        return user;
    }

    @NonNull
    public LiveData<String> getToken() {
        return token;
    }

    @NonNull
    public LiveData<Boolean> getUserIsAdmin() {
        return userIsAdmin;
    }
}
