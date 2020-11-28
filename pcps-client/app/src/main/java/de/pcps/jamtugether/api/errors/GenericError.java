package de.pcps.jamtugether.api.errors;

import de.pcps.jamtugether.R;

public class GenericError extends Error {

    public GenericError() {
        super(R.string.generic_error_title, R.string.generic_error_message);
    }
}
