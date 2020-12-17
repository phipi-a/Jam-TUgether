package de.pcps.jamtugether.storage;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.model.instrument.base.Instrument;
import de.pcps.jamtugether.model.instrument.base.Instruments;

@Singleton
public class Preferences {

    public static final String FILE_NAME = "prefs_file_jam_tugether";

    private static final String USER_NEVER_CHOSE_INSTRUMENT_KEY = "pref_key_user_never_chose_instrument";
    private static final String MAIN_INSTRUMENT_KEY = "pref_key_main_instrument";

    @NonNull
    private final SharedPreferences sharedPreferences;

    @NonNull
    private final Instruments instruments;

    @Inject
    public Preferences(@NonNull SharedPreferences sharedPreferences, @NonNull Instruments instruments) {
        this.sharedPreferences = sharedPreferences;
        this.instruments = instruments;
    }

    private boolean getBoolean(@NonNull String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    private void setBoolean(@NonNull String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    @NonNull
    private String getString(@NonNull String key, @NonNull String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    private void setString(@NonNull String key, @NonNull String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public boolean userNeverChoseInstrument() {
        return getBoolean(USER_NEVER_CHOSE_INSTRUMENT_KEY, true);
    }

    public void setUserNeverChoseInstrument(boolean value) {
        setBoolean(USER_NEVER_CHOSE_INSTRUMENT_KEY, value);
    }

    @NonNull
    public Instrument getMainInstrument() {
        String preferenceValue = getString(MAIN_INSTRUMENT_KEY, instruments.getFallback().getPreferenceValue());
        return instruments.fromPreferences(preferenceValue);
    }

    public void setMainInstrument(@NonNull Instrument mainInstrument) {
        setString(MAIN_INSTRUMENT_KEY, mainInstrument.getPreferenceValue());
    }
}
