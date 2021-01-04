package de.pcps.jamtugether.api.repositories;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.api.Constants;
import de.pcps.jamtugether.api.JamCallback;
import de.pcps.jamtugether.api.errors.base.Error;
import de.pcps.jamtugether.api.services.soundtrack.SoundtrackService;
import de.pcps.jamtugether.model.Composition;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import retrofit2.Call;
import timber.log.Timber;

@Singleton
public class SoundtrackRepository {

    @NonNull
    private final SoundtrackService soundtrackService;

    @NonNull
    private final Context context;

    private int currentRoomID;
    private String currentToken;

    private boolean fetching;

    @NonNull
    private final MutableLiveData<List<SingleSoundtrack>> allSoundtracks = new MutableLiveData<>();

    @NonNull
    private final MutableLiveData<Error> networkError = new MutableLiveData<>();

    @Inject
    public SoundtrackRepository(@NonNull SoundtrackService soundtrackService, @NonNull Context context) {
        this.soundtrackService = soundtrackService;
        this.context = context;
    }

    private void getComposition(@NonNull String token, int roomID, @NonNull JamCallback<Composition> callback) {
        Call<Composition> call = soundtrackService.getComposition(String.format(Constants.BEARER_TOKEN_FORMAT, token), roomID);
        call.enqueue(callback);
    }

    public void fetchSoundtracks(int currentRoomID, @NonNull String currentToken) {
        this.currentRoomID = currentRoomID;
        this.currentToken = currentToken;

        fetchSoundtracks();
        if (!fetching) {
            startFetchingSoundtracks();
            fetching = true;
        }
    }

    private void startFetchingSoundtracks() {
        Handler handler = new Handler();
        new Runnable() {

            @Override
            public void run() {
                if(currentToken == null || currentRoomID == -1) {
                    return;
                }
                fetchSoundtracks();
                handler.postDelayed(this, Constants.SOUNDTRACK_FETCHING_INTERVAL);
            }
        }.run();
    }

    private void fetchSoundtracks() {
        getComposition(currentToken, currentRoomID, new JamCallback<Composition>() {
            @Override
            public void onSuccess(@NonNull Composition response) {
                Timber.d("onSuccess()");
                List<SingleSoundtrack> newSoundtracks = new ArrayList<>();
                for (SingleSoundtrack soundtrack : response.getSoundtracks()) {
                    if (soundtrack != null) {
                        soundtrack.loadSounds(context);
                        newSoundtracks.add(soundtrack);
                    }
                }
                allSoundtracks.setValue(newSoundtracks);
            }

            @Override
            public void onError(@NonNull Error error) {
                Timber.d("onError()");
                networkError.setValue(error);
            }
        });
    }

    public void onUserLeftRoom() {
        currentToken = null;
        currentRoomID = -1;
    }

    // updates soundtracks locally so the change can be visible immediately
    public void updateAllSoundtracks(@NonNull List<SingleSoundtrack> soundtracks) {
        allSoundtracks.setValue(soundtracks);
    }

    public void onNetworkErrorShown() {
        networkError.setValue(null);
    }

    @NonNull
    public LiveData<List<SingleSoundtrack>> getAllSoundtracks() {
        return allSoundtracks;
    }

    @NonNull
    public LiveData<CompositeSoundtrack> getCompositeSoundtrack() {
        return Transformations.map(getAllSoundtracks(), allSoundtracks -> CompositeSoundtrack.from(allSoundtracks, context));
    }

    @NonNull
    public LiveData<Error> getNetworkError() {
        return networkError;
    }
}
