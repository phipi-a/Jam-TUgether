package de.pcps.jamtugether.audio.sound;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.model.sound.SoundWithStreamID;

public interface OnSoundWithIDPlayedCallback {

    void onSoundPlayed(@NonNull SoundWithStreamID soundWithStreamID);
}
