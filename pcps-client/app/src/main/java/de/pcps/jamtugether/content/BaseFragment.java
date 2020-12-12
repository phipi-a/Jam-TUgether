package de.pcps.jamtugether.content;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public abstract class BaseFragment extends Fragment {

    private FragmentActivity fragmentActivity;

    @NonNull
    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            BaseFragment.this.handleOnBackPressed();
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
