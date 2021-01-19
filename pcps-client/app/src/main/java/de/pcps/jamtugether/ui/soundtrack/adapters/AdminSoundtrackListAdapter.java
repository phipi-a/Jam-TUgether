package de.pcps.jamtugether.ui.soundtrack.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import de.pcps.jamtugether.ui.soundtrack.adapters.base.SoundtrackListAdapter;
import de.pcps.jamtugether.ui.soundtrack.adapters.holders.DeleteViewHolder;
import timber.log.Timber;

public class AdminSoundtrackListAdapter extends SoundtrackListAdapter<DeleteViewHolder> {

    public AdminSoundtrackListAdapter(@NonNull Soundtrack.OnChangeCallback onChangeCallback, @NonNull SingleSoundtrack.OnDeleteListener onDeleteListener, @NonNull LifecycleOwner lifecycleOwner) {
        super(onChangeCallback, onDeleteListener, lifecycleOwner);
    }

    @NonNull
    @Override
    public DeleteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return DeleteViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull DeleteViewHolder holder, int position) {
        Timber.d("onBindViewHolder() | %s", getItem(position));
        holder.bind(getItem(position), onChangeCallback, onDeleteListener, lifecycleOwner);
    }
}