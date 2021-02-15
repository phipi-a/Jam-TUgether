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
import de.pcps.jamtugether.api.errors.ForbiddenAccessError;
import de.pcps.jamtugether.api.errors.RoomDeletedError;
import de.pcps.jamtugether.api.errors.base.Error;
import de.pcps.jamtugether.api.requests.soundtrack.UploadSoundtracksResponse;
import de.pcps.jamtugether.api.requests.room.soundtrack.DeleteSoundtrackResponse;
import de.pcps.jamtugether.api.requests.room.soundtrack.DeleteSoundtrackBody;
import de.pcps.jamtugether.api.requests.soundtrack.SoundtrackService;
import de.pcps.jamtugether.api.requests.soundtrack.UploadSoundtracksBody;
import de.pcps.jamtugether.model.Composition;
import de.pcps.jamtugether.model.Beat;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.storage.db.SoundtrackVolumesDatabase;
import de.pcps.jamtugether.timer.JamCountDownTimer;
import de.pcps.jamtugether.timer.base.BaseJamTimer;
import de.pcps.jamtugether.utils.SoundtrackUtils;
import de.pcps.jamtugether.utils.TimeUtils;
import retrofit2.Call;

@Singleton
public class SoundtrackRepository {

    @NonNull
    private final SoundtrackService soundtrackService;

    @NonNull
    private final RoomRepository roomRepository;

    @NonNull
    private final SoundtrackVolumesDatabase soundtrackVolumesDatabase;

    @NonNull
    private final Context context;

    @NonNull
    private final List<SingleSoundtrack> EMPTY_SOUNDTRACK_LIST = new ArrayList<>();

    @NonNull
    private final MutableLiveData<Beat> beat = new MutableLiveData<>(Beat.DEFAULT);

    @NonNull
    private final MutableLiveData<List<SingleSoundtrack>> allSoundtracks = new MutableLiveData<>(EMPTY_SOUNDTRACK_LIST);

    @Nullable
    private CompositeSoundtrack previousCompositeSoundtrack;

    @NonNull
    private final LiveData<CompositeSoundtrack> compositeSoundtrack;

    @NonNull
    private final MutableLiveData<Error> showNetworkError = new MutableLiveData<>(null);

    @NonNull
    private final Handler handler = new Handler();

    @Nullable
    private Runnable soundtracksRunnable;

    @Inject
    public SoundtrackRepository(@NonNull SoundtrackService soundtrackService, @NonNull RoomRepository roomRepository, @NonNull SoundtrackVolumesDatabase soundtrackVolumesDatabase, @NonNull Context context) {
        this.soundtrackService = soundtrackService;
        this.roomRepository = roomRepository;
        this.soundtrackVolumesDatabase = soundtrackVolumesDatabase;
        this.context = context;
        this.compositeSoundtrack = Transformations.map(allSoundtracks, soundtracks -> {
            CompositeSoundtrack newCompositeSoundtrack = SoundtrackUtils.createCompositeSoundtrack(previousCompositeSoundtrack, soundtracks, context);
            float volume = soundtrackVolumesDatabase.getCompositeSoundtrackVolume();
            newCompositeSoundtrack.setVolume(volume);
            previousCompositeSoundtrack = newCompositeSoundtrack;
            return newCompositeSoundtrack;
        });

        roomRepository.getUserInRoom().observeForever(userInRoom -> {
            if (!userInRoom) {
                onUserLeftRoom();
            }
        });
    }

    public void getComposition(int roomID, @NonNull String token, JamCallback<Composition> callback) {
        Call<Composition> call = soundtrackService.getComposition(String.format(Constants.BEARER_TOKEN_FORMAT, token), roomID);
        call.enqueue(callback);
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

    public void deleteSoundtrack(@NonNull SingleSoundtrack soundtrack, @NonNull JamCallback<DeleteSoundtrackResponse> callback) {
        Integer roomID = roomRepository.getRoomID();
        String token = roomRepository.getToken().getValue();
        if (roomID == null || token == null) {
            return;
        }
        DeleteSoundtrackBody body = new DeleteSoundtrackBody(roomID, soundtrack.getUserID(), soundtrack.getInstrument().getServerString(), soundtrack.getNumber());
        Call<DeleteSoundtrackResponse> call = soundtrackService.deleteSoundtrack(String.format(Constants.BEARER_TOKEN_FORMAT, token), roomID, body);
        call.enqueue(callback);
    }

    public void startFetchingComposition() {
        fetchComposition();
        if (soundtracksRunnable == null) {
            soundtracksRunnable = new Runnable() {

                @Override
                public void run() {
                    fetchComposition();
                    handler.postDelayed(this, Constants.SOUNDTRACK_FETCHING_INTERVAL);
                }
            };
            soundtracksRunnable.run();
        }
    }

    public void fetchComposition() {
        getComposition(new JamCallback<Composition>() {
            @Override
            public void onSuccess(@NonNull Composition response) {
                for (SingleSoundtrack soundtrack : response.getSoundtracks()) {
                    soundtrack.loadSounds(context);
                }
                beat.setValue(response.getBeat());
                List<SingleSoundtrack> previousSoundtracks = allSoundtracks.getValue();
                if (!previousSoundtracks.equals(response.getSoundtracks())) {
                    setSoundtracks(response.getSoundtracks());
                }
            }

            @Override
            public void onError(@NonNull Error error) {
                if (!(error instanceof RoomDeletedError) && !(error instanceof ForbiddenAccessError)) {
                    showNetworkError.setValue(error);
                }
            }
        });
    }

    public void setSoundtracks(@NonNull List<SingleSoundtrack> soundtracks) {
        for (SingleSoundtrack soundtrack : soundtracks) {
            float volume = soundtrackVolumesDatabase.getVolumeOf(soundtrack);
            soundtrack.setVolume(volume);
        }
        allSoundtracks.setValue(soundtracks);
    }

    public void setBeat(@NonNull Beat beat) {
        this.beat.setValue(beat);
    }

    private void onUserLeftRoom() {
        if (soundtracksRunnable != null) {
            handler.removeCallbacks(soundtracksRunnable);
            soundtracksRunnable = null;
        }
        allSoundtracks.setValue(EMPTY_SOUNDTRACK_LIST);
        previousCompositeSoundtrack = null;
        showNetworkError.setValue(null);
    }

    public void onNetworkErrorShown() {
        showNetworkError.setValue(null);
    }

    @NonNull
    public LiveData<Beat> getBeat() {
        return beat;
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
    public LiveData<Error> getShowNetworkError() {
        return showNetworkError;
    }

    @NonNull
    public LiveData<Boolean> getRoomDeleted() {
        return Transformations.map(getShowNetworkError(), networkError -> (networkError instanceof RoomDeletedError));
    }

    @NonNull
    public LiveData<Boolean> getTokenExpired() {
        return Transformations.map(getShowNetworkError(), networkError -> (networkError instanceof ForbiddenAccessError));
    }
}
