package de.pcps.jamtugether.ui.soundtrack.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.databinding.ViewSoundtrackAdminBinding;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import de.pcps.jamtugether.ui.soundtrack.adapters.base.SoundtrackListAdapter;
import de.pcps.jamtugether.ui.soundtrack.views.SoundtrackContainer;

public class AdminSoundtrackListAdapter extends SoundtrackListAdapter<AdminSoundtrackListAdapter.ViewHolder> {

    @NonNull
    private final SingleSoundtrack.OnDeleteListener onDeleteListener;

    public AdminSoundtrackListAdapter(@NonNull Soundtrack.OnChangeCallback onChangeCallback, @NonNull SingleSoundtrack.OnDeleteListener onDeleteListener, @NonNull LifecycleOwner lifecycleOwner) {
        super(onChangeCallback, lifecycleOwner);
        this.onDeleteListener = onDeleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), onChangeCallback, onDeleteListener, lifecycleOwner);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ViewSoundtrackAdminBinding binding;

        ViewHolder(@NonNull ViewSoundtrackAdminBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(@NonNull SingleSoundtrack singleSoundtrack, @NonNull Soundtrack.OnChangeCallback onChangeCallback, @NonNull SingleSoundtrack.OnDeleteListener onDeleteListener, @NonNull LifecycleOwner lifecycleOwner) {
            binding.setSoundtrack(singleSoundtrack);
            binding.setOnChangeListener(onDeleteListener);

            binding.soundtrackControlsLayout.setSoundtrack(singleSoundtrack);
            binding.soundtrackControlsLayout.setOnChangeListener(onChangeCallback);
            binding.soundtrackControlsLayout.setLifecycleOwner(lifecycleOwner);
            binding.soundtrackControlsLayout.executePendingBindings();

            ((SoundtrackContainer) binding.soundtrackContainer).onSingleSoundtrackChanged(singleSoundtrack, lifecycleOwner);
        }

        @NonNull
        static ViewHolder from(@NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new ViewHolder(ViewSoundtrackAdminBinding.inflate(inflater, parent, false));
        }
    }
}