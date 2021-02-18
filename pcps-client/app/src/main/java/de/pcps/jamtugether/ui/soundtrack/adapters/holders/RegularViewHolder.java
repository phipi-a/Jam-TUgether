package de.pcps.jamtugether.ui.soundtrack.adapters.holders;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import de.pcps.jamtugether.databinding.ViewSoundtrackBinding;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import de.pcps.jamtugether.ui.soundtrack.adapters.holders.base.BaseViewHolder;
import de.pcps.jamtugether.ui.soundtrack.views.SoundtrackContainer;
import timber.log.Timber;

public class RegularViewHolder extends BaseViewHolder {

    @NonNull
    private final ViewSoundtrackBinding binding;

    RegularViewHolder(@NonNull ViewSoundtrackBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    @Override
    public void bind(@NonNull SingleSoundtrack singleSoundtrack, @NonNull Soundtrack.OnChangeCallback onChangeCallback, @NonNull SingleSoundtrack.OnDeleteListener onDeleteListener, @NonNull LifecycleOwner lifecycleOwner) {
        binding.setSoundtrack(singleSoundtrack);
        binding.soundtrackControlsLayout.setSoundtrack(singleSoundtrack);
        binding.soundtrackControlsLayout.setOnChangeListener(onChangeCallback);
        binding.soundtrackControlsLayout.setLifecycleOwner(lifecycleOwner);
        binding.soundtrackControlsLayout.executePendingBindings();

        binding.soundtrackContainer.onSingleSoundtrackChanged(singleSoundtrack, lifecycleOwner);
    }

    @NonNull
    public static RegularViewHolder from(@NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new RegularViewHolder(ViewSoundtrackBinding.inflate(inflater, parent, false));
    }
}