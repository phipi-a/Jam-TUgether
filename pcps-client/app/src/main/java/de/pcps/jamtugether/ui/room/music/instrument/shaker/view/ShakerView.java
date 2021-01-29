package de.pcps.jamtugether.ui.room.music.instrument.shaker.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import timber.log.Timber;

public class ShakerView extends AppCompatImageView {

    private boolean soundtracksExpanded = true;

    public ShakerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSoundtracksExpanded(boolean soundtracksExpanded) {
        this.soundtracksExpanded = soundtracksExpanded;
        Timber.d("soundtracksExpanded: %s", soundtracksExpanded);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
