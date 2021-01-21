package de.pcps.jamtugether.audio.instrument.base;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.annotation.StringRes;

import de.pcps.jamtugether.audio.sound.pool.base.BaseSoundPool;

public abstract class Instrument {

    private final int ordinal;

    @StringRes
    private final int name;

    @StringRes
    private final int helpMessage;

    @NonNull
    private final String preferenceValue;

    @NonNull
    private final String serverString;

    @Nullable
    protected BaseSoundPool soundPool;

    public Instrument(int ordinal, @StringRes int name, @StringRes int helpMessage, @NonNull String preferenceValue, @NonNull String serverString) {
        this.ordinal = ordinal;
        this.name = name;
        this.helpMessage = helpMessage;
        this.preferenceValue = preferenceValue;
        this.serverString = serverString;
    }

    @RawRes
    public abstract int getSoundResource(int pitch);

    @NonNull
    public abstract BaseSoundPool createSoundPool(@NonNull Context context);

    public void loadSounds(@NonNull Context context) {
        soundPool = createSoundPool(context);
    }

    public void stop() {
        if (soundPool != null) {
            soundPool.stopAllSounds();
        }
    }

    public int getOrdinal() {
        return ordinal;
    }

    @StringRes
    public int getName() {
        return name;
    }

    @StringRes
    public int getHelpMessage() {
        return helpMessage;
    }

    @NonNull
    public String getPreferenceValue() {
        return preferenceValue;
    }

    @NonNull
    public String getServerString() {
        return serverString;
    }

    /**
     * indicates whether the individual sounds of this instrument
     * need to be stopped manually
     */
    public abstract boolean soundsNeedToBeStopped();

    public abstract boolean soundsNeedToBeResumed();

    public interface OnSelectionListener {
        void onInstrumentSelected(@NonNull Instrument instrument);
    }

    public interface OnChangeCallback {
        void onInstrumentChanged(@NonNull Instrument instrument);
    }
}
