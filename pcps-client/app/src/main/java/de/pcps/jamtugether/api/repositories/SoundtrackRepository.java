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
import de.pcps.jamtugether.api.responses.room.DeleteTrackResponse;
import de.pcps.jamtugether.api.services.room.bodies.DeleteSoundtrackBody;
import de.pcps.jamtugether.api.services.soundtrack.SoundtrackService;
import de.pcps.jamtugether.api.services.soundtrack.bodies.UploadSoundtracksBody;
import de.pcps.jamtugether.model.Composition;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
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
    private final Context context;

    @NonNull
    private final List<SingleSoundtrack> EMPTY_SOUNDTRACK_LIST = new ArrayList<>();

    @NonNull
    private final MutableLiveData<List<SingleSoundtrack>> allSoundtracks = new MutableLiveData<>(EMPTY_SOUNDTRACK_LIST);

    @Nullable
    private CompositeSoundtrack previousCompositeSoundtrack;

    @NonNull
    private final LiveData<CompositeSoundtrack> compositeSoundtrack;

    @NonNull
    private final MutableLiveData<Boolean> isFetchingComposition = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Error> compositionNetworkError = new MutableLiveData<>(null);

    @NonNull
    private final Handler handler = new Handler();

    @Nullable
    private Runnable soundtracksRunnable;

    @NonNull
    private final BaseJamTimer countDownTimer = new JamCountDownTimer(Constants.SOUNDTRACK_FETCHING_INTERVAL, TimeUtils.ONE_SECOND, new BaseJamTimer.OnTickCallback() {
        @Override
        public void onTicked(long millis) {
            countDownTimerMillis.setValue(millis);
        }

        @Override
        public void onFinished() {
        }
    });

    @NonNull
    private final MutableLiveData<Long> countDownTimerMillis = new MutableLiveData<>(-1L);

    @Inject
    public SoundtrackRepository(@NonNull SoundtrackService soundtrackService, @NonNull RoomRepository roomRepository, @NonNull Context context) {
        this.soundtrackService = soundtrackService;
        this.roomRepository = roomRepository;
        this.context = context;
        this.compositeSoundtrack = Transformations.map(allSoundtracks, soundtracks -> {
            CompositeSoundtrack newCompositeSoundtrack = SoundtrackUtils.createCompositeSoundtrack(previousCompositeSoundtrack, soundtracks, context);
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

    public void startFetchingComposition() {
        fetchComposition();
        countDownTimer.start();

        if (soundtracksRunnable == null) {
            soundtracksRunnable = new Runnable() {

                @Override
                public void run() {
                    countDownTimer.reset();
                    fetchComposition();
                    handler.postDelayed(this, Constants.SOUNDTRACK_FETCHING_INTERVAL);
                }
            };
            soundtracksRunnable.run();
        }
    }

    public void fetchComposition() {
        isFetchingComposition.setValue(true);

        getComposition(new JamCallback<Composition>() {
            @Override
            public void onSuccess(@NonNull Composition response) {
                for (SingleSoundtrack soundtrack : response.getSoundtracks()) {
                    soundtrack.loadSounds(context);
                }
                allSoundtracks.setValue(response.getSoundtracks());
                isFetchingComposition.setValue(false);
            }

            @Override
            public void onError(@NonNull Error error) {
                isFetchingComposition.setValue(false);
                compositionNetworkError.setValue(error);
            }
        });
    }

    public void setSoundtracks(@NonNull List<SingleSoundtrack> soundtracks) {
        allSoundtracks.setValue(soundtracks);
    }

    private void onUserLeftRoom() {
        if (soundtracksRunnable != null) {
            handler.removeCallbacks(soundtracksRunnable);
            soundtracksRunnable = null;
        }
        if (!countDownTimer.isStopped()) {
            countDownTimer.stop();
        }
        allSoundtracks.setValue(EMPTY_SOUNDTRACK_LIST);
        previousCompositeSoundtrack = null;
        isFetchingComposition.setValue(false);
        compositionNetworkError.setValue(null);
        countDownTimerMillis.setValue(-1L);
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
    public LiveData<Boolean> getIsFetchingComposition() {
        return isFetchingComposition;
    }

    @NonNull
    public LiveData<Error> getCompositionNetworkError() {
        return compositionNetworkError;
    }

    @NonNull
    public LiveData<Long> getCountDownTimerMillis() {
        return countDownTimerMillis;
    }
}
