package de.pcps.jamtugether.model.beat;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.utils.TimeUtils;
import timber.log.Timber;

public class Beat {

    private final int ticksPerTact;

    /**
     * tact number per minute
     */
    private final int tempo;

    /**
     * <numerator> number of tick sounds should be
     * played evenly within these milliseconds
     */
    private transient final long millisPerTact;

    @NonNull
    public static transient final Beat DEFAULT = new Beat(2, 60);

    public Beat(int ticksPerTact, int tempo) {
        this.ticksPerTact = ticksPerTact;
        this.tempo = tempo;
        double tactNumberPerSecond = tempo / 60.0;
        Timber.d("tactNumberPerSecond: %s", tactNumberPerSecond);
        this.millisPerTact = (long) (TimeUtils.ONE_SECOND / tactNumberPerSecond);
    }

    @Override
    public String toString() {
        return "Beat{" +
                "ticksPerTact=" + ticksPerTact +
                ", tempo=" + tempo +
                ", millisPerTact=" + millisPerTact +
                '}';
    }

    public int getTicksPerTact() {
        return ticksPerTact;
    }

    public int getTempo() {
        return tempo;
    }

    public long getMillisPerTact() {
        return millisPerTact;
    }
}
