package de.pcps.jamtugether.content.soundtrack.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import de.pcps.jamtugether.models.Soundtrack;
import de.pcps.jamtugether.databinding.ViewSoundtrackAdminBinding;

public class AdminSoundtrackListAdapter extends SoundtrackListAdapter<AdminSoundtrackListAdapter.ViewHolder> {

    @NonNull
    private final Soundtrack.OnDeleteListener onDeleteListener;

    public AdminSoundtrackListAdapter(@NonNull Soundtrack.OnChangeListener onChangeListener, @NonNull Soundtrack.OnDeleteListener onDeleteListener, @NonNull LifecycleOwner lifecycleOwner) {
        super(onChangeListener, lifecycleOwner);
        this.onDeleteListener = onDeleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), onChangeListener, onDeleteListener, lifecycleOwner);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ViewSoundtrackAdminBinding binding;

        ViewHolder(@NonNull ViewSoundtrackAdminBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(@NonNull Soundtrack soundtrack, @NonNull Soundtrack.OnChangeListener onChangeListener, @NonNull Soundtrack.OnDeleteListener onDeleteListener, @NonNull LifecycleOwner lifecycleOwner) {
            binding.setSoundtrack(soundtrack);
            binding.setOnChangeListener(onDeleteListener);

            binding.soundtrackControlsLayout.setSoundtrack(soundtrack);
            binding.soundtrackControlsLayout.setOnChangeListener(onChangeListener);
            binding.soundtrackControlsLayout.setLifecycleOwner(lifecycleOwner);
            binding.soundtrackControlsLayout.executePendingBindings();

            binding.soundtrackView.draw(soundtrack);
        }

        @NonNull
        static ViewHolder from(@NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new ViewHolder(ViewSoundtrackAdminBinding.inflate(inflater, parent, false));
        }
    }
}