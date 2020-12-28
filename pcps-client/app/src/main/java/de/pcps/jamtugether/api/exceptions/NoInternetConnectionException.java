package de.pcps.jamtugether.api.exceptions;

import java.io.IOException;

public class NoInternetConnectionException extends IOException {

    public NoInternetConnectionException() {
        super("no internet connection");
    }
}
