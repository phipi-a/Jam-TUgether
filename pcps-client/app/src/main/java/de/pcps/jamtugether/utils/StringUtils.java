package de.pcps.jamtugether.utils;

import androidx.annotation.Nullable;

public class StringUtils {

    public static boolean isEmpty(@Nullable String string) {
        if (string == null) {
            return true;
        }
        return string.replace(" ", "").isEmpty();
    }
}
