package de.pcps.jamtugether.content.room.overview;

import android.os.Bundle;

import androidx.annotation.NonNull;
import de.pcps.jamtugether.content.soundtrack.adapters.RegularSoundtrackListAdapter;
import de.pcps.jamtugether.content.soundtrack.adapters.SoundtrackListAdapter;

public class RegularRoomOverviewFragment extends RoomOverviewFragment {

    @NonNull
    public static RegularRoomOverviewFragment newInstance(int roomID) {
        RegularRoomOverviewFragment fragment = new RegularRoomOverviewFragment();
        Bundle args = new Bundle();
        args.putInt(ROOM_ID_KEY, roomID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected SoundtrackListAdapter createSoundtrackListAdapter() {
        return new RegularSoundtrackListAdapter(viewModel, getViewLifecycleOwner());
    }

}
