package de.pcps.jamtugether.api.services;

import androidx.annotation.NonNull;

import java.util.List;

import de.pcps.jamtugether.content.soundtrack.Soundtrack;
import retrofit2.Call;

// todo add actual endpoints with correct response classes
public interface SoundtrackService {

    Call<List<Soundtrack>> getAllSoundtracks(int roomID);

    Call<Boolean> pushSoundtrack(@NonNull Soundtrack soundtrack, int roomID);
}
