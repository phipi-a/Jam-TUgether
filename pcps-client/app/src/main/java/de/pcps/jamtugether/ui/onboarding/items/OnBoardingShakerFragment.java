package de.pcps.jamtugether.ui.onboarding.items;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.ui.base.BaseFragment;

public class OnBoardingShakerFragment extends BaseFragment {

    @NonNull
    public static OnBoardingShakerFragment newInstance() {
        return new OnBoardingShakerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.on_boarding_shaker_fragment, container, false);
    }
}
