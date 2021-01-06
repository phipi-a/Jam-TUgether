package de.pcps.jamtugether.api.responses.base;

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
