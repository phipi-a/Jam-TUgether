package de.pcps.jamtugether.ui.base.views.dialogs;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import de.pcps.jamtugether.R;

public class ConfirmationDialog {

    @NonNull
    private final AlertDialog alertDialog;

    public ConfirmationDialog(@NonNull Context context, @NonNull String title, @NonNull String message, @NonNull OnButtonClickListener onButtonClickListener) {
        alertDialog = new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, (dialog, which) -> onButtonClickListener.onPositiveButtonClicked())
                .setNegativeButton(R.string.no, ((dialog, which) -> {
                    onButtonClickListener.onNegativeButtonClicked();
                })).create();
    }

    public ConfirmationDialog(@NonNull Context context, @StringRes int title, @StringRes int message, @NonNull OnButtonClickListener onButtonClickListener) {
        this(context, context.getString(title), context.getString(message), onButtonClickListener);
    }

    public void show() {
        alertDialog.show();
    }

    public interface OnButtonClickListener {
        void onPositiveButtonClicked();

        void onNegativeButtonClicked();
    }
}
