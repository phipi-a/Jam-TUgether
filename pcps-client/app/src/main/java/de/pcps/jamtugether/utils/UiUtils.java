package de.pcps.jamtugether.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import de.pcps.jamtugether.ui.base.views.JamSnackBar;
import de.pcps.jamtugether.ui.base.views.dialogs.ConfirmationDialog;
import de.pcps.jamtugether.ui.base.views.dialogs.InfoDialog;

public class UiUtils {

    public static void hideKeyboard(@NonNull Activity activity, @NonNull View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static int getPixels(@NonNull Context context, @DimenRes int dimension) {
        return context.getResources().getDimensionPixelSize(dimension);
    }

    public static void showInfoDialog(@NonNull Context context, @StringRes int title, @StringRes int message) {
        new InfoDialog(context, title, message).show();
    }

    public static void showInfoDialog(@NonNull Context context, @StringRes int title, @StringRes int message, @StringRes int positiveButtonText, @NonNull InfoDialog.OnButtonClickListener onButtonClickListener) {
        new InfoDialog(context, title, message, positiveButtonText, onButtonClickListener).show();
    }

    public static void showConfirmationDialog(@NonNull Context context, @StringRes int title, @StringRes int message, @NonNull ConfirmationDialog.OnButtonClickListener callback) {
        new ConfirmationDialog(context, title, message, callback).show();
    }

    public static void showSnackbar(@NonNull View view, @StringRes int message, int duration) {
        new JamSnackBar(view, message, duration).show();
    }
}
