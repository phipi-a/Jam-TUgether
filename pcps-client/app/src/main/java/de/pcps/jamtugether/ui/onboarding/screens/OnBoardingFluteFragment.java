package de.pcps.jamtugether.ui.onboarding.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.ui.base.BaseFragment;

public class OnBoardingFluteFragment extends BaseFragment {

    @NonNull
    public static OnBoardingFluteFragment newInstance() {
        return new OnBoardingFluteFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.onboarding_flute_fragment,container, false);
    }
}
