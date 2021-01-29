package de.pcps.jamtugether.ui.onboarding.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.ui.base.BaseFragment;

public class OnBoardingDrumsFragment extends BaseFragment {

    @NonNull
    public static OnBoardingDrumsFragment newInstance() {
        return new OnBoardingDrumsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.on_boarding_drums_fragment,container, false);
    }
}
