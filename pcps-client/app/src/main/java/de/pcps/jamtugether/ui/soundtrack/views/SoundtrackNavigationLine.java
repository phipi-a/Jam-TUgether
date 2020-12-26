package de.pcps.jamtugether.ui.soundtrack.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;

public class SoundtrackNavigationLine extends View {

    public SoundtrackNavigationLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void observeSingleSoundtrack(@NonNull LiveData<SingleSoundtrack> singleSoundtrack, @NonNull LifecycleOwner lifecycleOwner) {
        singleSoundtrack.observe(lifecycleOwner, soundtrack -> onSoundtrackChanged(soundtrack, lifecycleOwner));
    }

    public void observeCompositeSoundtrack(@NonNull LiveData<CompositeSoundtrack> compositeSoundtrack, @NonNull LifecycleOwner lifecycleOwner) {
        compositeSoundtrack.observe(lifecycleOwner, soundtrack -> onSoundtrackChanged(soundtrack, lifecycleOwner));
    }

    public void onSoundtrackChanged(@NonNull Soundtrack soundtrack, @NonNull LifecycleOwner lifecycleOwner) {
        soundtrack.getProgress().observe(lifecycleOwner, this::onProgressChanged);
    }

    private void onProgressChanged(Integer progress) {
        // todo draw
    }
}
