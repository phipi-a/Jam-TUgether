package de.pcps.jamtugether.ui.room.music.instrument.drums;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import de.pcps.jamtugether.ui.base.BaseFragment;
import de.pcps.jamtugether.ui.room.music.instrument.drums.DrumsViewModel;

public class DrumsFragment extends BaseFragment {

    private DrumsViewModel viewModel;


    @NonNull
    public static DrumsFragment newInstance() {
        return new DrumsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DrumsViewModel.class);

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
