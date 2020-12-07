package de.pcps.jamtugether.content.room.overview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.pcps.jamtugether.models.Soundtrack;
import de.pcps.jamtugether.dagger.AppInjector;

public class RoomOverviewViewModel extends ViewModel implements Soundtrack.OnChangeListener {

    @Inject
    Application application;

    private final int roomID;

    @NonNull
    private final String token;

    @NonNull
    private final MutableLiveData<List<Soundtrack>> allSoundtracks = new MutableLiveData<>(generateTestSoundtracks());

    public RoomOverviewViewModel(int roomID, @NonNull String token) {
        AppInjector.inject(this);
        this.roomID = roomID;
        this.token = token;
    }

    @NonNull
    private List<Soundtrack> generateTestSoundtracks() {
        List<Soundtrack> list = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            list.add(new Soundtrack(i));
        }
        return list;
    }

    @Override
    public void onVolumeChanged(@NonNull Soundtrack soundtrack, float volume) {
        soundtrack.setVolume(volume);
    }

    @Override
    public void onPlayPauseButtonClicked(@NonNull Soundtrack soundtrack) {
        // todo update stop button visibility of soundtrack
        // todo update play button drawable of soundtrack
    }

    @Override
    public void onStopButtonClicked(@NonNull Soundtrack soundtrack) { }

    @Override
    public void onFastForwardButtonClicked(@NonNull Soundtrack soundtrack) { }

    @Override
    public void onFastRewindButtonClicked(@NonNull Soundtrack soundtrack) { }

    @Override
    public void onDeleteButtonClicked(@NonNull Soundtrack soundtrack) {
        // todo update sound track list if sound track is in the list
        deleteSoundtrack();
    }

    private void deleteSoundtrack() {
        // todo delete soundtrack on server
    }

    private void fetchAllSoundtracks() {
        // todo get all soundtracks from server and update current list after
    }

    public int getRoomID() {
        return roomID;
    }

    @NonNull
    public LiveData<Soundtrack> getCompositeSoundtrack() {
        return Transformations.map(getAllSoundtracks(), Soundtrack::compositeFrom);
    }

    @NonNull
    public LiveData<List<Soundtrack>> getAllSoundtracks() {
        return allSoundtracks;
    }

    static class Factory implements ViewModelProvider.Factory {

        private final int roomID;

        @NonNull
        private final String token;

        public Factory(int roomID, @NonNull String token) {
            this.roomID = roomID;
            this.token = token;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(RoomOverviewViewModel.class)) {
                return (T) new RoomOverviewViewModel(roomID, token);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
