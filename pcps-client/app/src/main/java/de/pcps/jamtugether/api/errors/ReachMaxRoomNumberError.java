package de.pcps.jamtugether.api.errors;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.api.errors.base.Error;

public class ReachMaxRoomNumberError extends Error {
    public ReachMaxRoomNumberError() {
        super(R.string.reach_max_room_number_error_title, R.string.reach_max_room_number_error_message);
    }
}
