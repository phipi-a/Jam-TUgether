package de.pcps.jamtugether.network;

import java.io.IOException;

public class NoNetworkConnectionException extends IOException {

    public NoNetworkConnectionException() {
        super("no internet connection");
    }
}
