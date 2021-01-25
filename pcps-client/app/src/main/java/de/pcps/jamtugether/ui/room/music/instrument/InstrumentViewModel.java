package de.pcps.jamtugether.ui.room.music.instrument;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import de.pcps.jamtugether.api.Constants;
import de.pcps.jamtugether.api.JamCallback;
import de.pcps.jamtugether.api.errors.base.Error;
import de.pcps.jamtugether.api.repositories.RoomRepository;
import de.pcps.jamtugether.api.repositories.SoundtrackRepository;
import de.pcps.jamtugether.api.responses.soundtrack.UploadSoundtracksResponse;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.player.composite.CompositeSoundtrackPlayer;
import de.pcps.jamtugether.audio.player.composite.RepeatSoundtrackCallback;
import de.pcps.jamtugether.audio.player.single.SingleSoundtrackPlayer;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.model.User;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import de.pcps.jamtugether.storage.db.LatestSoundtracksDatabase;
import de.pcps.jamtugether.storage.db.SoundtrackNumbersDatabase;
import de.pcps.jamtugether.timer.JamCountDownTimer;
import de.pcps.jamtugether.timer.JamTimer;
import de.pcps.jamtugether.timer.base.BaseJamTimer;
import de.pcps.jamtugether.ui.room.music.OnOwnSoundtrackChangedCallback;
import de.pcps.jamtugether.utils.SoundtrackUtils;
import de.pcps.jamtugether.utils.TimeUtils;

public abstract class InstrumentViewModel extends ViewModel {

    @Inject
    protected Application application;

    @Inject
    protected RoomRepository roomRepository;

    @Inject
    protected SoundtrackRepository soundtrackRepository;

    @Inject
    protected CompositeSoundtrackPlayer compositeSoundtrackPlayer;

    @Inject
    protected SingleSoundtrackPlayer singleSoundtrackPlayer;

    @Inject
    protected SoundtrackNumbersDatabase soundtrackNumbersDatabase;

    @Inject
    protected LatestSoundtracksDatabase latestSoundtracksDatabase;

    @NonNull
    private final Instrument instrument;

    @NonNull
    private final OnOwnSoundtrackChangedCallback callback;

    @NonNull
    protected final MutableLiveData<Boolean> startedSoundtrackCreation = new MutableLiveData<>(false);

    @NonNull
    protected final MutableLiveData<Long> countDownTimerMillis = new MutableLiveData<>(-1L);

    @NonNull
    protected final MutableLiveData<Long> timerMillis = new MutableLiveData<>(-1L);

    @Nullable
    private CompositeSoundtrack compositeSoundtrack;

    @Nullable
    protected SingleSoundtrack ownSoundtrack;

    @NonNull
    private final MutableLiveData<Boolean> uploadButtonEnabled = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Integer> uploadButtonVisibility;

    @NonNull
    private final MutableLiveData<Integer> progressBarVisibility = new MutableLiveData<>(View.INVISIBLE);

    @NonNull
    private final MutableLiveData<Error> networkError = new MutableLiveData<>(null);

    @Nullable
    private List<SingleSoundtrack> previousSoundtracks;

    private boolean playWithCompositeSoundtrack;

    private boolean playWithCompositeSoundtrackInLoop;

    protected long startedMillis;

    @NonNull
    private final MutableLiveData<Boolean> repeatCompositeSoundtrack = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> loopCheckboxIsEnabled = new MutableLiveData<>(false);


    public InstrumentViewModel(@NonNull Instrument instrument, @NonNull OnOwnSoundtrackChangedCallback callback) {
        AppInjector.inject(this);
        this.instrument = instrument;
        this.callback = callback;
        ownSoundtrack = latestSoundtracksDatabase.getLatestSoundtrack(instrument);
        if (ownSoundtrack != null) {
            callback.onOwnSoundtrackChanged(ownSoundtrack);
            uploadButtonVisibility = new MutableLiveData<>(View.VISIBLE);
        } else {
            uploadButtonVisibility = new MutableLiveData<>(View.GONE);
        }
    }

    public void observeAllSoundtracks(@NonNull LifecycleOwner lifecycleOwner) {
        soundtrackRepository.getAllSoundtracks().observe(lifecycleOwner, allSoundtracks -> {
            User user = roomRepository.getUser();

            if (user != null && previousSoundtracks != null && ownSoundtrack != null) {
                for (SingleSoundtrack deletedSoundtrack : SoundtrackUtils.getOwnDeletedSoundtracks(user, previousSoundtracks, allSoundtracks)) {
                    // check if deleted soundtrack is own soundtrack
                    if (deletedSoundtrack.getUserID() == user.getID() && deletedSoundtrack.getInstrument() == ownSoundtrack.getInstrument() && deletedSoundtrack.getNumber() == ownSoundtrack.getNumber()) {
                        uploadButtonEnabled.setValue(true);
                    }
                }
            }
            previousSoundtracks = allSoundtracks;
        });
    }

    public void observeCompositeSoundtrack(@NonNull LifecycleOwner lifecycleOwner) {
        soundtrackRepository.getCompositeSoundtrack().observe(lifecycleOwner, compositeSoundtrack -> this.compositeSoundtrack = compositeSoundtrack);
    }

    public void onPlayWithCompositeSoundtrackClicked(boolean checked) {
        this.playWithCompositeSoundtrack = checked;
        loopCheckboxIsEnabled.setValue(checked);
    }

    public void onPlayWithCompositeSoundtrackInLoopClicked(boolean checked) {
        this.playWithCompositeSoundtrackInLoop = checked;
    }

    @NonNull
    protected final BaseJamTimer countDownTimer = new JamCountDownTimer(Constants.SOUNDTRACK_RECORDING_COUNT_DOWN, TimeUtils.ONE_SECOND, new BaseJamTimer.OnTickCallback() {
        @Override
        public void onTicked(long millis) {
            countDownTimerMillis.setValue(millis);
        }

        @Override
        public void onFinished() {
            countDownTimer.stop();
            countDownTimerMillis.setValue(-1L);
            startedMillis = System.currentTimeMillis();
            timer.start();
            if (playWithCompositeSoundtrack) {
                if (compositeSoundtrack != null) {
                    compositeSoundtrackPlayer.stop(compositeSoundtrack);
                    compositeSoundtrackPlayer.play(compositeSoundtrack);
                    if (playWithCompositeSoundtrackInLoop) {
                        compositeSoundtrackPlayer.setRepeatCallback(() -> repeatCompositeSoundtrack.postValue(true));
                    } else {
                        compositeSoundtrackPlayer.setRepeatCallback(null);
                    }
                }
            }
            onTimerStarted();
        }
    });


    public void onRepeatCompositeSoundtrack() {
        repeatCompositeSoundtrack.setValue(false);
    }

    void repeatCompositeSoundtrack() {
        if (playWithCompositeSoundtrackInLoop) {
            onCreateSoundtrackButtonClicked();
            if (ownSoundtrack != null && !ownSoundtrack.isEmpty()) {
                onUploadButtonClicked();
            }
            onCreateSoundtrackButtonClicked();
        }
    }


    @NonNull
    protected final BaseJamTimer timer = new JamTimer(Soundtrack.MAX_TIME, TimeUtils.ONE_SECOND, new BaseJamTimer.OnTickCallback() {
        @Override
        public void onTicked(long millis) {
            timerMillis.setValue(millis);
        }

        @Override
        public void onFinished() {
            finishSoundtrack();
        }
    });

    public void onCreateSoundtrackButtonClicked() {
        if (startedSoundtrackCreation()) {
            if (countDownTimer.isStopped()) {
                finishSoundtrack();
                timer.stop();
            } else {
                countDownTimer.stop();
                countDownTimerMillis.setValue(-1L);
                startedSoundtrackCreation.setValue(false);
            }
            compositeSoundtrackPlayer.setRepeatCallback(null);
        } else {
            timerMillis.setValue(-1L);

            int soundtrackNumber = soundtrackNumbersDatabase.getUnusedNumberFor(instrument);

            User user = roomRepository.getUser();
            if (user == null) {
                return;
            }

            // set userID to -1 so this soundtrack isn't linked to published soundtrack of this user
            ownSoundtrack = new SingleSoundtrack(-1, user.getName(), instrument, soundtrackNumber);
            ownSoundtrack.loadSounds(application.getApplicationContext());
            startedSoundtrackCreation.setValue(true);
            countDownTimer.start();
        }
    }

    protected void onTimerStarted() {
    }

    public void onUploadButtonClicked() {
        uploadTrack(true);
    }

    public void uploadTrack(boolean manualUpload) {
        User user = roomRepository.getUser();
        if (ownSoundtrack == null || user == null) {
            return;
        }

        SingleSoundtrack toBePublished = new SingleSoundtrack(user.getID(), user.getName(), instrument, ownSoundtrack.getNumber(), ownSoundtrack.getSoundSequence());
        if (manualUpload) {
            progressBarVisibility.setValue(View.VISIBLE);
        }
        uploadButtonEnabled.setValue(false);

        List<SingleSoundtrack> soundtracks = Collections.singletonList(toBePublished);
        soundtrackRepository.uploadSoundtracks(soundtracks, new JamCallback<UploadSoundtracksResponse>() {
            @Override
            public void onSuccess(@NonNull UploadSoundtracksResponse response) {
                if (manualUpload) {
                    progressBarVisibility.setValue(View.INVISIBLE);
                }
                soundtrackNumbersDatabase.onSoundtrackCreated(toBePublished);

                // add to local list in order to be visible immediately
                if (soundtrackRepository.getAllSoundtracks().getValue() != null) {
                    List<SingleSoundtrack> allSoundtracks = new ArrayList<>(soundtrackRepository.getAllSoundtracks().getValue());
                    allSoundtracks.add(toBePublished);
                    soundtrackRepository.setSoundtracks(allSoundtracks);
                }
            }

            @Override
            public void onError(@NonNull Error error) {
                if (manualUpload) {
                    progressBarVisibility.setValue(View.INVISIBLE);
                }
                uploadButtonEnabled.setValue(true);
                networkError.setValue(error);
            }
        });
    }

    protected void finishSoundtrack() {
        if (ownSoundtrack != null && !ownSoundtrack.isEmpty()) {
            singleSoundtrackPlayer.stop(ownSoundtrack);
            if (playWithCompositeSoundtrack && playWithCompositeSoundtrackInLoop && compositeSoundtrack != null) {
                ownSoundtrack.removeEnd(compositeSoundtrack.getLength());
            }
            callback.onOwnSoundtrackChanged(ownSoundtrack);
            latestSoundtracksDatabase.onOwnSoundtrackUpdated(ownSoundtrack);
            uploadButtonEnabled.setValue(true);
            uploadButtonVisibility.setValue(View.VISIBLE);
        }
        startedSoundtrackCreation.setValue(false);
    }

    protected boolean startedSoundtrackCreation() {
        Boolean started = startedSoundtrackCreation.getValue();
        return started != null && started;
    }

    public void onNetworkErrorShown() {
        networkError.setValue(null);
    }

    @Override
    protected void onCleared() {
        if (startedSoundtrackCreation()) {
            finishSoundtrack();
        }
    }

    @NonNull
    public LiveData<Boolean> getStartedSoundtrackCreation() {
        return startedSoundtrackCreation;
    }

    @NonNull
    public LiveData<String> getTimerText() {
        return Transformations.map(timerMillis, millis -> {
            if (millis == -1L) {
                return "";
            }
            return TimeUtils.formatTimerSecondMinutes(millis);
        });
    }

    @NonNull
    public LiveData<String> getCountDownTimerText() {
        return Transformations.map(countDownTimerMillis, millis -> {
            if (millis == -1L) {
                return "";
            }
            return TimeUtils.formatTimerSecondsSimple(millis);
        });
    }

    @NonNull
    public LiveData<Boolean> getUploadButtonEnabled() {
        return uploadButtonEnabled;
    }

    @NonNull
    public LiveData<Integer> getUploadButtonVisibility() {
        return uploadButtonVisibility;
    }

    @NonNull
    public LiveData<Integer> getProgressBarVisibility() {
        return progressBarVisibility;
    }

    @NonNull
    public LiveData<Error> getNetworkError() {
        return networkError;
    }

    @NonNull
    public LiveData<Boolean> getLoopCheckboxIsEnabled() {
        return loopCheckboxIsEnabled;
    }

    @NonNull
    public LiveData<Boolean> getRepeatCompositeSoundtrack() {
        return repeatCompositeSoundtrack;
    }

}
