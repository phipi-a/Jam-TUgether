package de.pcps.jamtugether.ui.room.music.instrument;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import de.pcps.jamtugether.api.repositories.SoundtrackRepository;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.player.composite.CompositeSoundtrackPlayer;
import de.pcps.jamtugether.audio.player.single.SingleSoundtrackPlayer;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import de.pcps.jamtugether.timer.JamCountDownTimer;
import de.pcps.jamtugether.timer.JamTimer;
import de.pcps.jamtugether.timer.base.BaseJamTimer;
import de.pcps.jamtugether.ui.room.music.OnOwnSoundtrackChangedCallback;
import de.pcps.jamtugether.utils.TimeUtils;

public abstract class InstrumentViewModel extends ViewModel implements LifecycleObserver {

    @Inject
    protected Application application;

    @Inject
    protected SoundtrackRepository soundtrackRepository;

    @Inject
    protected CompositeSoundtrackPlayer compositeSoundtrackPlayer;

    @Inject
    protected SingleSoundtrackPlayer singleSoundtrackPlayer;

    @NonNull
    private final Instrument instrument;

    protected final int roomID;
    protected final int userID;

    @NonNull
    protected final OnOwnSoundtrackChangedCallback callback;

    @NonNull
    protected final MutableLiveData<Boolean> startedSoundtrackCreation = new MutableLiveData<>(false);

    @NonNull
    protected final MutableLiveData<Long> countDownTimerMillis = new MutableLiveData<>();

    @NonNull
    protected final MutableLiveData<Long> timerMillis = new MutableLiveData<>();

    @NonNull
    private final BaseJamTimer.OnTickCallback countDownTimerCallback = new BaseJamTimer.OnTickCallback() {
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
            if(playWithCompositeSoundtrack) {
                compositeSoundtrackPlayer.stop(compositeSoundtrack);
                compositeSoundtrackPlayer.play(compositeSoundtrack);
            }
        }
    };

    @NonNull
    protected final BaseJamTimer countDownTimer = new JamCountDownTimer(TimeUtils.ONE_SECOND * 3, TimeUtils.ONE_SECOND, countDownTimerCallback);

    private final BaseJamTimer.OnTickCallback timerCallback = new BaseJamTimer.OnTickCallback() {
        @Override
        public void onTicked(long millis) {
            timerMillis.setValue(millis);
        }

        @Override
        public void onFinished() {
            finishSoundtrack();
        }
    };

    @NonNull
    protected final BaseJamTimer timer = new JamTimer(Soundtrack.MAX_TIME, TimeUtils.ONE_SECOND, timerCallback);

    @Nullable
    private CompositeSoundtrack compositeSoundtrack;

    @Nullable
    protected SingleSoundtrack ownSoundtrack;

    @NonNull
    private final MutableLiveData<Boolean> uploadPossible = new MutableLiveData<>(false);

    private boolean playWithCompositeSoundtrack;

    protected long startedMillis;

    public InstrumentViewModel(@NonNull Instrument instrument, int roomID, int userID, @NonNull OnOwnSoundtrackChangedCallback callback) {
        AppInjector.inject(this);
        this.instrument = instrument;
        this.roomID = roomID;
        this.userID = userID;
        this.callback = callback;
    }

    public void onCompositeSoundtrackChanged(@NonNull CompositeSoundtrack compositeSoundtrack) {
        this.compositeSoundtrack = compositeSoundtrack;
    }

    public void onPlayWithCompositeSoundtrackClicked(boolean checked) {
        this.playWithCompositeSoundtrack = checked;
    }

    public void onCreateSoundtrackButtonClicked() {
        if (startedSoundtrackCreation()) {
            if(countDownTimer.isStopped()) {
                finishSoundtrack();
            } else {
                countDownTimer.stop();
                countDownTimerMillis.setValue(-1L);
                startedSoundtrackCreation.setValue(false);
            }
        } else {
            timerMillis.setValue(-1L);
            // set userID to -1 so this soundtrack isn't linked to published soundtrack of this user
            ownSoundtrack = new SingleSoundtrack(-1, instrument);
            ownSoundtrack.loadSounds(application.getApplicationContext());
            startedSoundtrackCreation.setValue(true);
            countDownTimer.start();
        }
    }

    public void onUploadButtonClicked() {
        if(ownSoundtrack == null) {
            return;
        }
        SingleSoundtrack publishOwnSoundtrack = new SingleSoundtrack(userID, ownSoundtrack.getSoundSequence());
        // todo publish
    }

    protected void finishSoundtrack() {
        timer.stop();
        if(ownSoundtrack != null && !ownSoundtrack.isEmpty()) {
            singleSoundtrackPlayer.stop(ownSoundtrack);
            callback.onOwnSoundtrackChanged(ownSoundtrack);
            this.uploadPossible.setValue(true);
        }
        startedSoundtrackCreation.setValue(false);
    }

    protected boolean startedSoundtrackCreation() {
        Boolean started = startedSoundtrackCreation.getValue();
        return started != null && started;
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
    public LiveData<Boolean> getUploadPossible() {
        return uploadPossible;
    }
}
