package de.pcps.jamtugether.content.room.music.instruments.drums;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.MainActivity;
import de.pcps.jamtugether.R;
import de.pcps.jamtugether.databinding.FragmentDrumsBinding;
import de.pcps.jamtugether.databinding.FragmentFluteBinding;

public class DrumsFragment extends Fragment {

    private Activity activity;

    private DrumsViewModel viewModel;

    private SoundPool soundPool;
    private int kick;
    private int cymbal;
    private int snare;
    private int hat;


    @NonNull
    public static DrumsFragment newInstance() {
        return new DrumsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DrumsViewModel.class);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentDrumsBinding binding = FragmentDrumsBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);
        soundPool = new SoundPool.Builder().setMaxStreams(1).build();

        kick=soundPool.load(getContext(),R.raw.drum_kick,1);
        viewModel.getKickClicked().observe(getViewLifecycleOwner(), isClicked -> {
            soundPool.play(kick,1.0f,1.0f,0,0,10f);
        });

        cymbal=soundPool.load(getContext(),R.raw.drum_cymbal,1);
        viewModel.getCymbalClicked().observe(getViewLifecycleOwner(), isClicked -> {
            soundPool.play(cymbal,1.0f,1.0f,0,0,10f);
        });

        snare=soundPool.load(getContext(),R.raw.drum_snare,1);
        viewModel.getSnareClicked().observe(getViewLifecycleOwner(), isClicked -> {
            soundPool.play(snare,1.0f,1.0f,0,0,10f);
        });

        hat=soundPool.load(getContext(),R.raw.drum_hat,1);
        viewModel.getHatClicked().observe(getViewLifecycleOwner(), isClicked -> {
            soundPool.play(hat,1.0f,1.0f,0,0,10f);
        });

        /*imageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){

                    // Do what you want
                    return true;
                }
                return false;
            }
        });*/


        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }
}
