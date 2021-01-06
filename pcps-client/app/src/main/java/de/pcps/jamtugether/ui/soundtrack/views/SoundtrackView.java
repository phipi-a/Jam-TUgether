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

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.sound.SoundResource;
import de.pcps.jamtugether.audio.instrument.drums.Drums;
import de.pcps.jamtugether.audio.instrument.flute.Flute;
import de.pcps.jamtugether.audio.instrument.shaker.Shaker;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.model.sound.Sound;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import de.pcps.jamtugether.utils.TimeUtils;
import de.pcps.jamtugether.utils.UiUtils;

public class SoundtrackView extends View {

    @Nullable
    private Soundtrack soundtrack;

    @NonNull
    private final Paint paint = new Paint();

    public SoundtrackView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setStrokeWidth(5f);
    }

    public void observeSingleSoundtrack(@NonNull LiveData<SingleSoundtrack> singleSoundtrack, @NonNull LifecycleOwner lifecycleOwner) {
        singleSoundtrack.observe(lifecycleOwner, this::onSoundtrackChanged);
    }

    public void observeCompositeSoundtrack(@NonNull LiveData<CompositeSoundtrack> compositeSoundtrack, @NonNull LifecycleOwner lifecycleOwner) {
        compositeSoundtrack.observe(lifecycleOwner, this::onSoundtrackChanged);
    }

    public void onSoundtrackChanged(@NonNull Soundtrack soundtrack) {
        this.soundtrack = soundtrack;
        this.invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (soundtrack == null) {
            return;
        }
        if (soundtrack instanceof SingleSoundtrack) {
            SingleSoundtrack singleSoundtrack = (SingleSoundtrack) soundtrack;
            drawSingleSoundtrack(canvas, singleSoundtrack);
        } else {
            CompositeSoundtrack compositeSoundtrack = (CompositeSoundtrack) soundtrack;
            drawCompositeSoundtrack(canvas, compositeSoundtrack);
        }
    }

    @ColorInt
    private int getPaintColor(@NonNull SingleSoundtrack singleSoundtrack) {
        Instrument instrument = singleSoundtrack.getInstrument();
        if (instrument == Flute.getInstance()) {
            return Color.BLUE;
        }
        if (instrument == Drums.getInstance()) {
            return Color.RED;
        }
        if (instrument == Shaker.getInstance()) {
            return Color.GREEN;
        }
        return Color.GRAY;
    }

    private void drawSingleSoundtrack(@NonNull Canvas canvas, @NonNull SingleSoundtrack singleSoundtrack) {
        if (singleSoundtrack.isEmpty()) {
            return;
        }
        paint.setColor(getPaintColor(singleSoundtrack));
        if (singleSoundtrack.getInstrument() == Flute.getInstance()) {
            drawFluteSoundtrack(canvas, singleSoundtrack);
        } else if (singleSoundtrack.getInstrument() == Drums.getInstance()) {
            drawDrumsSoundtrack(canvas, singleSoundtrack);
        } else {
            drawShakerSoundtrack(canvas, singleSoundtrack);
        }
    }

    private void drawInCompositeSoundtrack(@NonNull Canvas canvas, @NonNull SingleSoundtrack singleSoundtrack) {
        if (singleSoundtrack.isEmpty()) {
            return;
        }
        paint.setColor(getPaintColor(singleSoundtrack));
        if (singleSoundtrack.getInstrument() == Flute.getInstance()) {
            drawFluteInCompositeSoundtrack(canvas, singleSoundtrack);
        } else if (singleSoundtrack.getInstrument() == Drums.getInstance()) {
            drawDrumsSoundtrack(canvas, singleSoundtrack);
        } else {
            drawShakerSoundtrack(canvas, singleSoundtrack);
        }
    }

    private void drawCompositeSoundtrack(@NonNull Canvas canvas, @NonNull CompositeSoundtrack compositeSoundtrack) {
        for (SingleSoundtrack singleSoundtrack : compositeSoundtrack.getSoundtracks()) {
            drawInCompositeSoundtrack(canvas, singleSoundtrack);
        }
    }

    private void drawFluteSoundtrack(@NonNull Canvas canvas, @NonNull SingleSoundtrack singleSoundtrack) {
        float widthOfOneMilliSecond = this.getWidth() / (float) singleSoundtrack.getLength();
        float heightOfPitchOne = this.getHeight() / (float) Flute.PITCH_RANGE;
        for (Sound sound : singleSoundtrack.getSoundSequence()) {
            float xStart = this.getX() + widthOfOneMilliSecond * sound.getStartTime();
            float xEnd = this.getX() + widthOfOneMilliSecond * sound.getEndTime();
            float y = this.getY() + heightOfPitchOne * (Flute.MAX_PITCH - sound.getPitch());
            canvas.drawRect(xStart, y, xEnd, this.getY() + this.getHeight(), paint);
        }
    }

    private void drawFluteInCompositeSoundtrack(@NonNull Canvas canvas, @NonNull SingleSoundtrack singleSoundtrack) {
        float widthOfOneMilliSecond = this.getWidth() / (float) singleSoundtrack.getLength();
        float heightOfPitchOne = this.getHeight() / (float) Flute.PITCH_RANGE;
        float lastX = -1;
        float lastY = -1;

        for (Sound sound : singleSoundtrack.getSoundSequence()) {
            float xStart = this.getX() + widthOfOneMilliSecond * sound.getStartTime();
            float xEnd = this.getX() + widthOfOneMilliSecond * sound.getEndTime();
            float y = this.getY() + heightOfPitchOne * (Flute.MAX_PITCH - sound.getPitch());
            if (lastX == xStart && lastY != -1) {
                canvas.drawLine(xStart, lastY, xStart, y, paint);
            }
            canvas.drawLine(xStart, y, xEnd, y, paint);
            lastX = xEnd;
            lastY = y;
        }
    }

    private void drawDrumsSoundtrack(@NonNull Canvas canvas, @NonNull SingleSoundtrack singleSoundtrack) {
        float widthOfOneMilliSecond = this.getWidth() / (float) singleSoundtrack.getLength();
        float heightOfPitchOne = this.getHeight() / (float) Drums.PITCH_RANGE;
        for (Sound sound : singleSoundtrack.getSoundSequence()) {
            float height;
            long millis;
            int heightPercentage = sound.getPitch();
            switch (sound.getPitch()) {
                case Drums.SNARE_PITCH:
                    millis = 125;
                    height = UiUtils.getPixels(this.getContext(), R.dimen.soundtrack_view_drums_snare_height);
                    break;
                case Drums.KICK_PITCH:
                    millis = 100;
                    height = UiUtils.getPixels(this.getContext(), R.dimen.soundtrack_view_drums_kick_height);
                    break;
                case Drums.HAT_PITCH:
                    millis = 75;
                    height = UiUtils.getPixels(this.getContext(), R.dimen.soundtrack_view_drums_hat_height);
                    break;
                default:
                    millis = SoundResource.CYMBAL.getDuration() - TimeUtils.ONE_SECOND;
                    height = UiUtils.getPixels(this.getContext(), R.dimen.soundtrack_view_drums_cymbal_height);
                    break;
            }
            float xStart = this.getX() + widthOfOneMilliSecond * sound.getStartTime();
            float yStart = this.getY() + heightOfPitchOne * (Drums.MAX_PITCH - heightPercentage);
            float xEnd = xStart + widthOfOneMilliSecond * millis;
            float yEnd = yStart + height;
            canvas.drawRect(xStart, yStart, xEnd, yEnd, paint);
        }
    }

    private void drawShakerSoundtrack(@NonNull Canvas canvas, @NonNull SingleSoundtrack singleSoundtrack) {
        float widthOfOneMilliSecond = this.getWidth() / (float) singleSoundtrack.getLength();
        int containerHeight = UiUtils.getPixels(getContext(), R.dimen.soundtrack_view_height);
        float height = containerHeight / 2.0f;
        float borderSpace = (containerHeight - height) / 2;
        float yStart = this.getY() + borderSpace;
        float yEnd = this.getY() + this.getHeight() - borderSpace;
        float millis = SoundResource.SHAKER.getDuration() / 1.5f;
        for (Sound sound : singleSoundtrack.getSoundSequence()) {
            float xStart = this.getX() + widthOfOneMilliSecond * sound.getStartTime();
            float xEnd = xStart + widthOfOneMilliSecond * millis;
            canvas.drawOval(xStart, yStart, xEnd, yEnd, paint);
        }
    }
}
