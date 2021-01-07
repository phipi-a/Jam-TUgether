package de.pcps.jamtugether.api.responses.room;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.api.responses.base.SimpleResponse;

public class AdminStatusResponse {
    @NonNull
    private final String description;
    @NonNull
    private final Boolean flag;

    private final String token;

    public AdminStatusResponse(@NonNull String description, @NonNull Boolean flag, String token) {

        this.description = description;
        this.flag = flag;
        this.token = token;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @NonNull
    public Boolean getFlag() { return flag; }

    @NonNull
    public String getToken() { return token; }
}
