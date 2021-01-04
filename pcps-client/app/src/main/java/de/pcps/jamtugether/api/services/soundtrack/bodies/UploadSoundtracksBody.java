package de.pcps.jamtugether.api.services.soundtrack.bodies;

import androidx.annotation.NonNull;

import java.util.List;

import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;

public class UploadSoundtracksBody {

    @NonNull
    private final List<SingleSoundtrack> soundtracks;

    public UploadSoundtracksBody(@NonNull List<SingleSoundtrack> soundtracks) {
        this.soundtracks = soundtracks;
    }
}
