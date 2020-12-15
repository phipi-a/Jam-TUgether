package de.pcps.jamtugether.api.errors;

import java.io.IOException;

public class NoInternetConnectionException extends IOException {

    public NoInternetConnectionException() {
        super("no internet connection");
    }
}
