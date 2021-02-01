package de.pcps.jamtugether.ui.base.views;

import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import de.pcps.jamtugether.R;

public class JamSnackBar {

    @NonNull
    private final Snackbar snackbar;

    public JamSnackBar(@NonNull View view, @StringRes int message, int duration) {
        snackbar = Snackbar.make(view, message, duration);
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
        snackbar.setBackgroundTint(ContextCompat.getColor(view.getContext(), R.color.snackbarBackgroundColor));
        snackbar.setTextColor(ContextCompat.getColor(view.getContext(), R.color.snackbarTextColor));
        snackbar.setText(message);
        View snackbarView = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
        params.gravity = Gravity.TOP;
        snackbarView.setLayoutParams(params);
    }

    public void show() {
        snackbar.show();
    }
}
