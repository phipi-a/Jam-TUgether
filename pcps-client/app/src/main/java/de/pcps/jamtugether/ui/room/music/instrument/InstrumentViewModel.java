package de.pcps.jamtugether.ui.room.music.instrument;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.api.Constants;
import de.pcps.jamtugether.api.JamCallback;
import de.pcps.jamtugether.api.errors.base.Error;
import de.pcps.jamtugether.api.repositories.RoomRepository;
import de.pcps.jamtugether.api.repositories.SoundtrackRepository;
import de.pcps.jamtugether.api.responses.soundtrack.UploadSoundtracksResponse;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.metronome.MetronomeController;
import de.pcps.jamtugether.audio.player.composite.CompositeSoundtrackPlayer;
import de.pcps.jamtugether.audio.player.single.SingleSoundtrackPlayer;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.model.User;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.storage.Preferences;
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

    @Inject
    protected Preferences preferences;

    @Inject
    protected MetronomeController metronomeController;

    @NonNull
    private final Instrument instrument;

    @NonNull
    private final OnOwnSoundtrackChangedCallback callback;

    @NonNull
    protected final MutableLiveData<Boolean> recordingSoundtrack = new MutableLiveData<>(false);

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
    private final MutableLiveData<Boolean> showUploadReminderDialog = new MutableLiveData<>(false);

    private boolean metronomeActive = true;

    @NonNull
    private final MutableLiveData<Integer> metronomeColor;

    @NonNull
    private final MutableLiveData<Boolean> compositeSoundtrackCheckBoxIsEnabled = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> uncheckCompositeSoundtrackCheckBox = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> loopCheckBoxIsEnabled = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> uncheckLoopCheckBox = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Integer> progressBarVisibility = new MutableLiveData<>(View.INVISIBLE);

    @NonNull
    private final MutableLiveData<Error> networkError = new MutableLiveData<>(null);

    @Nullable
    private List<SingleSoundtrack> previousSoundtracks;

    private boolean lastCompositeSoundtrackCheckBoxIsEnabled;
    private boolean lastLoopCheckBoxIsEnabled;

    private boolean playWithCompositeSoundtrack;
    private boolean playWithCompositeSoundtrackInLoop;

    protected long startedMillis;

    public InstrumentViewModel(@NonNull Instrument instrument, @NonNull OnOwnSoundtrackChangedCallback callback) {
        AppInjector.inject(this);
        this.instrument = instrument;
        this.callback = callback;
        this.metronomeColor = new MutableLiveData<>(ContextCompat.getColor(application.getApplicationContext(), R.color.metronomeActiveColor));
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
        soundtrackRepository.getCompositeSoundtrack().observe(lifecycleOwner, compositeSoundtrack -> {
            this.compositeSoundtrack = compositeSoundtrack;
            // if composite soundtrack is not empty anymore while soundtrack is being recorded
            // update checkbox after user is done recording
            if (timer.isRunning()) {
                lastCompositeSoundtrackCheckBoxIsEnabled = !compositeSoundtrack.isEmpty();
            } else {
                compositeSoundtrackCheckBoxIsEnabled.setValue(!compositeSoundtrack.isEmpty());
            }
            if (compositeSoundtrack.isEmpty()) {
                uncheckCompositeSoundtrackCheckBox.setValue(true);
                uncheckLoopCheckBox.setValue(true);
                loopCheckBoxIsEnabled.setValue(false);
            }
        });
    }

    public void onMetronomeButtonClicked() {
        if (timer.isRunning()) { // soundtrack is being recorded
            return;
        }
        metronomeActive = !metronomeActive;
        Context context = application.getApplicationContext();
        if (metronomeActive) {
            metronomeColor.setValue(ContextCompat.getColor(context, R.color.metronomeActiveColor));
        } else {
            metronomeColor.setValue(ContextCompat.getColor(context, R.color.metronomeInactiveColor));
        }
    }

    public void onPlayWithCompositeSoundtrackClicked(boolean checked) {
        this.playWithCompositeSoundtrack = checked;
        loopCheckBoxIsEnabled.setValue(checked);
        if (!checked) {
            uncheckLoopCheckBox.setValue(true);
        }
    }

    public void onPlayWithCompositeSoundtrackInLoopClicked(boolean checked) {
        this.playWithCompositeSoundtrackInLoop = checked;
    }

    public void onLoopCheckBoxUnchecked() {
        uncheckLoopCheckBox.setValue(false);
    }

    public void onCompositeSoundtrackCheckBoxUnchecked() {
        uncheckCompositeSoundtrackCheckBox.setValue(false);
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
            startRecordingSoundtrack();
        }
    });

    private void repeatCompositeSoundtrack() {
        if (playWithCompositeSoundtrackInLoop) {
            onRecordSoundtrackButtonClicked();
            if (ownSoundtrack != null && !ownSoundtrack.isEmpty()) {
                onUploadButtonClicked();
            }
            onRecordSoundtrackButtonClicked();
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

    public void onRecordSoundtrackButtonClicked() {
        if (recordingSoundtrack()) { // stop button clicked
            if (countDownTimer.isStopped()) {
                finishRecordingSoundtrack();
            } else { // stop button was clicked before count down timer finished
                countDownTimer.stop();
                countDownTimerMillis.setValue(-1L);
                recordingSoundtrack.setValue(false);
            }
        } else { // record button clicked
            timerMillis.setValue(-1L);

            int soundtrackNumber = soundtrackNumbersDatabase.getUnusedNumberFor(instrument);

            User user = roomRepository.getUser();
            if (user == null) {
                return;
            }

            // set userID to -1 so this soundtrack isn't linked to published soundtrack of this user
            ownSoundtrack = new SingleSoundtrack(-1, user.getName(), instrument, soundtrackNumber);
            ownSoundtrack.loadSounds(application.getApplicationContext());

            recordingSoundtrack.setValue(true);
            countDownTimer.start();
        }
    }

    protected void startRecordingSoundtrack() {
        if (playWithCompositeSoundtrack) {
            if (compositeSoundtrack != null) {
                compositeSoundtrackPlayer.stop(compositeSoundtrack);
                compositeSoundtrackPlayer.play(compositeSoundtrack);
                if (playWithCompositeSoundtrackInLoop) {
                    compositeSoundtrackPlayer.setOnSoundtrackFinishedCallback((soundtrackPlayingThread) -> {
                        Handler mainThreadHandler = new Handler(application.getApplicationContext().getMainLooper());
                        mainThreadHandler.post(this::repeatCompositeSoundtrack);
                    });
                } else {
                    compositeSoundtrackPlayer.setOnSoundtrackFinishedCallback(null);
                }
            }
        }
        if (metronomeActive) {
            metronomeController.onStartedRecordingSoundtrack();
        }

        lastCompositeSoundtrackCheckBoxIsEnabled = loopCheckBoxIsEnabled.getValue();
        lastLoopCheckBoxIsEnabled = loopCheckBoxIsEnabled.getValue();
        compositeSoundtrackCheckBoxIsEnabled.setValue(false);
        loopCheckBoxIsEnabled.setValue(false);
    }

    protected void finishRecordingSoundtrack() {
        finishSoundtrack();
        timer.stop();
        compositeSoundtrackPlayer.setOnSoundtrackFinishedCallback(null);
        if (!preferences.userSawUploadReminderDialog()) {
            showUploadReminderDialog.setValue(true);
        }
        if (metronomeActive) {
            metronomeController.onFinishedRecordingSoundtrack();
        }
        if (lastLoopCheckBoxIsEnabled) {
            loopCheckBoxIsEnabled.setValue(true);
        }
        if (lastCompositeSoundtrackCheckBoxIsEnabled) {
            compositeSoundtrackCheckBoxIsEnabled.setValue(true);
        }
    }

    public void onUploadDialogShown() {
        preferences.setUserSawUploadReminderDialog(true);
        showUploadReminderDialog.setValue(false);
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
        recordingSoundtrack.setValue(false);
    }

    protected boolean recordingSoundtrack() {
        Boolean started = recordingSoundtrack.getValue();
        return started != null && started;
    }

    public void onNetworkErrorShown() {
        networkError.setValue(null);
    }

    @Override
    protected void onCleared() {
        if (recordingSoundtrack()) {
            finishSoundtrack();
        }
    }

    @NonNull
    public LiveData<Boolean> getRecordingSoundtrack() {
        return recordingSoundtrack;
    }

    @NonNull
    public LiveData<String> getTimerText() {
        return Transformations.map(timerMillis, millis -> {
            if (millis == -1L) {
                return "";
            }
            return TimeUtils.formatToMinutesSeconds(millis);
        });
    }

    @NonNull
    public LiveData<String> getCountDownTimerText() {
        return Transformations.map(countDownTimerMillis, millis -> {
            if (millis == -1L) {
                return "";
            }
            return TimeUtils.formatToSeconds(millis);
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
    public LiveData<Boolean> getShowUploadReminderDialog() {
        return showUploadReminderDialog;
    }

    @NonNull
    public LiveData<Integer> getMetronomeColor() {
        return metronomeColor;
    }

    @NonNull
    public LiveData<Boolean> getCompositeSoundtrackCheckBoxIsEnabled() {
        return compositeSoundtrackCheckBoxIsEnabled;
    }

    @NonNull
    public LiveData<Boolean> getUncheckCompositeSoundtrackCheckBox() {
        return uncheckCompositeSoundtrackCheckBox;
    }

    @NonNull
    public LiveData<Boolean> getLoopCheckBoxIsEnabled() {
        return loopCheckBoxIsEnabled;
    }

    @NonNull
    public LiveData<Boolean> getUncheckLoopCheckBox() {
        return uncheckLoopCheckBox;
    }

    @NonNull
    public LiveData<Integer> getProgressBarVisibility() {
        return progressBarVisibility;
    }

    @NonNull
    public LiveData<Error> getNetworkError() {
        return networkError;
    }
}
