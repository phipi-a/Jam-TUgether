package de.pcps.jamtugether.ui.soundtrack.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import de.pcps.jamtugether.model.music.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.music.soundtrack.SingleSoundtrack;

public class SoundtrackView extends View {

    public SoundtrackView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void observeSingleSoundtrack(@NonNull LiveData<SingleSoundtrack> singleSoundtrack, @NonNull LifecycleOwner lifecycleOwner) {
        singleSoundtrack.observe(lifecycleOwner, this::draw);
    }

    public void observeCompositeSoundtrack(@NonNull LiveData<CompositeSoundtrack> compositeSoundtrack, @NonNull LifecycleOwner lifecycleOwner) {
        compositeSoundtrack.observe(lifecycleOwner, this::draw);
    }

    public void draw(@NonNull SingleSoundtrack singleSoundtrack) {
        // todo
    }

    private void draw(@NonNull CompositeSoundtrack soundtrack) {
        for(SingleSoundtrack singleSoundtrack : soundtrack.getSoundtracks()) {
            draw(singleSoundtrack);
        }
    }
}
