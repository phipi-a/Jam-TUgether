package de.pcps.jamtugether.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import de.pcps.jamtugether.models.music.soundtrack.Soundtrack;
import de.pcps.jamtugether.models.music.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.models.music.soundtrack.SingleSoundtrack;

public class SoundtrackView extends View {

    public SoundtrackView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void observeSoundtrack(@NonNull LiveData<Soundtrack> soundtrack, @NonNull LifecycleOwner lifecycleOwner) {
        soundtrack.observe(lifecycleOwner, baseSoundtrack -> {
            if(baseSoundtrack instanceof SingleSoundtrack) {
                draw((SingleSoundtrack) baseSoundtrack);
            }
            if(baseSoundtrack instanceof CompositeSoundtrack) {
                draw((CompositeSoundtrack) baseSoundtrack);
            }
        });
    }

    public void draw(@NonNull SingleSoundtrack singleSoundtrack) {
        // todo
    }

    private void draw(@NonNull CompositeSoundtrack soundtrack) {
        // todo
    }
}
