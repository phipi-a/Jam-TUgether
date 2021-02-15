package de.pcps.jamtugether.ui.base.views.dialogs;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import de.pcps.jamtugether.R;

public class InfoDialog {

    @NonNull
    private final AlertDialog alertDialog;

    public InfoDialog(@NonNull Context context, @NonNull String title, @NonNull String message) {
        alertDialog = new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                }).create();

    }

    public InfoDialog(@NonNull Context context, String title, @NonNull String message, @NonNull String positiveButtonText, @NonNull OnButtonClickListener onButtonClickListener) {
        alertDialog = new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, ((dialog, which) -> {
                    onButtonClickListener.onPositiveButtonClicked();
                }))
                .setNeutralButton(R.string.ok, (dialog, which) -> {
                }).create();

    }

    public InfoDialog(@NonNull Context context, @StringRes int title, @StringRes int message, @StringRes int positiveButtonText, @NonNull OnButtonClickListener onButtonClickListener) {
        this(context, context.getString(title), context.getString(message), context.getString(positiveButtonText), onButtonClickListener);
    }

    public InfoDialog(@NonNull Context context, @StringRes int title, @StringRes int message) {
        this(context, context.getString(title), context.getString(message));
    }

    public void show() {
        alertDialog.show();
    }

    public interface OnButtonClickListener {
        void onPositiveButtonClicked();
    }
}
