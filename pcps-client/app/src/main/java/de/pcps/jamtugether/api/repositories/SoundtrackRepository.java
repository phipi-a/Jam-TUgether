package de.pcps.jamtugether.api.repositories;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import de.pcps.jamtugether.api.responses.soundtrack.UploadSoundtracksResponse;
import de.pcps.jamtugether.api.services.soundtrack.SoundtrackService;
import de.pcps.jamtugether.api.services.soundtrack.bodies.UploadSoundtracksBody;
import de.pcps.jamtugether.model.Composition;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import retrofit2.Call;
import timber.log.Timber;

@Singleton
public class SoundtrackRepository {

    @NonNull
    private final SoundtrackService soundtrackService;

    @NonNull
    private final Context context;

    private int currentRoomID;

    @Nullable
    private String currentToken;

    @NonNull
    private final Handler handler = new Handler();

    @Nullable
    private Runnable soundtrackFetchingRunnable;

    @NonNull
    private final MutableLiveData<List<SingleSoundtrack>> allSoundtracks = new MutableLiveData<>(new ArrayList<>());

    @NonNull
    private final MutableLiveData<Boolean> showCompositionIsLoading = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Error> compositionNetworkError = new MutableLiveData<>(null);

    private boolean loadingCompositionOfCurrentRoomShown;
    private boolean networkErrorOfCurrentRoomShown;

    @Inject
    public SoundtrackRepository(@NonNull SoundtrackService soundtrackService, @NonNull Context context) {
        this.soundtrackService = soundtrackService;
        this.context = context;
    }

    private void getComposition(@NonNull String token, int roomID, @NonNull JamCallback<Composition> callback) {
        Call<Composition> call = soundtrackService.getComposition(String.format(Constants.BEARER_TOKEN_FORMAT, token), roomID);
        call.enqueue(callback);
    }

    public void uploadSoundtracks(@NonNull String token, int roomID, @NonNull List<SingleSoundtrack> soundtracks, @NonNull JamCallback<UploadSoundtracksResponse> callback) {
        UploadSoundtracksBody body = new UploadSoundtracksBody(soundtracks);
        Call<UploadSoundtracksResponse> call = soundtrackService.uploadSoundtracks(String.format(Constants.BEARER_TOKEN_FORMAT, token), roomID, body);
        call.enqueue(callback);
    }

    public void fetchSoundtracks(int currentRoomID, @NonNull String currentToken, boolean requestedFromUser) {
        this.currentRoomID = currentRoomID;
        this.currentToken = currentToken;

        fetchSoundtracks(requestedFromUser);

        if (!requestedFromUser) {
            if (soundtrackFetchingRunnable == null) {
                startFetchingSoundtracks();
            }
        }
    }

    private void startFetchingSoundtracks() {
        soundtrackFetchingRunnable = new Runnable() {

            @Override
            public void run() {
                if (currentToken == null || currentRoomID == -1) {
                    return;
                }
                fetchSoundtracks(false);
                handler.postDelayed(this, Constants.SOUNDTRACK_FETCHING_INTERVAL);
            }
        };
        soundtrackFetchingRunnable.run();
    }

    private void fetchSoundtracks(boolean requestedFromUser) {
        if(!loadingCompositionOfCurrentRoomShown || requestedFromUser) { // loading for the first time or requested from user
            showCompositionIsLoading.setValue(true);
            loadingCompositionOfCurrentRoomShown = true;
        }
        getComposition(currentToken, currentRoomID, new JamCallback<Composition>() {
            @Override
            public void onSuccess(@NonNull Composition response) {
                showCompositionIsLoading.setValue(false);
                List<SingleSoundtrack> newSoundtracks = new ArrayList<>();
                for (SingleSoundtrack soundtrack : response.getSoundtracks()) {
                    if (soundtrack != null) {
                        soundtrack.loadSounds(context);
                        newSoundtracks.add(soundtrack);
                        Timber.d("soundtrack: %s", soundtrack.toString());
                    }
                }
                allSoundtracks.setValue(newSoundtracks);
            }

            @Override
            public void onError(@NonNull Error error) {
                showCompositionIsLoading.setValue(false);
                if (!networkErrorOfCurrentRoomShown || requestedFromUser) { // loading for the first time or requested from user
                    compositionNetworkError.setValue(error);
                    networkErrorOfCurrentRoomShown = true;
                }
            }
        });
    }

    public void onUserLeftRoom() {
        if (soundtrackFetchingRunnable != null) {
            handler.removeCallbacks(soundtrackFetchingRunnable);
            soundtrackFetchingRunnable = null;
        }
        currentToken = null;
        currentRoomID = -1;
        loadingCompositionOfCurrentRoomShown = false;
        networkErrorOfCurrentRoomShown = false;
    }

    // updates soundtracks locally so that the change can be visible immediately
    public void updateAllSoundtracks(@NonNull List<SingleSoundtrack> soundtracks) {
        allSoundtracks.setValue(soundtracks);
    }

    public void onCompositionNetworkErrorShown() {
        compositionNetworkError.setValue(null);
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
    public LiveData<Boolean> getShowCompositionIsLoading() {
        return showCompositionIsLoading;
    }

    @NonNull
    public LiveData<Error> getCompositionNetworkError() {
        return compositionNetworkError;
    }
}
