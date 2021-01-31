package de.pcps.jamtugether.audio.sound;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.audio.sound.model.SoundWithStreamID;

public interface OnSoundWithIDPlayedCallback {

    void onSoundPlayed(@NonNull SoundWithStreamID soundWithStreamID);
}
