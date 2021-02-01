package de.pcps.jamtugether.model;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.utils.TimeUtils;

public class Beat {

    @NonNull
    public static transient final Beat DEFAULT = new Beat(4, 60);

    private static transient final int MIN_TICKS = 1;
    private static transient final int MAX_TICKS = 10;

    public static final Integer[] TICKS_OPTIONS = new Integer[MAX_TICKS - MIN_TICKS + 1];

    private static transient final int MIN_TEMPO = 1;
    private static transient final int MAX_TEMPO = 300;

    public static final Integer[] TEMPO_OPTIONS = new Integer[MAX_TEMPO - MIN_TEMPO + 1];

    static {

        for (int ticks = MIN_TICKS, i = 0; ticks <= MAX_TICKS; ticks++, i++) {
            TICKS_OPTIONS[i] = ticks;
        }

        for (int tempo = MIN_TEMPO, i = 0; tempo <= MAX_TEMPO; tempo++, i++) {
            TEMPO_OPTIONS[i] = tempo;
        }
    }

    private final int ticksPerTact;

    /**
     * tact number per minute
     */
    private final int tempo;

    public Beat(int ticksPerTact, int tempo) {
        this.ticksPerTact = ticksPerTact;
        this.tempo = tempo;
    }

    public int getTicksPerTact() {
        return ticksPerTact;
    }

    public int getTempo() {
        return tempo;
    }

    /**
     * <ticksPerTact> tick sounds should be
     * played evenly within these milliseconds
     */
    public long getMillisPerTact() {
        double tactNumberPerSecond = tempo / 60.0;
        return (long) (TimeUtils.ONE_SECOND / tactNumberPerSecond);
    }
}
