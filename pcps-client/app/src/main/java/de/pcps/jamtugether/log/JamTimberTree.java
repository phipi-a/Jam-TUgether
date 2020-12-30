package de.pcps.jamtugether.log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class JamTimberTree extends Timber.DebugTree {

    private static final String TAG_PREFIX = "#-#";

    @Override
    protected void log(int priority, @NonNull String tag, @NotNull String message, Throwable t) {
        super.log(priority, TAG_PREFIX.concat(" ").concat(tag), message, t);
    }
}
