package de.pcps.jamtugether.storage;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.instrument.base.Instruments;

@Singleton
public class Preferences {

    public static final String FILE_NAME = "prefs_file_jam_tugether";

    private static final String USER_COMPLETED_ON_BOARDING_KEY = "pref_key_user_completed_on_boarding";
    private static final String MAIN_INSTRUMENT_KEY = "pref_key_main_instrument";
    private static final String USER_SAW_UPLOAD_REMINDER_DIALOG = "pref_key_user_saw_upload_reminder_dialog";

    @NonNull
    private final SharedPreferences sharedPreferences;

    @Inject
    public Preferences(@NonNull SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    private void setBoolean(@NonNull String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    private boolean getBoolean(@NonNull String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public boolean userCompletedOnBoarding() {
        return getBoolean(USER_COMPLETED_ON_BOARDING_KEY, false);
    }

    public void setUserCompletedOnBoarding(boolean value) {
        setBoolean(USER_COMPLETED_ON_BOARDING_KEY, value);
    }

    public boolean userSawUploadReminderDialog() {
        return getBoolean(USER_SAW_UPLOAD_REMINDER_DIALOG, false);
    }

    public void setUserSawUploadReminderDialog(boolean value) {
        setBoolean(USER_SAW_UPLOAD_REMINDER_DIALOG, value);
    }

    @NonNull
    public Instrument getMainInstrument() {
        String preferenceValue = sharedPreferences.getString(MAIN_INSTRUMENT_KEY, Instruments.DEFAULT.getPreferenceValue());
        return Instruments.fromPreferences(preferenceValue);
    }

    public void setMainInstrument(@NonNull Instrument mainInstrument) {
        sharedPreferences.edit().putString(MAIN_INSTRUMENT_KEY, mainInstrument.getPreferenceValue()).apply();
    }
}
