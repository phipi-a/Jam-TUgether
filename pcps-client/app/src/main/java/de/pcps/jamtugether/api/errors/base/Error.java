package de.pcps.jamtugether.api.errors.base;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import de.pcps.jamtugether.api.errors.ForbiddenAccessError;
import de.pcps.jamtugether.api.errors.GenericError;
import de.pcps.jamtugether.api.errors.InternalServerError;
import de.pcps.jamtugether.api.errors.NoInternetConnectionError;
import de.pcps.jamtugether.api.errors.OldAdminError;
import de.pcps.jamtugether.api.errors.PageNotFoundError;
import de.pcps.jamtugether.api.errors.RoomDeletedError;
import de.pcps.jamtugether.api.errors.RoomDoesNotExistError;
import de.pcps.jamtugether.api.errors.RoomNumberLimitReachedError;
import de.pcps.jamtugether.api.exceptions.NoInternetConnectionException;
import de.pcps.jamtugether.api.errors.PasswordTooLargeError;
import de.pcps.jamtugether.api.errors.UnauthorizedAccessError;

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
        switch (statusCode) {
            case 401:
                return new UnauthorizedAccessError();
            case 403:
                return new ForbiddenAccessError();
            case 404:
                return new PageNotFoundError();
            case 408:
                return new OldAdminError();
            case 413:
                return new PasswordTooLargeError();
            case 500:
                return new InternalServerError();
            case 503:
                return new RoomNumberLimitReachedError();
            case 410:
                return new RoomDeletedError();
            default:
                return new GenericError();
        }
    }

    @NonNull
    public static Error from(Throwable throwable) {
        if (throwable instanceof NoInternetConnectionException) {
            return new NoInternetConnectionError();
        }
        return new GenericError();
    }
}
