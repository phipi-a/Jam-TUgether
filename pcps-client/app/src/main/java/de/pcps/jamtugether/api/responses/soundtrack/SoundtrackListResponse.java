package de.pcps.jamtugether.api.responses.soundtrack;

import androidx.annotation.NonNull;

import java.util.List;

import de.pcps.jamtugether.models.music.soundtrack.SingleSoundtrack;

// todo replace with actual response
public class SoundtrackListResponse {

    @NonNull
    private final List<SingleSoundtrack> allSingleSoundtracks;

    public SoundtrackListResponse(@NonNull List<SingleSoundtrack> allSingleSoundtracks) {
        this.allSingleSoundtracks = allSingleSoundtracks;
    }

    @NonNull
    public List<SingleSoundtrack> getAllSingleSoundtracks() {
        return allSingleSoundtracks;
    }
}
