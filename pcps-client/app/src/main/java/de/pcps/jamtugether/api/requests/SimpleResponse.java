package de.pcps.jamtugether.api.requests;

import androidx.annotation.NonNull;

public abstract class SimpleResponse {

    @NonNull
    protected final String description;

    public SimpleResponse(@NonNull String description) {
        this.description = description;
    }

    @NonNull
    public String getDescription() {
        return description;
    }
}
