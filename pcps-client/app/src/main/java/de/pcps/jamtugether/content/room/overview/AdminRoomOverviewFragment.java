package de.pcps.jamtugether.content.room.overview;

import android.os.Bundle;
import android.os.NetworkOnMainThreadException;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.content.soundtrack.adapters.AdminSoundtrackListAdapter;
import de.pcps.jamtugether.content.soundtrack.adapters.SoundtrackListAdapter;

public class AdminRoomOverviewFragment extends RoomOverviewFragment {

    @NonNull
    public static AdminRoomOverviewFragment newInstance(int roomID, @NonNull String token) {
        AdminRoomOverviewFragment fragment = new AdminRoomOverviewFragment();
        Bundle args = new Bundle();
        args.putInt(ROOM_ID_KEY, roomID);
        args.putString(TOKEN_KEY, token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected SoundtrackListAdapter createSoundtrackListAdapter() {
        return new AdminSoundtrackListAdapter(viewModel, getViewLifecycleOwner());
    }
}
