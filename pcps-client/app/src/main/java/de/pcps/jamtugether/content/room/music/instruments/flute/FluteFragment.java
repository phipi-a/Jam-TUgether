package de.pcps.jamtugether.content.room.music.instruments.flute;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.databinding.FragmentFluteBinding;
import timber.log.Timber;

public class FluteFragment extends Fragment {

    private static final int REQUEST_MICROPHONE = 1;

    private Activity activity;

    private FluteViewModel viewModel;

    @NonNull
    public static FluteFragment newInstance() {
        return new FluteFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FluteViewModel.class);
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
                viewModel.startRecording(activity);
            }
        }

        return binding.getRoot();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_MICROPHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.startRecording(activity);
            } else {
                //TODO:Add Error Message
                Timber.e("onRequestPermissionsResult: No microphone permission");
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }
}
