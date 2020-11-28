package de.pcps.jamtugether.api.responses;

import androidx.annotation.NonNull;

import java.util.List;

import de.pcps.jamtugether.content.soundtrack.Soundtrack;

// todo replace with actual response
public class SoundtrackListResponse {

    @NonNull
    private final List<Soundtrack> allSoundtracks;

    public SoundtrackListResponse(@NonNull List<Soundtrack> allSoundtracks) {
        this.allSoundtracks = allSoundtracks;
    }

    @NonNull
    public List<Soundtrack> getAllSoundtracks() {
        return allSoundtracks;
    }
}
