package de.pcps.jamtugether.api.repositories;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.api.Constants;
import de.pcps.jamtugether.api.JamCallback;
import de.pcps.jamtugether.api.errors.base.Error;
import de.pcps.jamtugether.api.responses.soundtrack.UploadSoundtracksResponse;
import de.pcps.jamtugether.api.responses.room.DeleteTrackResponse;
import de.pcps.jamtugether.api.services.room.bodies.DeleteSoundtrackBody;
import de.pcps.jamtugether.api.services.soundtrack.SoundtrackService;
import de.pcps.jamtugether.api.services.soundtrack.bodies.UploadSoundtracksBody;
import de.pcps.jamtugether.audio.player.composite.CompositeSoundtrackPlayer;
import de.pcps.jamtugether.model.Composition;
import de.pcps.jamtugether.model.User;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import de.pcps.jamtugether.storage.db.SoundtrackNumbersDatabase;
import retrofit2.Call;

@Singleton
public class SoundtrackRepository {

    @NonNull
    private final SoundtrackService soundtrackService;

    @NonNull
    private final RoomRepository roomRepository;

    @NonNull
    private final SoundtrackNumbersDatabase soundtrackNumbersDatabase;

    @NonNull
    private final CompositeSoundtrackPlayer compositeSoundtrackPlayer;

    @NonNull
    private final Context context;

    @NonNull
    private final Handler handler = new Handler();

    @Nullable
    private Runnable soundtrackFetchingRunnable;

    @NonNull
    private static final CompositeSoundtrack EMPTY_COMPOSITE_SOUNDTRACK = new CompositeSoundtrack(new ArrayList<>());

    @Nullable
    private List<SingleSoundtrack> previousSoundtracks;

    @NonNull
    private final MutableLiveData<List<SingleSoundtrack>> allSoundtracks = new MutableLiveData<>(new ArrayList<>());

    @Nullable
    private CompositeSoundtrack previousCompositeSoundtrack;

    @NonNull
    private final MutableLiveData<CompositeSoundtrack> compositeSoundtrack = new MutableLiveData<>(EMPTY_COMPOSITE_SOUNDTRACK);

    @NonNull
    private final MutableLiveData<Boolean> showCompositionIsLoading = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Error> compositionNetworkError = new MutableLiveData<>(null);

    private boolean loadingCompositionOfCurrentRoomShown;
    private boolean networkErrorOfCurrentRoomShown;

    @Inject
    public SoundtrackRepository(@NonNull SoundtrackService soundtrackService, @NonNull RoomRepository roomRepository, @NonNull SoundtrackNumbersDatabase soundtrackNumbersDatabase, @NonNull CompositeSoundtrackPlayer compositeSoundtrackPlayer, @NonNull Context context) {
        this.soundtrackService = soundtrackService;
        this.roomRepository = roomRepository;
        this.soundtrackNumbersDatabase = soundtrackNumbersDatabase;
        this.compositeSoundtrackPlayer = compositeSoundtrackPlayer;
        this.context = context;

        roomRepository.getUserInRoom().observeForever(userInRoom -> {
            if (!userInRoom) {
                onUserLeftRoom();
            }
        });
    }

    private void getComposition(@NonNull JamCallback<Composition> callback) {
        Integer roomID = roomRepository.getRoomID();
        String token = roomRepository.getToken().getValue();
        if (roomID == null || token == null) {
            return;
        }
        Call<Composition> call = soundtrackService.getComposition(String.format(Constants.BEARER_TOKEN_FORMAT, token), roomID);
        call.enqueue(callback);
    }

    public void uploadSoundtracks(@NonNull List<SingleSoundtrack> soundtracks, @NonNull JamCallback<UploadSoundtracksResponse> callback) {
        Integer roomID = roomRepository.getRoomID();
        String token = roomRepository.getToken().getValue();
        if (roomID == null || token == null) {
            return;
        }
        UploadSoundtracksBody body = new UploadSoundtracksBody(soundtracks);
        Call<UploadSoundtracksResponse> call = soundtrackService.uploadSoundtracks(String.format(Constants.BEARER_TOKEN_FORMAT, token), roomID, body);
        call.enqueue(callback);
    }

    public void deleteSoundtrack(@NonNull SingleSoundtrack soundtrack, @NonNull JamCallback<DeleteTrackResponse> callback) {
        Integer roomID = roomRepository.getRoomID();
        String token = roomRepository.getToken().getValue();
        if (roomID == null || token == null) {
            return;
        }
        DeleteSoundtrackBody body = new DeleteSoundtrackBody(roomID, soundtrack.getUserID(), soundtrack.getInstrument().getServerString(), soundtrack.getNumber());
        Call<DeleteTrackResponse> call = soundtrackService.deleteSoundtrack(String.format(Constants.BEARER_TOKEN_FORMAT, token), roomID, body);
        call.enqueue(callback);
    }

    public void startFetchingSoundtracks(boolean requestedFromUser) {
        fetchSoundtracks(requestedFromUser);

        if (!requestedFromUser) {
            if (soundtrackFetchingRunnable == null) {
                soundtrackFetchingRunnable = new Runnable() {

                    @Override
                    public void run() {
                        fetchSoundtracks(false);
                        handler.postDelayed(this, Constants.SOUNDTRACK_FETCHING_INTERVAL);
                    }
                };
                soundtrackFetchingRunnable.run();
            }
        }
    }

    private void fetchSoundtracks(boolean requestedFromUser) {
        if (!loadingCompositionOfCurrentRoomShown || requestedFromUser) { // loading for the first time or requested from user
            showCompositionIsLoading.setValue(true);
            loadingCompositionOfCurrentRoomShown = true;
        }

        getComposition(new JamCallback<Composition>() {
            @Override
            public void onSuccess(@NonNull Composition response) {
                showCompositionIsLoading.setValue(false);
                List<SingleSoundtrack> newSoundtracks = new ArrayList<>();
                for (SingleSoundtrack soundtrack : response.getSoundtracks()) {
                    if (soundtrack != null) {
                        soundtrack.loadSounds(context);
                        newSoundtracks.add(soundtrack);
                    }
                }
                updateAllSoundtracks(newSoundtracks);

                if (previousSoundtracks == null) {
                    previousSoundtracks = new ArrayList<>();
                }
                List<SingleSoundtrack> ownDeletedSoundtracks = getOwnDeletedSoundtracks(newSoundtracks);
                for (SingleSoundtrack soundtrack : ownDeletedSoundtracks) {
                    soundtrackNumbersDatabase.onSoundtrackDeleted(soundtrack);
                }

                previousSoundtracks.clear();
                previousSoundtracks.addAll(newSoundtracks);
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

    private List<SingleSoundtrack> getOwnDeletedSoundtracks(@NonNull List<SingleSoundtrack> newSoundtracks) {
        List<SingleSoundtrack> ownDeletedSoundtracks = new ArrayList<>();
        User user = roomRepository.getUser();
        if (user == null || previousSoundtracks == null) {
            return ownDeletedSoundtracks;
        }
        for (SingleSoundtrack soundtrack : previousSoundtracks) {
            if (!isInList(soundtrack, newSoundtracks) && soundtrack.getUserID() == user.getID()) {
                ownDeletedSoundtracks.add(soundtrack);
            }
        }
        return ownDeletedSoundtracks;
    }

    private boolean isInList(@NonNull SingleSoundtrack soundtrack, @NonNull List<SingleSoundtrack> list) {
        for (SingleSoundtrack element : list) {
            if (element.getID().equals(soundtrack.getID())) {
                return true;
            }
        }
        return false;
    }

    private void onUserLeftRoom() {
        if (soundtrackFetchingRunnable != null) {
            handler.removeCallbacks(soundtrackFetchingRunnable);
            soundtrackFetchingRunnable = null;
        }
        loadingCompositionOfCurrentRoomShown = false;
        networkErrorOfCurrentRoomShown = false;
        previousSoundtracks = null;
        previousCompositeSoundtrack = null;
    }

    public void updateAllSoundtracks(@NonNull List<SingleSoundtrack> soundtracks) {
        allSoundtracks.setValue(soundtracks);

        CompositeSoundtrack newCompositeSoundtrack = CompositeSoundtrack.from(soundtracks, context);
        if (!newCompositeSoundtrack.equals(previousCompositeSoundtrack)) {
            if (previousCompositeSoundtrack != null) {
                Soundtrack.State previousState = previousCompositeSoundtrack.getState().getValue();
                int previousProgressInMillis = previousCompositeSoundtrack.getProgressInMillis();
                float previousVolume = previousCompositeSoundtrack.getVolume();
                if (previousState != null) {
                    newCompositeSoundtrack.setState(previousState);
                }
                newCompositeSoundtrack.setProgressInMillis(previousProgressInMillis);
                newCompositeSoundtrack.setVolume(previousVolume);
            }
            compositeSoundtrack.setValue(newCompositeSoundtrack);
            previousCompositeSoundtrack = newCompositeSoundtrack;

            if (compositeSoundtrackPlayer.isPlaying()) {
                compositeSoundtrackPlayer.stop();
                compositeSoundtrackPlayer.play(newCompositeSoundtrack);
            }
        }
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
        return compositeSoundtrack;
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
