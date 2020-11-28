package de.pcps.jamtugether.api.errors;

import de.pcps.jamtugether.R;

public class UnauthorizedAccessError extends Error {

    public UnauthorizedAccessError() {
        super(R.string.unauthorized_access_error_title, R.string.unauthorized_access_error_message);
    }
}
