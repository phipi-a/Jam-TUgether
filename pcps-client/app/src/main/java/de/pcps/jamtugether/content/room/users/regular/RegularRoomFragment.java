package de.pcps.jamtugether.content.room.users.regular;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.base.views.JamTabView;
import de.pcps.jamtugether.content.room.users.TabLayoutAdapter;
import de.pcps.jamtugether.content.room.users.TabLayoutFragment;

// parent fragment with a tab bar and two child fragments (one for each tab)
public class RegularRoomFragment extends TabLayoutFragment {

    @NonNull
    @Override
    protected TabLayoutAdapter getTabLayoutAdapter() {
        return new TabLayoutAdapter(this) {

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return position == 0 ? RegularRoomOverviewFragment.newInstance() : MusicianViewFragment.newInstance();
            }

            @Override
            public int getItemCount() {
                return 2;
            }

            @NonNull
            @Override
            public JamTabView getTabView(int position) {
                return JamTabView.from(tabLayout);
            }

            @StringRes
            @Override
            public int getTabTitle(int position) {
                return position == 0 ? R.string.room_over_view : R.string.musician_view;
            }
        };
    }
}
