package de.pcps.jamtugether.content.room;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.utils.UiUtils;
import de.pcps.jamtugether.views.JamTabView;

public abstract class RoomFragment extends TabLayoutFragment {

    protected int roomID;
    
    protected String token;

    private RoomViewModel viewModel;

    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(RoomViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        viewModel.getShowLeaveRoomConfirmationDialog().observe(getViewLifecycleOwner(), showLeaveRoomConfirmationDialog -> {
            if(showLeaveRoomConfirmationDialog) {
                AlertDialog dialog = UiUtils.createConfirmationDialog(context, R.string.leave_room, R.string.leave_room_confirmation, () -> viewModel.onLeaveRoomConfirmationButtonClicked());
                dialog.setOnShowListener(arg -> {
                    viewModel.onLeaveRoomConfirmationDialogShown();
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(dialog.getContext(), R.color.primaryTextColor));
                    dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(dialog.getContext(), R.color.primaryTextColor));
                });
                dialog.show();
            }
        });

        viewModel.getNavigateBack().observe(getViewLifecycleOwner(), navigateBack -> {
            if(navigateBack) {
                this.navigateBack();
                viewModel.onNavigatedBack();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    protected void handleOnBackPressed() {
        viewModel.handleBackPressed();
    }

    @NonNull
    @Override
    protected TabLayoutAdapter createTabLayoutAdapter() {
        return new TabLayoutAdapter(this) {

            @NonNull
            @Override
            public JamTabView getTabView(int position) {
                return JamTabView.from(tabLayout);
            }

            @Override
            public int getTabTitle(int position) {
                return position == 0 ? R.string.room_over_view : R.string.musician_view;
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return RoomFragment.this.createFragment(position);
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        };
    }

    @NonNull
    protected abstract Fragment createFragment(int position);
}
