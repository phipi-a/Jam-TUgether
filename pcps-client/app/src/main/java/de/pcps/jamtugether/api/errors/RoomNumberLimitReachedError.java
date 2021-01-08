package de.pcps.jamtugether.api.errors;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.api.errors.base.Error;

public class RoomNumberLimitReachedError extends Error {
    public RoomNumberLimitReachedError() {
        super(R.string.room_number_limit_reached_error_title, R.string.room_number_limit_reached_error_message);
    }
}
