package de.pcps.jamtugether.ui.room;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.ui.base.TabLayoutAdapter;
import de.pcps.jamtugether.ui.base.views.tab.JamTabView;
import de.pcps.jamtugether.ui.room.music.MusicianViewFragment;
import de.pcps.jamtugether.ui.room.overview.SoundtrackOverviewFragment;

public class RoomAdapter extends TabLayoutAdapter {

    @NonNull
    private final RoomViewModel roomViewModel;

    @NonNull
    private final TabLayout tabLayout;

    public RoomAdapter(@NonNull Fragment fragment, @NonNull RoomViewModel roomViewModel, @NonNull TabLayout tabLayout) {
        super(fragment);
        this.roomViewModel = roomViewModel;
        this.tabLayout = tabLayout;
    }

    @NonNull
    @Override
    public JamTabView getTabView(int position) {
        return JamTabView.from(tabLayout);
    }

    @StringRes
    @Override
    public int getTabTitle(int position) {
        return position == 0 ? R.string.soundtrack_over_view : R.string.musician_view;
    }

    @Override
    public int getInitialTabPosition() {
        return roomViewModel.getInitialTabPosition();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return position == 0 ? SoundtrackOverviewFragment.newInstance() : MusicianViewFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
