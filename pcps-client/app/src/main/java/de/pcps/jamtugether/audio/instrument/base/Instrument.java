package de.pcps.jamtugether.audio.instrument.base;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.DiffUtil;

import de.pcps.jamtugether.audio.soundpool.base.BaseSoundPool;

public abstract class Instrument {

    @NonNull
    public static final DiffUtil.ItemCallback<Instrument> DIFF_UTIL_CALLBACK = new DiffUtil.ItemCallback<Instrument>() {

        @Override
        public boolean areItemsTheSame(@NonNull Instrument oldItem, @NonNull Instrument newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Instrument oldItem, @NonNull Instrument newItem) {
            return true;
        }
    };

    private final int ordinal;

    @StringRes
    private final int name;

    @StringRes
    private final int helpMessage;

    @NonNull
    private final String preferenceValue;

    @NonNull
    private final String serverString;

    protected BaseSoundPool soundPool;

    public Instrument(int ordinal, @StringRes int name, @StringRes int helpMessage, @NonNull String preferenceValue, @NonNull String serverString) {
        this.ordinal = ordinal;
        this.name = name;
        this.helpMessage = helpMessage;
        this.preferenceValue = preferenceValue;
        this.serverString = serverString;
    }

    @RawRes
    public abstract int getSoundResource(int element);

    @NonNull
    public abstract BaseSoundPool createSoundPool(@NonNull Context context);

    public void loadSounds(@NonNull Context context) {
        soundPool = createSoundPool(context);
    }

    public int stop() {
        soundPool.stopAllSounds();
        return 0;
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

    public interface ClickListener {
        void onInstrumentClicked(@NonNull Instrument instrument);
    }

    public interface OnChangeCallback {
        void onInstrumentChanged(@NonNull Instrument instrument);
    }
}
