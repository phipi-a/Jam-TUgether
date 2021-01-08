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
import retrofit2.Call;
import timber.log.Timber;

@Singleton
public class RoomRepository {

    @NonNull
    private final RoomService roomService;

    private int currentRoomID;

    @NonNull
    private final MutableLiveData<String> currentToken = new MutableLiveData<>();

    @NonNull
    private final MutableLiveData<Boolean> currentUserIsAdmin = new MutableLiveData<>();

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

    public void deleteRoom(int roomID, @NonNull String password, @NonNull String token, @NonNull JamCallback<DeleteRoomResponse> callback) {
        DeleteRoomBody body = new DeleteRoomBody(roomID, password);
        Call<DeleteRoomResponse> call = roomService.deleteRoom(String.format(Constants.BEARER_TOKEN_FORMAT, token), body);
        call.enqueue(callback);
    }

    public void removeAdmin(int roomID, @NonNull String token, @NonNull JamCallback<RemoveAdminResponse> callback) {
        Call<RemoveAdminResponse> call = roomService.removeAdmin(String.format(Constants.BEARER_TOKEN_FORMAT, token), roomID);
        call.enqueue(callback);
    }

    private void getAdminStatus(int roomID, @NonNull String token, @NonNull JamCallback<AdminStatusResponse> callback) {
        Call<AdminStatusResponse> call = roomService.getAdminStatus(String.format(Constants.BEARER_TOKEN_FORMAT, token), roomID);
        call.enqueue(callback);
    }

    // called by RoomViewModel every time a user enters a new room
    public void updateInfo(@NonNull String currentToken, boolean userIsAdmin) {
        this.currentToken.setValue(currentToken);
        this.currentUserIsAdmin.setValue(userIsAdmin);
    }

    public void fetchAdminStatus(int currentRoomID, @NonNull String currentToken) {
        this.currentRoomID = currentRoomID;
        this.currentToken.setValue(currentToken);

        fetchAdminStatus();

        if (adminStatusFetchingRunnable == null) {
            startFetchingAdminStatus();
        }
    }

    private void startFetchingAdminStatus() {
        adminStatusFetchingRunnable = new Runnable() {

            @Override
            public void run() {
                if (currentToken == null || currentRoomID == -1) {
                    return;
                }
                fetchAdminStatus();
                handler.postDelayed(this, Constants.ADMIN_STATUS_FETCHING_INTERVAL);
            }
        };
        adminStatusFetchingRunnable.run();
    }

    private void fetchAdminStatus() {
        String token = currentToken.getValue();
        if (token == null) {
            return;
        }
        getAdminStatus(currentRoomID, token, new JamCallback<AdminStatusResponse>() {

            @Override
            public void onSuccess(@NonNull AdminStatusResponse response) {
                Boolean flag = response.getFlag();
                String token = response.getToken();

                if (flag != null) {
                    currentUserIsAdmin.setValue(flag);
                    if (flag) {
                        currentToken.setValue(token);
                    }
                }
            }

            @Override
            public void onError(@NonNull Error error) {
                Timber.d("onError()");
            }
        });
    }

    public void onUserLeftRoom() {
        if (adminStatusFetchingRunnable != null) {
            handler.removeCallbacks(adminStatusFetchingRunnable);
            adminStatusFetchingRunnable = null;
        }
        currentToken.setValue(null);
        currentRoomID = -1;
    }

    @NonNull
    public LiveData<String> getCurrentToken() {
        return currentToken;
    }

    @NonNull
    public LiveData<Boolean> getUserIsAdmin() {
        return currentUserIsAdmin;
    }
}
