package de.pcps.jamtugether.ui.base;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

public abstract class BaseDialogFragment extends DialogFragment {

    private FragmentActivity fragmentActivity;

    protected AppCompatActivity activity;

    protected Context context;

    @NonNull
    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            BaseDialogFragment.this.handleOnBackPressed();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentActivity.getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.fragmentActivity = (FragmentActivity) context;
        this.activity = (AppCompatActivity) context;
        this.context = context;
    }

    // fragments can override this method for custom logic
    protected void handleOnBackPressed() {
        navigateBack();
    }

    protected void navigateBack() {
        onBackPressedCallback.setEnabled(false);
        fragmentActivity.getOnBackPressedDispatcher().onBackPressed();
    }
}
