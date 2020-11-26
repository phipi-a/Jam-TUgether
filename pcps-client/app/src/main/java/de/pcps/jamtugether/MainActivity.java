package de.pcps.jamtugether;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;

import javax.inject.Inject;

import de.pcps.jamtugether.dagger.AppInjector;
import de.pcps.jamtugether.storage.Preferences;
import de.pcps.jamtugether.utils.UiUtils;

public class MainActivity extends AppCompatActivity {

    @Inject
    Preferences preferences;

    private int currentFragmentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInjector.inject(this);

        DataBindingUtil.setContentView(this, R.layout.activity_main);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> currentFragmentID = destination.getId());

        NavInflater navInflater = navController.getNavInflater();
        NavGraph graph = navInflater.inflate(R.navigation.navigation);

        if(preferences.userNeverChoseInstrument()) {
            graph.setStartDestination(R.id.welcome_fragment);
        } else {
            graph.setStartDestination(R.id.menu_fragment);
        }

        navController.setGraph(graph);
    }

    @Override
    public void onBackPressed() {
        if(currentFragmentID == R.id.admin_room_fragment || currentFragmentID == R.id.regular_room_fragment) {
            AlertDialog dialog = UiUtils.createConfirmationDialog(this, R.string.leave_room, R.string.leave_room_confirmation, MainActivity.super::onBackPressed);
            dialog.setOnShowListener((DialogInterface.OnShowListener) arg -> {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(dialog.getContext(), R.color.primaryTextColor));
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(dialog.getContext(), R.color.primaryTextColor));
            });
            dialog.show();
        } else {
            super.onBackPressed();
        }
    }
}