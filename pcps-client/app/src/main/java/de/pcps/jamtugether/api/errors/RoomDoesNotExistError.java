package de.pcps.jamtugether.api.errors;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.api.errors.base.Error;

public class RoomDoesNotExistError extends Error {

    public RoomDoesNotExistError() {
        super(R.string.room_does_not_exist_error_title, R.string.room_does_not_exist_error_message);
    }
}
