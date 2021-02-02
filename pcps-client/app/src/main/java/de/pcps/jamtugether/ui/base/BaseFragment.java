package de.pcps.jamtugether.ui.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public abstract class BaseFragment extends Fragment {

    private FragmentActivity fragmentActivity;

    protected AppCompatActivity activity;

    protected Context context;

    @NonNull
    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            BaseFragment.this.onBackPressed();
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

    protected void lockOrientation() {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
    }

    protected void unlockOrientation() {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    // fragments can override this method for custom logic
    protected void onBackPressed() {
        navigateBack();
    }

    protected void navigateBack() {
        onBackPressedCallback.setEnabled(false);
        fragmentActivity.getOnBackPressedDispatcher().onBackPressed();
    }
}
