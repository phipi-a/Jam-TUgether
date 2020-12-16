package de.pcps.jamtugether.ui.soundtrack.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import de.pcps.jamtugether.model.instrument.Drums;
import de.pcps.jamtugether.model.instrument.Flute;
import de.pcps.jamtugether.model.instrument.base.Instrument;
import de.pcps.jamtugether.model.music.sound.Sound;
import de.pcps.jamtugether.model.music.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.music.soundtrack.SingleSoundtrack;

public class SoundtrackView extends View {

    private SingleSoundtrack singleSoundtrack;
    private CompositeSoundtrack compositeSoundtrack;

    private final Paint paint = new Paint();

    public SoundtrackView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setStrokeWidth(5f);
    }

    public void observeSingleSoundtrack(@NonNull LiveData<SingleSoundtrack> singleSoundtrack, @NonNull LifecycleOwner lifecycleOwner) {
        singleSoundtrack.observe(lifecycleOwner, this::onSingleSoundtrackChanged);
    }

    public void observeCompositeSoundtrack(@NonNull LiveData<CompositeSoundtrack> compositeSoundtrack, @NonNull LifecycleOwner lifecycleOwner) {
        compositeSoundtrack.observe(lifecycleOwner, this::onCompositeSoundtrackChanged);
    }

    public void onSingleSoundtrackChanged(@NonNull SingleSoundtrack singleSoundtrack) {
        this.singleSoundtrack = singleSoundtrack;
        this.invalidate();
    }

    private void onCompositeSoundtrackChanged(@NonNull CompositeSoundtrack compositeSoundtrack) {
        this.compositeSoundtrack = compositeSoundtrack;
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(singleSoundtrack != null) {
            drawSingleSoundtrack(canvas, singleSoundtrack);
        } else if(compositeSoundtrack != null) {
            drawCompositeSoundtrack(canvas);
        }
    }

    @ColorInt
    private int getPaintColor(@NonNull SingleSoundtrack singleSoundtrack) {
        final Instrument instrument = singleSoundtrack.getSoundSequence().get(0).getInstrument();
        if(instrument == Flute.getInstance()) {
            return Color.BLUE;
        } else if(instrument == Drums.getInstance()) {
            return Color.RED;
        } else {
            return Color.GREEN;
        }
    }

    private void drawSingleSoundtrack(@NonNull Canvas canvas, @NonNull SingleSoundtrack singleSoundtrack) {
        if(singleSoundtrack.getLength() == 0) {
            return;
        }

        paint.setColor(getPaintColor(singleSoundtrack));
        boolean line = false;
        if(line) {
            drawLines(canvas, singleSoundtrack);
        } else {
            drawRectangles(canvas, singleSoundtrack);
        }
    }

    private void drawLines(@NonNull Canvas canvas, @NonNull SingleSoundtrack singleSoundtrack) {
        float widthOfOneMilliSecond = this.getWidth() / (float) singleSoundtrack.getLength();
        float heightOfPitchOne = this.getHeight() / (float) Sound.PITCH_RANGE;
        float lastX = -1;
        float lastY = -1;

        for(Sound sound : singleSoundtrack.getSoundSequence()) {
            float xStart = this.getX() + widthOfOneMilliSecond * sound.getStartTime();
            float xEnd = this.getX() + widthOfOneMilliSecond * sound.getEndTime();
            float y = this.getY() + heightOfPitchOne * (Sound.MAX_PITCH - sound.getPitch());
            if(lastX == xStart && lastY != -1) {
                canvas.drawLine(xStart, lastY, xStart, y, paint);
            }
            canvas.drawLine(xStart, y, xEnd, y, paint);
            lastX = xEnd;
            lastY = y;
        }
    }

    private void drawRectangles(@NonNull Canvas canvas, @NonNull SingleSoundtrack singleSoundtrack) {
        float widthOfOneMilliSecond = this.getWidth() / (float) singleSoundtrack.getLength();
        float heightOfPitchOne = this.getHeight() / (float) Sound.PITCH_RANGE;
        for(Sound sound : singleSoundtrack.getSoundSequence()) {
            float xStart = this.getX() + widthOfOneMilliSecond * sound.getStartTime();
            float xEnd = this.getX() + widthOfOneMilliSecond * sound.getEndTime();
            float y = this.getY() + heightOfPitchOne * (Sound.MAX_PITCH - sound.getPitch());
            canvas.drawRect(xStart, y, xEnd, this.getY() + this.getHeight(), paint);
        }
    }

    private void drawCompositeSoundtrack(@NonNull Canvas canvas) {
        for(SingleSoundtrack singleSoundtrack : compositeSoundtrack.getSoundtracks()) {
            drawSingleSoundtrack(canvas, singleSoundtrack);
        }
    }
}
