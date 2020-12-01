package de.pcps.jamtugether.content.room.play;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import de.pcps.jamtugether.views.JamTabLayout;
import de.pcps.jamtugether.databinding.FragmentTabLayoutBinding;

public abstract class TabLayoutFragment extends Fragment {

    protected JamTabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentTabLayoutBinding binding = FragmentTabLayoutBinding.inflate(inflater, container, false);
        tabLayout = binding.tabLayout;
        tabLayout.setup(binding.viewPager, createTabLayoutAdapter());

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
    protected abstract TabLayoutAdapter createTabLayoutAdapter();
}
