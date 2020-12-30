package de.pcps.jamtugether.log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class JamTimberTree extends Timber.DebugTree {

    @NonNull
    private final String prefix;

    public JamTimberTree(@NonNull String prefix) {
        this.prefix = prefix;
    }

    @Override
    protected void log(int priority, @NonNull String tag, @NotNull String message, Throwable t) {
        super.log(priority, prefix.concat(" ").concat(tag), message, t);
    }
}
