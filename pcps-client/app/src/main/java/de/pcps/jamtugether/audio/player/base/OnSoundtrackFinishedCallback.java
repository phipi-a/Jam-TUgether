package de.pcps.jamtugether.audio.player.base;

import androidx.annotation.NonNull;

public interface OnSoundtrackFinishedCallback {

    void onSoundtrackFinished(@NonNull SoundtrackPlayingThread thread);
}
