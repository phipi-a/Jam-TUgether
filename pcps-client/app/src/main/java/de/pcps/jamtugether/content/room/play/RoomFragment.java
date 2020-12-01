package de.pcps.jamtugether.content.room.play;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.views.JamTabView;

public abstract class RoomFragment extends TabLayoutFragment {

    protected int roomID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            RegularRoomFragmentArgs args = RegularRoomFragmentArgs.fromBundle(getArguments());
            this.roomID = args.getRoomID();
        }
    }

    @NonNull
    @Override
    protected TabLayoutAdapter createTabLayoutAdapter() {
        return new TabLayoutAdapter(this) {

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return RoomFragment.this.createFragment(position);
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

    @NonNull
    protected abstract Fragment createFragment(int position);
}
