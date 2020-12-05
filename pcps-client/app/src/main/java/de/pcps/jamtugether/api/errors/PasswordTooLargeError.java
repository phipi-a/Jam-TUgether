package de.pcps.jamtugether.api.errors;

import de.pcps.jamtugether.R;

public class PasswordTooLargeError extends Error {

    public PasswordTooLargeError() {
        super(R.string.password_too_large_error_title, R.string.password_too_large_error_message);
    }
}
