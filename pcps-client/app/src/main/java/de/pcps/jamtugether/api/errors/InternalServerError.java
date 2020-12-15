package de.pcps.jamtugether.api.errors;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.api.errors.base.Error;

public class InternalServerError extends Error {

    public InternalServerError() {
        super(R.string.internal_server_error_title, R.string.internal_server_error_message);
    }
}
