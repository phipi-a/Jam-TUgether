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

    private int progress;

    public SoundtrackNavigationLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        SoundtrackContainer container = (SoundtrackContainer) getParent();
        SoundtrackView soundtrackView = container.getSoundtrackView();
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
        soundtrack.getProgress().observe(lifecycleOwner, this::onProgressChanged);
    }

    private void onProgressChanged(Integer progress) {
        this.progress = progress;
        this.invalidate();
    }
}
