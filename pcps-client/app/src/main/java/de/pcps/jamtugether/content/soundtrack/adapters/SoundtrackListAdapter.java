package de.pcps.jamtugether.content.soundtrack.adapters;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import de.pcps.jamtugether.content.soundtrack.Soundtrack;

public abstract class SoundtrackListAdapter<T extends RecyclerView.ViewHolder> extends ListAdapter<Soundtrack, T> {

    @NonNull
    protected final Soundtrack.OnChangeListener onChangeListener;

    @NonNull
    protected final LiveData<Drawable> playPauseButtonDrawable;

    @NonNull
    protected final LiveData<Integer> stopButtonVisibility;

    @NonNull
    protected final LifecycleOwner lifecycleOwner;

    public SoundtrackListAdapter(@NonNull Soundtrack.OnChangeListener onChangeListener, @NonNull LiveData<Drawable> playPauseButtonDrawable, @NonNull LiveData<Integer> stopButtonVisibility, @NonNull LifecycleOwner lifecycleOwner) {
        super(Soundtrack.DIFF_UTIL_CALLBACK);
        this.onChangeListener = onChangeListener;
        this.playPauseButtonDrawable = playPauseButtonDrawable;
        this.stopButtonVisibility = stopButtonVisibility;
        this.lifecycleOwner = lifecycleOwner;
    }
}








