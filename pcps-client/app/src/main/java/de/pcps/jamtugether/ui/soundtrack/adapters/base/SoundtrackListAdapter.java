package de.pcps.jamtugether.ui.soundtrack.adapters.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;

public abstract class SoundtrackListAdapter<T extends RecyclerView.ViewHolder> extends ListAdapter<SingleSoundtrack, T> {

    @NonNull
    protected final Soundtrack.OnChangeCallback onChangeCallback;

    @NonNull
    protected final LifecycleOwner lifecycleOwner;

    public SoundtrackListAdapter(@NonNull Soundtrack.OnChangeCallback onChangeCallback, @NonNull LifecycleOwner lifecycleOwner) {
        super(SingleSoundtrack.DIFF_UTIL_CALLBACK);
        this.onChangeCallback = onChangeCallback;
        this.lifecycleOwner = lifecycleOwner;
    }
}








