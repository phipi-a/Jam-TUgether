package de.pcps.jamtugether.api.errors;

import de.pcps.jamtugether.R;

public class NoInternetConnectionError extends Error {

    public NoInternetConnectionError() {
        super(R.string.no_internet_connection_title, R.string.no_internet_connection_message);
    }
}
