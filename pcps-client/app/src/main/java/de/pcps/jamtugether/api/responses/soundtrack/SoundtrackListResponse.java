package de.pcps.jamtugether.api.responses.soundtrack;

import androidx.annotation.NonNull;

import java.util.List;

import de.pcps.jamtugether.model.music.soundtrack.SingleSoundtrack;

// todo replace with actual response
public class SoundtrackListResponse {

    @NonNull
    private final List<SingleSoundtrack> allSoundtracks;

    public SoundtrackListResponse(@NonNull List<SingleSoundtrack> allSoundtracks) {
        this.allSoundtracks = allSoundtracks;
    }

    @NonNull
    public List<SingleSoundtrack> getAllSoundtracks() {
        return allSoundtracks;
    }
}
