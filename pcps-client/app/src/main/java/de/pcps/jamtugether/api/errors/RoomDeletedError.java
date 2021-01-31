package de.pcps.jamtugether.api.errors;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.api.errors.base.Error;

public class RoomDeletedError extends Error {

    public RoomDeletedError() {
        super(R.string.room_deleted_error_title, R.string.room_deleted_error_message);
    }
}
