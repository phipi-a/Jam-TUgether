package de.pcps.jamtugether.api.responses;

import androidx.annotation.NonNull;

public class JoinRoomResponse {

    @NonNull
    private final String token;

    public JoinRoomResponse(@NonNull String token) {
        this.token = token;
    }

    @NonNull
    public String getToken() {
        return token;
    }
}
