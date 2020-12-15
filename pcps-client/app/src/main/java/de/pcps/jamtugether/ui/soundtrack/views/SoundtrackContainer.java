package de.pcps.jamtugether.ui.soundtrack.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.music.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.music.soundtrack.SingleSoundtrack;

public class SoundtrackContainer extends ConstraintLayout {

    private SoundtrackView soundtrackView;

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

    public void drawSingleSoundtrack(@NonNull SingleSoundtrack soundtrack, @NonNull LifecycleOwner lifecycleOwner) {
        soundtrackView.draw(soundtrack);
        line.onSoundtrackUpdated(soundtrack, lifecycleOwner);
    }

    public void observeSingleSoundtrack(@NonNull LiveData<SingleSoundtrack> singleSoundtrack, @NonNull LifecycleOwner lifecycleOwner) {
        soundtrackView.observeSingleSoundtrack(singleSoundtrack, lifecycleOwner);
        line.observeSingleSoundtrack(singleSoundtrack, lifecycleOwner);
    }

    public void observeCompositeSoundtrack(@NonNull LiveData<CompositeSoundtrack> compositeSoundtrack, @NonNull LifecycleOwner lifecycleOwner) {
        soundtrackView.observeCompositeSoundtrack(compositeSoundtrack, lifecycleOwner);
        line.observeCompositeSoundtrack(compositeSoundtrack, lifecycleOwner);
    }
}
