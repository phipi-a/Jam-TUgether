package de.pcps.jamtugether.ui.menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import de.pcps.jamtugether.ui.base.views.JamTextInputLayout;

public class DataBindingUtils {

    @BindingAdapter("error")
    public static void setError(@NonNull JamTextInputLayout textInputLayout, @Nullable String error) {
        textInputLayout.setError(error);
    }
}
