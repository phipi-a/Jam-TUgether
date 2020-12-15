package de.pcps.jamtugether.ui.soundtrack.adapters;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import de.pcps.jamtugether.model.music.soundtrack.SingleSoundtrack;

public abstract class SoundtrackListAdapter<T extends RecyclerView.ViewHolder> extends ListAdapter<SingleSoundtrack, T> {

    @NonNull
    protected final SingleSoundtrack.OnChangeListener onChangeListener;

    @NonNull
    protected final LifecycleOwner lifecycleOwner;

    public SoundtrackListAdapter(@NonNull SingleSoundtrack.OnChangeListener onChangeListener, @NonNull LifecycleOwner lifecycleOwner) {
        super(SingleSoundtrack.DIFF_UTIL_CALLBACK);
        this.onChangeListener = onChangeListener;
        this.lifecycleOwner = lifecycleOwner;
    }
}








