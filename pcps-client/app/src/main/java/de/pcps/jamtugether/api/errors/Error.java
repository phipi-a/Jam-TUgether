package de.pcps.jamtugether.api.errors;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import timber.log.Timber;

public abstract class Error {

    @StringRes
    private final int title;

    @StringRes
    private final int message;

    public Error(@StringRes int title, @StringRes int message) {
        this.title = title;
        this.message = message;
    }

    @StringRes
    public int getTitle() {
        return title;
    }

    @StringRes
    public int getMessage() {
        return message;
    }

    @NonNull
    public static Error from(int statusCode) {
        // todo maybe add more status codes
        Timber.d("statusCode: %d", statusCode);
        switch (statusCode) {
            case 401:
                return new UnauthorizedAccessError();
            case 403:
                return new ForbiddenAccessError();
            case 500:
                return new InternalServerError();
            default:
                return new GenericError();
        }
    }

    @NonNull
    public static Error from(Throwable throwable) {
        Timber.d("error %s", throwable.getMessage());
        return new GenericError();
    }
}
