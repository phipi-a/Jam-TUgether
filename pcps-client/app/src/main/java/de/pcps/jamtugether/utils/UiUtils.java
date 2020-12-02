package de.pcps.jamtugether.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import de.pcps.jamtugether.R;

public class UiUtils {

    public static void hideKeyboard(@NonNull Activity activity, @NonNull View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @NonNull
    public static AlertDialog createInfoDialog(@NonNull Context context, @NonNull String title, @NonNull String message) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                })
                .create();
    }

    @NonNull
    public static AlertDialog createInfoDialog(@NonNull Context context, @StringRes int title, @StringRes int message) {
        return createInfoDialog(context, context.getString(title), context.getString(message));
    }

    public static AlertDialog createConfirmationDialog(@NonNull Context context, @NonNull String title, @NonNull String message, OnDialogPositiveButtonClickedCallback callback) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, (dialog, which) -> callback.onPositiveButtonClicked())
                .setNegativeButton(R.string.no, ((dialog, which) -> {
                }))
                .create();
    }

    @NonNull
    public static AlertDialog createConfirmationDialog(@NonNull Context context, @StringRes int title, @StringRes int message, OnDialogPositiveButtonClickedCallback callback) {
        return createConfirmationDialog(context, context.getString(title), context.getString(message), callback);
    }

    public interface OnDialogPositiveButtonClickedCallback {
        void onPositiveButtonClicked();
    }
}
