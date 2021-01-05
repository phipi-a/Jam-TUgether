package de.pcps.jamtugether.api.errors;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.api.errors.base.Error;

public class UnauthorizedAccessError extends Error {

    // todo change message depending on case
    public UnauthorizedAccessError() {
        super(R.string.unauthorized_access_error_title, R.string.unauthorized_access_error_message);
    }
}
