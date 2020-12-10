package de.pcps.jamtugether.content.soundtrack.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import de.pcps.jamtugether.models.music.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.databinding.ViewSoundtrackBinding;

public class RegularSoundtrackListAdapter extends SoundtrackListAdapter<RegularSoundtrackListAdapter.ViewHolder> {

    public RegularSoundtrackListAdapter(@NonNull SingleSoundtrack.OnChangeListener onChangeListener, @NonNull LifecycleOwner lifecycleOwner) {
        super(onChangeListener, lifecycleOwner);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), onChangeListener, lifecycleOwner);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ViewSoundtrackBinding binding;

        ViewHolder(@NonNull ViewSoundtrackBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(@NonNull SingleSoundtrack singleSoundtrack, @NonNull SingleSoundtrack.OnChangeListener onChangeListener, @NonNull LifecycleOwner lifecycleOwner) {
            binding.soundtrackControlsLayout.setSoundtrack(singleSoundtrack);
            binding.soundtrackControlsLayout.setOnChangeListener(onChangeListener);
            binding.soundtrackControlsLayout.setLifecycleOwner(lifecycleOwner);
            binding.soundtrackControlsLayout.executePendingBindings();

            binding.soundtrackView.draw(singleSoundtrack);
        }

        @NonNull
        static ViewHolder from(@NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new ViewHolder(ViewSoundtrackBinding.inflate(inflater, parent, false));
        }
    }
}