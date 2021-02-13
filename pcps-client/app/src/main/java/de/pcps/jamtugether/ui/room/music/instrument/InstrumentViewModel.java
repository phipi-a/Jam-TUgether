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
import de.pcps.jamtugether.api.requests.soundtrack.UploadSoundtracksResponse;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.metronome.MetronomeController;
import de.pcps.jamtugether.audio.metronome.MetronomePlayer;
import de.pcps.jamtugether.audio.metronome.MetronomePlayingThread;
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
    RoomRepository roomRepository;

    @Inject
    SoundtrackRepository soundtrackRepository;

    @Inject
    CompositeSoundtrackPlayer compositeSoundtrackPlayer;

    @Inject
    SingleSoundtrackPlayer singleSoundtrackPlayer;

    @Inject
    SoundtrackNumbersDatabase soundtrackNumbersDatabase;

    @Inject
    LatestSoundtracksDatabase latestSoundtracksDatabase;

    @Inject
    Preferences preferences;

    @Inject
    MetronomeController metronomeController;

    @Inject
    MetronomePlayer metronomePlayer;

    @NonNull
    private final Instrument instrument;

    @NonNull
    private final OnOwnSoundtrackChangedCallback callback;

    @Nullable
    private CompositeSoundtrack compositeSoundtrack;

    @Nullable
    protected SingleSoundtrack ownSoundtrack;

    @Nullable
    private List<SingleSoundtrack> previousSoundtracks;

    @NonNull
    private final MutableLiveData<Boolean> uploadButtonIsEnabled = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Integer> uploadButtonVisibility;

    @NonNull
    private final MutableLiveData<Integer> recordButtonImage = new MutableLiveData<>(R.drawable.ic_record);

    @NonNull
    private final MutableLiveData<Long> countDownTimerMillis = new MutableLiveData<>(-1L);

    @NonNull
    private final MutableLiveData<Long> timerMillis = new MutableLiveData<>(-1L);

    @NonNull
    private final MutableLiveData<Boolean> showUploadReminderDialog = new MutableLiveData<>(false);

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
    private final MutableLiveData<Error> showNetworkError = new MutableLiveData<>(null);

    @Nullable
    private JamCountDownTimer countDownTimer;

    @Nullable
    private JamTimer timer;

    protected boolean recordingSoundtrack;

    protected long startedMillis;

    /**
     * number of tacts that started after
     * soundtrack is finished
     */
    private int newTactCount;

    private boolean lastCompositeSoundtrackCheckBoxIsEnabled;
    private boolean lastLoopCheckBoxIsEnabled;

    private boolean playWithCompositeSoundtrack;
    private boolean playWithCompositeSoundtrackInLoop;

    public InstrumentViewModel(@NonNull Instrument instrument, @NonNull OnOwnSoundtrackChangedCallback callback) {
        AppInjector.inject(this);
        this.instrument = instrument;
        this.callback = callback;
        this.metronomeColor = new MutableLiveData<>(ContextCompat.getColor(application.getApplicationContext(), R.color.metronomeActiveColor));
        ownSoundtrack = latestSoundtracksDatabase.getLatestSoundtrack(instrument);
        if (ownSoundtrack != null) {
            callback.onOwnSoundtrackChanged(ownSoundtrack);
            uploadButtonVisibility = new MutableLiveData<>(View.VISIBLE);
            boolean ownSoundtrackUploaded = latestSoundtracksDatabase.latestSoundtrackWasUploaded(instrument);
            uploadButtonIsEnabled.setValue(!ownSoundtrackUploaded);
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
                        uploadButtonIsEnabled.setValue(true);
                        latestSoundtracksDatabase.onLatestSoundtrackDeleted(instrument);
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
            if (recordingSoundtrack) {
                lastCompositeSoundtrackCheckBoxIsEnabled = !compositeSoundtrack.isEmpty();
                lastLoopCheckBoxIsEnabled = !compositeSoundtrack.isEmpty();
            } else {
                compositeSoundtrackCheckBoxIsEnabled.setValue(!compositeSoundtrack.isEmpty());
                loopCheckBoxIsEnabled.setValue(!compositeSoundtrack.isEmpty());
            }
            if (compositeSoundtrack.isEmpty()) {
                uncheckCompositeSoundtrackCheckBox.setValue(true);
                uncheckLoopCheckBox.setValue(true);
            }
        });
    }

    public void onMetronomeButtonClicked() {
        metronomeController.onMetronomeButtonClicked();
        Context context = application.getApplicationContext();
        if (metronomePlayer.isActive()) {
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

    public void onRecordSoundtrackButtonClicked() {
        if (recordButtonImage.getValue() != null && recordButtonImage.getValue() == R.drawable.ic_stop) {
            if (recordingSoundtrack) {
                finishRecordingSoundtrack(false);
            } else { // stop button was clicked before count down timer finished
                if (countDownTimer != null) {
                    countDownTimer.stop();
                }
                countDownTimerMillis.setValue(-1L);
            }
            recordButtonImage.setValue(R.drawable.ic_record);
        } else { // record button clicked
            timerMillis.setValue(-1L);
            recordButtonImage.setValue(R.drawable.ic_stop);
            countDownTimer = createCountDownTimer();
            countDownTimer.start();
        }
    }

    protected void startRecordingSoundtrack(boolean loop) {
        User user = roomRepository.getUser();
        if (user == null) {
            return;
        }

        int soundtrackNumber = soundtrackNumbersDatabase.getUnusedNumberFor(instrument);
        // set userID to -1 so this soundtrack isn't linked to published soundtrack of this user
        ownSoundtrack = new SingleSoundtrack(-1, user.getName(), instrument, soundtrackNumber);
        ownSoundtrack.loadSounds(application.getApplicationContext());

        if (playWithCompositeSoundtrack && compositeSoundtrack != null) {
            compositeSoundtrackPlayer.stop(compositeSoundtrack);
            compositeSoundtrackPlayer.play(compositeSoundtrack);
        }
        if (playWithCompositeSoundtrackInLoop) {
            compositeSoundtrackPlayer.setOnSoundtrackFinishedCallback((soundtrackPlayingThread) -> {
                Handler mainThreadHandler = new Handler(application.getApplicationContext().getMainLooper());
                mainThreadHandler.post(this::repeatCompositeSoundtrack);
            });
        } else {
            compositeSoundtrackPlayer.setOnSoundtrackFinishedCallback(null);
        }

        Boolean compositeSoundtrackCheckBoxIsEnabled = this.compositeSoundtrackCheckBoxIsEnabled.getValue();
        Boolean loopCheckBoxIsEnabled = this.loopCheckBoxIsEnabled.getValue();
        lastCompositeSoundtrackCheckBoxIsEnabled = compositeSoundtrackCheckBoxIsEnabled != null && compositeSoundtrackCheckBoxIsEnabled;
        lastLoopCheckBoxIsEnabled = loopCheckBoxIsEnabled != null && loopCheckBoxIsEnabled;
        this.compositeSoundtrackCheckBoxIsEnabled.setValue(false);
        this.loopCheckBoxIsEnabled.setValue(false);

        if (!loop) {
            metronomeController.onStartedRecordingSoundtrack();
            this.uploadButtonIsEnabled.setValue(false);
        }

        startedMillis = System.currentTimeMillis();
        recordingSoundtrack = true;
        timer = createTimer();
        timer.start();
    }

    protected void finishRecordingSoundtrack(boolean loop) {
        if (!loop) {
            recordingSoundtrack = false;
        }

        if (timer != null) {
            timer.stop();
        }

        compositeSoundtrackPlayer.setOnSoundtrackFinishedCallback(null);
        if (playWithCompositeSoundtrack && compositeSoundtrack != null) {
            compositeSoundtrackPlayer.stop(compositeSoundtrack);
        }

        if (!loop) {
            if (lastLoopCheckBoxIsEnabled) {
                loopCheckBoxIsEnabled.setValue(true);
            }
            if (lastCompositeSoundtrackCheckBoxIsEnabled) {
                compositeSoundtrackCheckBoxIsEnabled.setValue(true);
            }

            metronomeController.onFinishedRecordingSoundtrack();

            if (!preferences.userSawUploadReminderDialog()) {
                showUploadReminderDialog.setValue(true);
            }
        }

        if (ownSoundtrack != null && !ownSoundtrack.isEmpty()) {
            singleSoundtrackPlayer.stop(ownSoundtrack);
            callback.onOwnSoundtrackChanged(ownSoundtrack);
            latestSoundtracksDatabase.onLatestSoundtrackChanged(ownSoundtrack);
            if (!loop) {
                uploadButtonIsEnabled.setValue(true);
            }
            uploadButtonVisibility.setValue(View.VISIBLE);
        } else {
            ownSoundtrack = latestSoundtracksDatabase.getLatestSoundtrack(instrument);
            uploadButtonIsEnabled.setValue(!latestSoundtracksDatabase.latestSoundtrackWasUploaded(instrument));
        }
    }

    private void repeatCompositeSoundtrack() {
        finishRecordingSoundtrack(true);
        uploadTrack(true);
        metronomePlayer.setOnTickCallback(new MetronomePlayingThread.OnTickCallback() {
            @Override
            public void onNewTactTick(long millis) {
                newTactCount++;
                if (newTactCount == 2) { // to ensure that there's a pause of one complete tact
                    Handler mainThreadHandler = new Handler(application.getApplicationContext().getMainLooper());
                    mainThreadHandler.post(() -> {
                        newTactCount = 0;
                        metronomePlayer.setOnTickCallback(null);
                        startRecordingSoundtrack(true);
                    });
                }
            }

            @Override
            public void onTick(long millis) {
            }
        });
    }

    public void onUploadButtonClicked() {
        uploadTrack(false);
    }

    private void uploadTrack(boolean loop) {
        User user = roomRepository.getUser();
        if  (ownSoundtrack == null || ownSoundtrack.isEmpty() || user == null) {
            return;
        }
        SingleSoundtrack toBePublished = new SingleSoundtrack(user.getID(), user.getName(), instrument, ownSoundtrack.getNumber(), ownSoundtrack.getSoundSequence());

        if (!loop) {
            progressBarVisibility.setValue(View.VISIBLE);
        }

        uploadButtonIsEnabled.setValue(false);

        List<SingleSoundtrack> soundtracks = Collections.singletonList(toBePublished);
        soundtrackRepository.uploadSoundtracks(soundtracks, new JamCallback<UploadSoundtracksResponse>() {
            @Override
            public void onSuccess(@NonNull UploadSoundtracksResponse response) {
                if (!loop) {
                    progressBarVisibility.setValue(View.INVISIBLE);
                }
                soundtrackNumbersDatabase.onSoundtrackCreated(toBePublished);
                latestSoundtracksDatabase.onLatestSoundtrackUploaded(instrument);

                // add to local list in order to be visible immediately
                if (soundtrackRepository.getAllSoundtracks().getValue() != null) {
                    List<SingleSoundtrack> allSoundtracks = new ArrayList<>(soundtrackRepository.getAllSoundtracks().getValue());
                    allSoundtracks.add(toBePublished);
                    for (SingleSoundtrack soundtrack : allSoundtracks) {
                        soundtrack.loadSounds(application.getApplicationContext());
                    }
                    soundtrackRepository.setSoundtracks(allSoundtracks);
                }
            }

            @Override
            public void onError(@NonNull Error error) {
                if (!loop) {
                    progressBarVisibility.setValue(View.INVISIBLE);
                }
                uploadButtonIsEnabled.setValue(true);
                showNetworkError.setValue(error);
            }
        });
    }

    public void onUploadDialogShown() {
        preferences.setUserSawUploadReminderDialog(true);
        showUploadReminderDialog.setValue(false);
    }

    public void onNetworkErrorShown() {
        showNetworkError.setValue(null);
    }

    @NonNull
    private JamCountDownTimer createCountDownTimer() {
        return new JamCountDownTimer(Constants.SOUNDTRACK_RECORDING_COUNT_DOWN, TimeUtils.ONE_SECOND, new BaseJamTimer.OnTickCallback() {
            @Override
            public void onTicked(long millis) {
                countDownTimerMillis.setValue(millis);
            }

            @Override
            public void onFinished() {
                countDownTimerMillis.setValue(-1L);
                startRecordingSoundtrack(false);
            }
        });
    }

    @NonNull
    private JamTimer createTimer() {
        return new JamTimer(Soundtrack.MAX_TIME, TimeUtils.ONE_SECOND, new BaseJamTimer.OnTickCallback() {
            @Override
            public void onTicked(long millis) {
                timerMillis.setValue(millis);
            }

            @Override
            public void onFinished() {
                finishRecordingSoundtrack(false);
            }
        });
    }

    @Override
    protected void onCleared() {
        if (recordingSoundtrack) {
            finishRecordingSoundtrack(false);
        }
        metronomePlayer.stop();
    }

    @NonNull
    public LiveData<Integer> getRecordButtonImage() {
        return recordButtonImage;
    }

    @NonNull
    public LiveData<Integer> getRecordButtonColor() {
        return Transformations.map(recordButtonImage, buttonImage -> buttonImage == R.drawable.ic_record ? R.color.recordButtonColor : R.color.iconColor);
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
    public LiveData<Boolean> getUploadButtonIsEnabled() {
        return uploadButtonIsEnabled;
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
    public LiveData<Error> getShowNetworkError() {
        return showNetworkError;
    }
}
