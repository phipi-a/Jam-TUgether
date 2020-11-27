package de.pcps.jamtugether.api;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import de.pcps.jamtugether.R;

public class Error {

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
    public static Error from(int responseCode) {
        // todo create error from response code
        return new Error(R.string.error, R.string.error);
    }

    @NonNull
    public static Error from(Throwable throwable) {
        // todo create error from throwable
        return new Error(R.string.error, R.string.error);
    }
}
