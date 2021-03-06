package de.pcps.jamtugether.ui.soundtrack.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import java.util.HashMap;
import java.util.Map;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.drums.Drums;
import de.pcps.jamtugether.audio.instrument.flute.Flute;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.instrument.piano.Piano;
import de.pcps.jamtugether.audio.instrument.shaker.Shaker;
import de.pcps.jamtugether.model.Sound;
import de.pcps.jamtugether.audio.instrument.drums.DrumsSound;
import de.pcps.jamtugether.audio.instrument.flute.FluteSound;
import de.pcps.jamtugether.audio.instrument.piano.PianoSound;
import de.pcps.jamtugether.audio.instrument.shaker.ShakerSound;
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

    private static final Map<FluteSound, Integer> heightPercentages = new HashMap<>();

    static {
        heightPercentages.put(FluteSound.C_SHARP, 15);
        heightPercentages.put(FluteSound.D, 22);
        heightPercentages.put(FluteSound.D_SHARP, 29);
        heightPercentages.put(FluteSound.E, 36);
        heightPercentages.put(FluteSound.F, 43);
        heightPercentages.put(FluteSound.F_SHARP, 50);
        heightPercentages.put(FluteSound.G, 57);
        heightPercentages.put(FluteSound.G_SHARP, 64);
        heightPercentages.put(FluteSound.A, 71);
        heightPercentages.put(FluteSound.A_SHARP, 78);
        heightPercentages.put(FluteSound.B, 85);
        heightPercentages.put(FluteSound.C_HIGH, 92);
    }

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

    @ColorRes
    private static int getPaintColor(@NonNull SingleSoundtrack singleSoundtrack) {
        Instrument instrument = singleSoundtrack.getInstrument();
        if (instrument == Flute.getInstance()) {
            return R.color.soundtrackFluteColor;
        }
        if (instrument == Drums.getInstance()) {
            return R.color.soundtrackDrumsColor;
        }
        if (instrument == Shaker.getInstance()) {
            return R.color.soundtrackShakerColor;
        }
        return R.color.soundtrackPianoColor;
    }

    private static int getFluteSoundHeightPercentage(@NonNull FluteSound fluteSound) {
        if (heightPercentages.containsKey(fluteSound)) {
            return heightPercentages.get(fluteSound);
        } else return 8;
    }

    private static int getPianoSoundHeightPercentage(@NonNull PianoSound pianoSound) {
        if (heightPercentages.containsKey(pianoSound)) {
            return heightPercentages.get(pianoSound);
        } else return 8;
    }

    private static int getDrumsSoundHeightPercentage(@NonNull DrumsSound drumsSound) {
        switch (drumsSound) {
            case SNARE:
                return 50;
            case HAT:
                return 70;
            case CYMBAL:
                return 90;
            default:
                return 30;
        }
    }

    private static int getDrumsSoundHeight(@NonNull DrumsSound drumsSound, @NonNull Context context) {
        switch (drumsSound) {
            case SNARE:
                return UiUtils.getPixels(context, R.dimen.soundtrack_view_drums_snare_height);
            case HAT:
                return UiUtils.getPixels(context, R.dimen.soundtrack_view_drums_hat_height);
            case CYMBAL:
                return UiUtils.getPixels(context, R.dimen.soundtrack_view_drums_cymbal_height);
            default:
                return UiUtils.getPixels(context, R.dimen.soundtrack_view_drums_kick_height);
        }
    }

    private static long getDrumsSoundWidthInMillis(@NonNull DrumsSound drumsSound) {
        switch (drumsSound) {
            case SNARE:
                return 125;
            case HAT:
                return 75;
            case CYMBAL:
                return DrumsSound.CYMBAL.getDuration() - TimeUtils.ONE_SECOND;
            default:
                return 100;
        }
    }

    private void drawSingleSoundtrack(@NonNull Canvas canvas, @NonNull SingleSoundtrack singleSoundtrack) {
        if (singleSoundtrack.isEmpty()) {
            return;
        }
        paint.setColor(ContextCompat.getColor(getContext(), getPaintColor(singleSoundtrack)));
        if (singleSoundtrack.getInstrument() == Flute.getInstance()) {
            drawFluteSoundtrack(canvas, singleSoundtrack);
        } else if (singleSoundtrack.getInstrument() == Drums.getInstance()) {
            drawDrumsSoundtrack(canvas, singleSoundtrack);
        } else if (singleSoundtrack.getInstrument() == Shaker.getInstance()) {
            drawShakerSoundtrack(canvas, singleSoundtrack);
        } else {
            drawPianoSoundtrack(canvas, singleSoundtrack);
        }
    }

    private void drawInCompositeSoundtrack(@NonNull Canvas canvas, @NonNull SingleSoundtrack singleSoundtrack, int length) {
        if (singleSoundtrack.isEmpty()) {
            return;
        }
        paint.setColor(ContextCompat.getColor(getContext(), getPaintColor(singleSoundtrack)));
        if (singleSoundtrack.getInstrument() == Flute.getInstance()) {
            drawFluteInCompositeSoundtrack(canvas, singleSoundtrack, length);
        } else if (singleSoundtrack.getInstrument() == Drums.getInstance()) {
            drawDrumsSoundtrack(canvas, singleSoundtrack, length);
        } else if (singleSoundtrack.getInstrument() == Shaker.getInstance()) {
            drawShakerSoundtrack(canvas, singleSoundtrack, length);
        } else {
            drawPianoSoundtrack(canvas, singleSoundtrack, length);
        }
    }

    private void drawCompositeSoundtrack(@NonNull Canvas canvas, @NonNull CompositeSoundtrack compositeSoundtrack) {
        for (SingleSoundtrack singleSoundtrack : compositeSoundtrack.getSoundtracks()) {
            drawInCompositeSoundtrack(canvas, singleSoundtrack, compositeSoundtrack.getLength());
        }
    }

    private void drawFluteSoundtrack(@NonNull Canvas canvas, @NonNull SingleSoundtrack singleSoundtrack) {
        float widthOfOneMilliSecond = this.getWidth() / (float) singleSoundtrack.getLength();
        float heightOfPitchOne = this.getHeight() / (float) Flute.PITCH_RANGE;
        for (Sound sound : singleSoundtrack.getSoundSequence()) {
            float xStart = this.getX() + widthOfOneMilliSecond * sound.getStartTime();
            float xEnd = this.getX() + widthOfOneMilliSecond * sound.getEndTime();
            FluteSound fluteSound = FluteSound.values()[sound.getPitch()];
            float y = this.getY() + heightOfPitchOne * (Flute.MAX_PITCH - getFluteSoundHeightPercentage(fluteSound));
            canvas.drawRect(xStart, y, xEnd, this.getY() + this.getHeight(), paint);
        }
    }

    private void drawFluteInCompositeSoundtrack(@NonNull Canvas canvas, @NonNull SingleSoundtrack singleSoundtrack, int length) {
        float widthOfOneMilliSecond = this.getWidth() / (float) length;
        float heightOfPitchOne = this.getHeight() / (float) Flute.PITCH_RANGE;
        float lastX = -1;
        float lastY = -1;

        for (Sound sound : singleSoundtrack.getSoundSequence()) {
            float xStart = this.getX() + widthOfOneMilliSecond * sound.getStartTime();
            float xEnd = this.getX() + widthOfOneMilliSecond * sound.getEndTime();
            FluteSound fluteSound = FluteSound.from(sound.getPitch());
            float y = this.getY() + heightOfPitchOne * (Flute.MAX_PITCH - getFluteSoundHeightPercentage(fluteSound));
            if (lastX == xStart && lastY != -1) {
                canvas.drawLine(xStart, lastY, xStart, y, paint);
            }
            canvas.drawLine(xStart, y, xEnd, y, paint);
            lastX = xEnd;
            lastY = y;
        }
    }

    private void drawDrumsSoundtrack(@NonNull Canvas canvas, @NonNull SingleSoundtrack singleSoundtrack) {
        drawDrumsSoundtrack(canvas, singleSoundtrack, singleSoundtrack.getLength());
    }

    private void drawDrumsSoundtrack(@NonNull Canvas canvas, @NonNull SingleSoundtrack singleSoundtrack, int length) {
        float widthOfOneMilliSecond = this.getWidth() / (float) length;
        float heightOfPitchOne = this.getHeight() / (float) Drums.PITCH_RANGE;
        for (Sound sound : singleSoundtrack.getSoundSequence()) {
            DrumsSound drumsSound = DrumsSound.from(sound.getPitch());
            int heightPercentage = getDrumsSoundHeightPercentage(drumsSound);
            int height = getDrumsSoundHeight(drumsSound, getContext());
            long widthInMillis = getDrumsSoundWidthInMillis(drumsSound);

            float xStart = this.getX() + widthOfOneMilliSecond * sound.getStartTime();
            float yStart = this.getY() + heightOfPitchOne * (Drums.MAX_PITCH - heightPercentage);
            float xEnd = xStart + widthOfOneMilliSecond * widthInMillis;
            float yEnd = yStart + height;
            canvas.drawRect(xStart, yStart, xEnd, yEnd, paint);
        }
    }

    private void drawShakerSoundtrack(@NonNull Canvas canvas, @NonNull SingleSoundtrack singleSoundtrack) {
        drawShakerSoundtrack(canvas, singleSoundtrack, singleSoundtrack.getLength());
    }

    private void drawShakerSoundtrack(@NonNull Canvas canvas, @NonNull SingleSoundtrack singleSoundtrack, int length) {
        float widthOfOneMilliSecond = this.getWidth() / (float) length;
        int containerHeight = UiUtils.getPixels(getContext(), R.dimen.soundtrack_view_height);
        float height = containerHeight / 2.0f;
        float borderSpace = (containerHeight - height) / 2;
        float yStart = this.getY() + borderSpace;
        float yEnd = this.getY() + this.getHeight() - borderSpace;
        float millis = ShakerSound.SHAKER.getDuration() / 1.5f;
        for (Sound sound : singleSoundtrack.getSoundSequence()) {
            float xStart = this.getX() + widthOfOneMilliSecond * sound.getStartTime();
            float xEnd = xStart + widthOfOneMilliSecond * millis;
            canvas.drawOval(xStart, yStart, xEnd, yEnd, paint);
        }
    }

    private void drawPianoSoundtrack(@NonNull Canvas canvas, @NonNull SingleSoundtrack singleSoundtrack) {
        drawPianoSoundtrack(canvas, singleSoundtrack, singleSoundtrack.getLength());
    }

    private void drawPianoSoundtrack(@NonNull Canvas canvas, @NonNull SingleSoundtrack singleSoundtrack, int length) {
        float widthOfOneMilliSecond = this.getWidth() / (float) length;
        float heightOfPitchOne = this.getHeight() / (float) Piano.PITCH_RANGE;
        float lastX = -1;
        float lastY = -1;

        for (Sound sound : singleSoundtrack.getSoundSequence()) {
            float xStart = this.getX() + widthOfOneMilliSecond * sound.getStartTime();
            float xEnd = this.getX() + widthOfOneMilliSecond * sound.getEndTime();
            PianoSound pianoSound = PianoSound.from(sound.getPitch());
            float y = this.getY() + heightOfPitchOne * (Piano.MAX_PITCH - getPianoSoundHeightPercentage(pianoSound));
            if (lastX == xStart && lastY != -1) {
                canvas.drawLine(xStart, lastY, xStart, y, paint);
            }
            canvas.drawLine(xStart, y, xEnd, y, paint);
            lastX = xEnd;
            lastY = y;
        }
    }
}
