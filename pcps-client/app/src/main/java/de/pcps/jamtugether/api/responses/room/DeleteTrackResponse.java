package de.pcps.jamtugether.api.responses.room;

import androidx.annotation.NonNull;

public class DeleteTrackResponse {

    @NonNull
    private final String description;

    public DeleteTrackResponse(@NonNull String description) {
        this.description = description;
    }

    @NonNull
    public String getDescription() {
        return description;
    }
}
