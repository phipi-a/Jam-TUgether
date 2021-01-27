package de.pcps.jamtugether.ui.soundtrack.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import de.pcps.jamtugether.ui.soundtrack.adapters.base.SoundtrackListAdapter;
import de.pcps.jamtugether.ui.soundtrack.adapters.holders.base.BaseViewHolder;
import de.pcps.jamtugether.ui.soundtrack.adapters.holders.DeleteViewHolder;
import de.pcps.jamtugether.ui.soundtrack.adapters.holders.RegularViewHolder;

public class RegularSoundtrackListAdapter extends SoundtrackListAdapter<BaseViewHolder> {

    public static final int REGULAR_VIEW = 0;
    public static final int DELETE_VIEW = 1;

    private final int userID;

    public RegularSoundtrackListAdapter(int userID, @NonNull Soundtrack.OnChangeCallback onChangeCallback, @NonNull SingleSoundtrack.OnDeleteListener onDeleteListener, @NonNull LifecycleOwner lifecycleOwner) {
        super(onChangeCallback, onDeleteListener, lifecycleOwner);
        this.userID = userID;
    }

    @Override
    public int getItemViewType(int position) {
        SingleSoundtrack soundtrack = getItem(position);
        if (soundtrack.getUserID() == userID) {
            return DELETE_VIEW;
        }
        return REGULAR_VIEW;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == DELETE_VIEW) {
            return DeleteViewHolder.from(parent);
        }
        return RegularViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.bind(getItem(position), onChangeCallback, onDeleteListener, lifecycleOwner);
    }
}