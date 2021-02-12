package de.pcps.jamtugether.ui.soundtrack.views;

import android.content.Context;
import android.graphics.Canvas;
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

    private float progress;

    @Nullable
    private Soundtrack currentSoundtrack;

    public SoundtrackNavigationLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        SoundtrackContainer container = (SoundtrackContainer) getParent();
        SoundtrackView soundtrackView = container.getSoundtrackView();
        if (soundtrackView == null) {
            return;
        }
        float onePercentWidth = (soundtrackView.getWidth() - this.getWidth()) / 100.0f;
        this.setX(soundtrackView.getX() + onePercentWidth * progress);
    }

    public void observeSingleSoundtrack(@NonNull LiveData<SingleSoundtrack> singleSoundtrack, @NonNull LifecycleOwner lifecycleOwner) {
        singleSoundtrack.observe(lifecycleOwner, soundtrack -> onSoundtrackChanged(soundtrack, lifecycleOwner));
    }

    public void observeCompositeSoundtrack(@NonNull LiveData<CompositeSoundtrack> compositeSoundtrack, @NonNull LifecycleOwner lifecycleOwner) {
        compositeSoundtrack.observe(lifecycleOwner, soundtrack -> onSoundtrackChanged(soundtrack, lifecycleOwner));
    }

    public void onSoundtrackChanged(@NonNull Soundtrack soundtrack, @NonNull LifecycleOwner lifecycleOwner) {
        this.currentSoundtrack = soundtrack;
        soundtrack.getProgress().observe(lifecycleOwner, integer -> {
            // progress should only be updated if the soundtrack this progress belongs to
            // is the current soundtrack that this line represents
            // needed due to the possible delay of dispatching live data updates from a background thread

            if (currentSoundtrack == soundtrack) {
                onProgressChanged(integer);
            }
        });
    }

    private void onProgressChanged(Float progress) {
        if (this.progress == progress) { // only update progress if needed
            return;
        }
        this.progress = progress;
        this.invalidate();
    }
}
