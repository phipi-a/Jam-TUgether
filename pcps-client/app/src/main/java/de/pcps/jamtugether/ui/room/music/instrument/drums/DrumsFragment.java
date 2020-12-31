package de.pcps.jamtugether.ui.room.music.instrument.drums;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.databinding.FragmentDrumsBinding;
import de.pcps.jamtugether.ui.base.BaseFragment;
import de.pcps.jamtugether.ui.room.music.MusicianViewViewModel;
import de.pcps.jamtugether.ui.room.music.instrument.flute.FluteFragment;
import de.pcps.jamtugether.ui.room.music.instrument.flute.FluteViewModel;

public class DrumsFragment extends BaseFragment {

    private static final String ROOM_ID_KEY = "room_id_key";
    private static final String USER_ID_KEY = "user_id_key";
    private static final String TOKEN_KEY = "token_key";

    private DrumsViewModel viewModel;

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

        if (getArguments() != null) {
            int roomID = getArguments().getInt(ROOM_ID_KEY);
            int userID = getArguments().getInt(USER_ID_KEY);
            String token = getArguments().getString(TOKEN_KEY);

            MusicianViewViewModel.Factory musicianViewViewModelFactory = new MusicianViewViewModel.Factory(roomID, userID, token);
            MusicianViewViewModel musicianViewViewModel = new ViewModelProvider(getParentFragment(), musicianViewViewModelFactory).get(MusicianViewViewModel.class);

            DrumsViewModel.Factory drumsViewModelFactory = new DrumsViewModel.Factory(roomID, userID, musicianViewViewModel);
            viewModel = new ViewModelProvider(this, drumsViewModelFactory).get(DrumsViewModel.class);
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
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);
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
