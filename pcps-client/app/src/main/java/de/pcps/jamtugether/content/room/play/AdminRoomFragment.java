package de.pcps.jamtugether.content.room.play;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import de.pcps.jamtugether.content.room.play.music.MusicianViewFragment;
import de.pcps.jamtugether.content.room.play.overview.AdminRoomOverviewFragment;


public class AdminRoomFragment extends RoomFragment {

    @NonNull
    @Override
    protected Fragment createFragment(int position) {
        return position == 0 ? AdminRoomOverviewFragment.newInstance(roomID) : MusicianViewFragment.newInstance(roomID);
    }
}
