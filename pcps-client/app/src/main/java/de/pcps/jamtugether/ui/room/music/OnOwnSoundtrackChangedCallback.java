package de.pcps.jamtugether.ui.room.music;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;

public interface OnOwnSoundtrackChangedCallback {

    void onOwnSoundtrackChanged(@NonNull SingleSoundtrack ownSoundtrack);
}
