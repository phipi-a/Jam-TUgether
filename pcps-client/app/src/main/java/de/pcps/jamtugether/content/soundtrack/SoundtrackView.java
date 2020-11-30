package de.pcps.jamtugether.content.soundtrack;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

public class SoundtrackView extends View {

    public SoundtrackView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void observeSoundtrack(LiveData<Soundtrack> sound, LifecycleOwner lifecycleOwner) {
        sound.observe(lifecycleOwner, this::draw);
    }

    public void draw(@NonNull Soundtrack soundtrack) {
        // todo
    }
}
