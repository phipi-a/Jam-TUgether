package de.pcps.jamtugether.api.requests.room.beat;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.api.requests.SimpleResponse;

public class UpdateBeatResponse extends SimpleResponse {

    public UpdateBeatResponse(@NonNull String description) {
        super(description);
    }
}
