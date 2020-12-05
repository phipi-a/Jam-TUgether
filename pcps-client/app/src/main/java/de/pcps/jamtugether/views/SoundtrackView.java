package de.pcps.jamtugether.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import de.pcps.jamtugether.models.Soundtrack;

public class SoundtrackView extends View {

    public SoundtrackView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void observeSoundtrack(@NonNull LiveData<Soundtrack> sound, @NonNull LifecycleOwner lifecycleOwner) {
        sound.observe(lifecycleOwner, this::draw);
    }

    public void draw(@NonNull Soundtrack soundtrack) {
        // todo
    }
}
