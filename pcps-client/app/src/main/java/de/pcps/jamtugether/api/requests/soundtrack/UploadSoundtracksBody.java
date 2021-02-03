package de.pcps.jamtugether.api.requests.soundtrack;

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
