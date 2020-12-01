package de.pcps.jamtugether.content.room.play.overview;

import android.os.Bundle;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.content.soundtrack.adapters.AdminSoundtrackListAdapter;
import de.pcps.jamtugether.content.soundtrack.adapters.SoundtrackListAdapter;

public class AdminRoomOverviewFragment extends RoomOverviewFragment {

    @NonNull
    public static AdminRoomOverviewFragment newInstance(int roomID) {
        AdminRoomOverviewFragment fragment = new AdminRoomOverviewFragment();
        Bundle args = new Bundle();
        args.putInt(ROOM_ID_KEY, roomID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected SoundtrackListAdapter createSoundtrackListAdapter() {
        return new AdminSoundtrackListAdapter(viewModel, getViewLifecycleOwner());
    }
}
