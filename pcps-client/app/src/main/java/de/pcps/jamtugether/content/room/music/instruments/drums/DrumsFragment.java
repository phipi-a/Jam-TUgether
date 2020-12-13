package de.pcps.jamtugether.content.room.music.instruments.drums;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.MainActivity;
import de.pcps.jamtugether.R;
import de.pcps.jamtugether.databinding.FragmentDrumsBinding;

public class DrumsFragment extends Fragment {

    private Activity activity;

    private DrumsViewModel viewModel;

    //MediaPlayer kick;

    private SoundPool soundPool;
    private int kick;



    @NonNull
    public static DrumsFragment newInstance() {
        return new DrumsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DrumsViewModel.class);
        //kick = MediaPlayer.create(getContext(),R.raw.drum_kick);
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        kick=soundPool.load(getContext(),R.raw.drum_kick,1);
    }

    public void playKick(View view){
        soundPool.play(kick,1.0f,1.0f,0,0,10f);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentDrumsBinding binding = FragmentDrumsBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }
}
