package de.pcps.jamtugether.api.errors;

import de.pcps.jamtugether.R;

public class ForbiddenAccessError extends Error {

    public ForbiddenAccessError() {
        super(R.string.forbidden_access_error_title, R.string.forbidden_access_error_message);
    }
}
