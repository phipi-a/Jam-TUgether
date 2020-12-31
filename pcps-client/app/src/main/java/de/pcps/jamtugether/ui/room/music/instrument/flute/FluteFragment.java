package de.pcps.jamtugether.ui.room.music.instrument.flute;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.ui.base.BaseFragment;
import de.pcps.jamtugether.databinding.FragmentFluteBinding;
import de.pcps.jamtugether.ui.room.music.MusicianViewViewModel;
import timber.log.Timber;

public class FluteFragment extends BaseFragment {

    private static final String ROOM_ID_KEY = "room_id_key";
    private static final String USER_ID_KEY = "user_id_key";
    private static final String TOKEN_KEY = "token_key";

    private static final int REQUEST_MICROPHONE = 1;

    private FluteViewModel viewModel;

    @NonNull
    public static FluteFragment newInstance(int roomID, int userID, @NonNull String token) {
        FluteFragment fragment = new FluteFragment();
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

            FluteViewModel.Factory fluteViewModelFactory = new FluteViewModel.Factory(roomID, userID, musicianViewViewModel);
            viewModel = new ViewModelProvider(this, fluteViewModelFactory).get(FluteViewModel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentFluteBinding binding = FragmentFluteBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);

        ClipDrawable clipDrawable = (ClipDrawable) binding.ivFluteFill.getDrawable();
        viewModel.getPitchPercentage().observe(getViewLifecycleOwner(), percentage -> clipDrawable.setLevel((int) (10000 * percentage)));

        if (savedInstanceState == null) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_MICROPHONE);
            } else {
                viewModel.onUserHasPermission();
            }
        }

        return binding.getRoot();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_MICROPHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.onUserHasPermission();
            } else {
                //TODO:Add Error Message
                Timber.e("onRequestPermissionsResult: No microphone permission");
            }
        }
    }
}
