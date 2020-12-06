package de.pcps.jamtugether.content.room;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.views.JamTabView;

public abstract class RoomFragment extends TabLayoutFragment {

    protected int roomID;
    
    protected String token;

    @NonNull
    @Override
    protected TabLayoutAdapter createTabLayoutAdapter() {
        return new TabLayoutAdapter(this) {

            @NonNull
            @Override
            public JamTabView getTabView(int position) {
                return JamTabView.from(tabLayout);
            }

            @Override
            public int getTabTitle(int position) {
                return position == 0 ? R.string.room_over_view : R.string.musician_view;
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return RoomFragment.this.createFragment(position);
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        };
    }

    @NonNull
    protected abstract Fragment createFragment(int position);
}
