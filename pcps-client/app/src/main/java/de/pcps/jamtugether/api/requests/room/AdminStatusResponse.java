package de.pcps.jamtugether.api.requests.room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.moshi.Json;

import de.pcps.jamtugether.api.requests.SimpleResponse;

public class AdminStatusResponse extends SimpleResponse {

    @Json(name = "flag")
    @Nullable
    private final Boolean isAdmin;

    @Nullable
    private final String token;

    public AdminStatusResponse(@NonNull String description, @Nullable Boolean isAdmin, @Nullable String token) {
        super(description);
        this.isAdmin = isAdmin;
        this.token = token;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @Nullable
    public Boolean isAdmin() { return isAdmin; }

    @Nullable
    public String getToken() { return token; }
}
