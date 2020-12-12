package de.pcps.jamtugether.content.room.overview;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.pcps.jamtugether.api.errors.Error;
import de.pcps.jamtugether.api.repositories.RoomRepository;
import de.pcps.jamtugether.api.repositories.SoundtrackRepository;
import de.pcps.jamtugether.dagger.AppInjector;
import de.pcps.jamtugether.models.music.soundtrack.Soundtrack;
import de.pcps.jamtugether.models.music.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.models.music.soundtrack.SingleSoundtrack;

public abstract class RoomOverviewViewModel extends ViewModel implements Soundtrack.OnChangeListener {

    @Inject
    protected Application application;

    @Inject
    protected RoomRepository roomRepository;

    @Inject
    protected SoundtrackRepository soundtrackRepository;

    protected final int roomID;

    @NonNull
    protected final String token;

    @NonNull
    protected final MutableLiveData<List<Soundtrack>> allSoundtracks = new MutableLiveData<>(generateTestSoundtracks());

    @NonNull
    protected final MutableLiveData<Error> networkError = new MutableLiveData<>();

    @NonNull
    protected final MutableLiveData<Boolean> leaveRoom = new MutableLiveData<>();

    public RoomOverviewViewModel(int roomID, @NonNull String token) {
        AppInjector.inject(this);
        this.roomID = roomID;
        this.token = token;
    }

    @NonNull
    private List<Soundtrack> generateTestSoundtracks() {
        List<Soundtrack> list = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            list.add(new SingleSoundtrack(i));
        }
        return list;
    }

    @Override
    public void onVolumeChanged(@NonNull Soundtrack soundtrack, float volume) {
        soundtrack.setVolume(volume);
    }

    @Override
    public void onPlayPauseButtonClicked(@NonNull Soundtrack soundtrack) { }

    @Override
    public void onStopButtonClicked(@NonNull Soundtrack soundtrack) { }

    @Override
    public void onFastForwardButtonClicked(@NonNull Soundtrack soundtrack) { }

    @Override
    public void onFastRewindButtonClicked(@NonNull Soundtrack soundtrack) { }

    private void fetchAllSoundtracks() {
        // todo get all soundtracks from server and update current list after
    }

    public void onNetworkErrorShown() {
        networkError.setValue(null);
    }

    public void onLeftRoom() {
        leaveRoom.setValue(false);
    }

    public int getRoomID() {
        return roomID;
    }

    public LiveData<Boolean> getLeaveRoom() {
        return leaveRoom;
    }

    @NonNull
    public LiveData<Soundtrack> getCompositeSoundtrack() {
        return Transformations.map(getAllSoundtracks(), CompositeSoundtrack::from);
    }

    @NonNull
    public LiveData<List<Soundtrack>> getAllSoundtracks() {
        return allSoundtracks;
    }

    @NonNull
    public MutableLiveData<Error> getNetworkError() {
        return networkError;
    }
}
