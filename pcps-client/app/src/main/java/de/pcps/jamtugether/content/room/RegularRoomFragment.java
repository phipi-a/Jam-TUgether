package de.pcps.jamtugether.content.room;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import de.pcps.jamtugether.content.room.music.MusicianViewFragment;
import de.pcps.jamtugether.content.room.overview.RegularRoomOverviewFragment;

public class RegularRoomFragment extends RoomFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            RegularRoomFragmentArgs args = RegularRoomFragmentArgs.fromBundle(getArguments());
            this.roomID = args.getRoomID();
            this.token = args.getToken();
        }
    }

    @NonNull
    @Override
    protected Fragment createFragment(int position) {
        return position == 0 ? RegularRoomOverviewFragment.newInstance(roomID, token) : MusicianViewFragment.newInstance(roomID, token);
    }
}
