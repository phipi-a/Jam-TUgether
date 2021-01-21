package de.pcps.jamtugether.ui.base;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import de.pcps.jamtugether.ui.base.views.JamTabView;

public abstract class TabLayoutAdapter extends FragmentStateAdapter {

    public TabLayoutAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    public abstract JamTabView getTabView(int position);

    @StringRes
    public abstract int getTabTitle(int position);

    public abstract int getInitialTabPosition();
}
