package de.pcps.jamtugether.model.beat;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.utils.TimeUtils;

public class Beat {

    private final int ticksPerTact;

    /**
     * tact number per minute
     */
    private final int tempo;

    /**
     * <numerator> number of sounds should be
     * played evenly within these milliseconds
     */
    private transient final long millisPerTact;

    @NonNull
    public static transient final Beat DEFAULT = new Beat(4, 60);

    public Beat(int ticksPerTact, int tempo) {
        this.ticksPerTact = ticksPerTact;
        this.tempo = tempo;
        double tactNumberPerSecond = tempo / (double) TimeUtils.ONE_SECOND;
        this.millisPerTact = (long) (TimeUtils.ONE_SECOND / tactNumberPerSecond);
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
