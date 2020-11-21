package de.pcps.jamtugether.base.tablayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.databinding.FragmentTabLayoutBinding;

public abstract class TabLayoutFragment extends Fragment {

    protected JamTabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentTabLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tab_layout, container, false);
        tabLayout = binding.tabLayout;
        tabLayout.setup(binding.viewPager, getTabLayoutAdapter());

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(tabLayout != null) {
            tabLayout.unregisterOnPageChangeCallback();
        }
    }

    @NonNull
    protected abstract TabLayoutAdapter getTabLayoutAdapter();
}
