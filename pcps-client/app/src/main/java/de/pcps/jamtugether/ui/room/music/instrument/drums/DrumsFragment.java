package de.pcps.jamtugether.ui.room.music.instrument.drums;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.databinding.FragmentDrumsBinding;
import de.pcps.jamtugether.ui.room.music.instrument.InstrumentFragment;

public class DrumsFragment extends InstrumentFragment {

    @NonNull
    public static DrumsFragment newInstance(int roomID, int userID, @NonNull String token) {
        DrumsFragment fragment = new DrumsFragment();
        Bundle args = new Bundle();
        args.putInt(ROOM_ID_KEY, roomID);
        args.putInt(USER_ID_KEY, userID);
        args.putString(TOKEN_KEY, token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            DrumsViewModel.Factory drumsViewModelFactory = new DrumsViewModel.Factory(roomID, userID, musicianViewViewModel);
            instrumentViewModel = new ViewModelProvider(this, drumsViewModelFactory).get(DrumsViewModel.class);
            getLifecycle().addObserver(instrumentViewModel);
        }
    }

    /*public void onTouch(View view, MotionEvent motionEvent) {

        ClipData data = ClipData.newPlainText("", "");
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
        view.startDrag(data, shadowBuilder, null, 0);

        viewModel = new ViewModelProvider(this).get(DrumsViewModel.class);
    }*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentDrumsBinding binding = FragmentDrumsBinding.inflate(inflater, container, false);
        binding.setViewModel((DrumsViewModel) instrumentViewModel);
        binding.ownSoundtrackControlsLayout.setLifecycleOwner(getViewLifecycleOwner());
        binding.ownSoundtrackControlsLayout.setViewModel(instrumentViewModel);

        observeCompositeSoundtrack();

        //getView().setOnTouchListener(handleTouch);

        return binding.getRoot();

    }
    /*private View.OnTouchListener handleTouch = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            int x = (int) event.getX();
            int y = (int) event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.i("TAG", "touched down");
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i("TAG", "moving: (" + x + ", " + y + ")");
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i("TAG", "touched up");
                    break;
            }

            return true;
        }
    };*/
}
