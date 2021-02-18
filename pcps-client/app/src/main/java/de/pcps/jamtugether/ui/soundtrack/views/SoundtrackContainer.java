package de.pcps.jamtugether.ui.soundtrack.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;

public class SoundtrackContainer extends ConstraintLayout {

    @Nullable
    private SoundtrackView soundtrackView;

    @Nullable
    private SoundtrackNavigationLine line;

    public SoundtrackContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        soundtrackView = this.findViewById(R.id.soundtrack_view);
        line = this.findViewById(R.id.soundtrack_navigation_line);
    }

    public void onSingleSoundtrackChanged(@NonNull SingleSoundtrack soundtrack, @NonNull LifecycleOwner lifecycleOwner) {
        if (soundtrackView == null || line == null) {
            return;
        }
        soundtrackView.onSoundtrackChanged(soundtrack);
        line.onSoundtrackChanged(soundtrack, lifecycleOwner);
    }

    public void observeSingleSoundtrack(@NonNull LiveData<SingleSoundtrack> singleSoundtrack, @NonNull LifecycleOwner lifecycleOwner) {
        if (soundtrackView == null || line == null) {
            return;
        }
        soundtrackView.observeSingleSoundtrack(singleSoundtrack, lifecycleOwner);
        line.observeSingleSoundtrack(singleSoundtrack, lifecycleOwner);
    }

    public void observeCompositeSoundtrack(@NonNull LiveData<CompositeSoundtrack> compositeSoundtrack, @NonNull LifecycleOwner lifecycleOwner) {
        if (soundtrackView == null || line == null) {
            return;
        }
        soundtrackView.observeCompositeSoundtrack(compositeSoundtrack, lifecycleOwner);
        line.observeCompositeSoundtrack(compositeSoundtrack, lifecycleOwner);
    }

    @Nullable
    public SoundtrackView getSoundtrackView() {
        return soundtrackView;
    }
}
