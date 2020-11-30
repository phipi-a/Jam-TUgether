package de.pcps.jamtugether.content.soundtrack.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import de.pcps.jamtugether.content.soundtrack.Soundtrack;
import de.pcps.jamtugether.databinding.ViewSoundtrackBinding;

public class RegularSoundtrackListAdapter extends SoundtrackListAdapter<RegularSoundtrackListAdapter.ViewHolder> {

    public RegularSoundtrackListAdapter(@NonNull Soundtrack.OnChangeListener onChangeListener, @NonNull LiveData<Drawable> playPauseButtonDrawable, @NonNull LiveData<Integer> stopButtonVisibility, @NonNull LifecycleOwner lifecycleOwner) {
        super(onChangeListener, playPauseButtonDrawable, stopButtonVisibility, lifecycleOwner);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), onChangeListener, playPauseButtonDrawable, stopButtonVisibility, lifecycleOwner);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ViewSoundtrackBinding binding;

        ViewHolder(@NonNull ViewSoundtrackBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(@NonNull Soundtrack soundtrack, @NonNull Soundtrack.OnChangeListener onChangeListener, @NonNull LiveData<Drawable> playPauseButtonDrawable, @NonNull LiveData<Integer> stopButtonVisibility, @NonNull LifecycleOwner lifecycleOwner) {
            binding.soundtrackControlsLayout.setSoundtrack(soundtrack);
            binding.soundtrackControlsLayout.setOnChangeListener(onChangeListener);
            binding.soundtrackControlsLayout.setLifecycleOwner(lifecycleOwner);
            binding.soundtrackControlsLayout.setPlayPauseButtonDrawable(playPauseButtonDrawable);
            binding.soundtrackControlsLayout.setStopButtonVisibility(stopButtonVisibility);

            binding.soundtrackView.draw(soundtrack);
        }

        @NonNull
        static ViewHolder from(@NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new ViewHolder(ViewSoundtrackBinding.inflate(inflater, parent, false));
        }
    }
}