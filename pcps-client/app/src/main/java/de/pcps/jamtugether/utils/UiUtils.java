package de.pcps.jamtugether.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import de.pcps.jamtugether.R;

public class UiUtils {

    public static void hideKeyboard(@NonNull Activity activity, @NonNull View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static int getPixels(@NonNull Context context, @DimenRes int dimension) {
        return context.getResources().getDimensionPixelSize(dimension);
    }

    public static void showInfoDialog(@NonNull Context context, @NonNull String title, @NonNull String message) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                })
                .show();
    }

    public static void showInfoDialog(@NonNull Context context, @StringRes int title, @StringRes int message) {
        showInfoDialog(context, context.getString(title), context.getString(message));
    }

    public static void showConfirmationDialog(@NonNull Context context, @NonNull String title, @NonNull String message, @NonNull OnDialogPositiveButtonClickedCallback callback) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, (dialog, which) -> callback.onPositiveButtonClicked())
                .setNegativeButton(R.string.no, ((dialog, which) -> {
                }))
                .show();
    }

    public static void showConfirmationDialog(@NonNull Context context, @StringRes int title, @StringRes int message, @NonNull OnDialogPositiveButtonClickedCallback callback) {
        showConfirmationDialog(context, context.getString(title), context.getString(message), callback);
    }

    public static void showSnackbar(@NonNull View view, @StringRes int message, int duration) {
        Snackbar snackbar = Snackbar.make(view, message, duration);
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
        snackbar.setBackgroundTint(ContextCompat.getColor(view.getContext(), R.color.snackbarBackgroundColor));
        snackbar.setTextColor(ContextCompat.getColor(view.getContext(), R.color.snackbarTextColor));
        snackbar.setText(message);

        View snackbarView = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
        params.gravity = Gravity.TOP;
        snackbarView.setLayoutParams(params);

        snackbar.show();
    }

    public interface OnDialogPositiveButtonClickedCallback {
        void onPositiveButtonClicked();
    }
}
