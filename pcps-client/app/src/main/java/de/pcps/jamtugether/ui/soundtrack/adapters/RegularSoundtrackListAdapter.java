package de.pcps.jamtugether.ui.soundtrack.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import de.pcps.jamtugether.model.music.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.databinding.ViewSoundtrackBinding;
import de.pcps.jamtugether.model.music.soundtrack.base.Soundtrack;
import de.pcps.jamtugether.ui.soundtrack.adapters.base.SoundtrackListAdapter;
import de.pcps.jamtugether.ui.soundtrack.views.SoundtrackContainer;

public class RegularSoundtrackListAdapter extends SoundtrackListAdapter<RegularSoundtrackListAdapter.ViewHolder> {

    public RegularSoundtrackListAdapter(@NonNull Soundtrack.OnChangeCallback onChangeCallback, @NonNull LifecycleOwner lifecycleOwner) {
        super(onChangeCallback, lifecycleOwner);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), onChangeCallback, lifecycleOwner);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ViewSoundtrackBinding binding;

        ViewHolder(@NonNull ViewSoundtrackBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(@NonNull SingleSoundtrack singleSoundtrack, @NonNull Soundtrack.OnChangeCallback onChangeCallback, @NonNull LifecycleOwner lifecycleOwner) {
            binding.soundtrackControlsLayout.setSoundtrack(singleSoundtrack);
            binding.soundtrackControlsLayout.setOnChangeListener(onChangeCallback);
            binding.soundtrackControlsLayout.setLifecycleOwner(lifecycleOwner);
            binding.soundtrackControlsLayout.executePendingBindings();

            ((SoundtrackContainer) binding.soundtrackContainer).onSingleSoundtrackChanged(singleSoundtrack, lifecycleOwner);
        }

        @NonNull
        static ViewHolder from(@NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new ViewHolder(ViewSoundtrackBinding.inflate(inflater, parent, false));
        }
    }
}