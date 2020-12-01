package de.pcps.jamtugether.content.room;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import de.pcps.jamtugether.content.room.music.MusicianViewFragment;
import de.pcps.jamtugether.content.room.overview.RegularRoomOverviewFragment;

public class RegularRoomFragment extends RoomFragment {

    @NonNull
    @Override
    protected Fragment createFragment(int position) {
        return position == 0 ? RegularRoomOverviewFragment.newInstance(roomID) : MusicianViewFragment.newInstance(roomID);
    }
}
