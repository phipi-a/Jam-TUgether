package de.pcps.jamtugether.content.room.users.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.base.views.JamTabView;
import de.pcps.jamtugether.content.room.users.TabLayoutAdapter;
import de.pcps.jamtugether.content.room.users.TabLayoutFragment;
import de.pcps.jamtugether.content.room.users.MusicianViewFragment;

// parent fragment with a tab bar and two child fragments (one for each tab)
public class AdminRoomFragment extends TabLayoutFragment {

    private int roomID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            AdminRoomFragmentArgs args = AdminRoomFragmentArgs.fromBundle(getArguments());
            this.roomID = args.getRoomID();
        }
    }

    @NonNull
    @Override
    protected TabLayoutAdapter getTabLayoutAdapter() {
        return new TabLayoutAdapter(this) {

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return position == 0 ? AdminRoomOverviewFragment.newInstance(roomID) : MusicianViewFragment.newInstance(roomID);
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
