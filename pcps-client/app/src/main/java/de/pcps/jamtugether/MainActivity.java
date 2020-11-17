package de.pcps.jamtugether;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import de.pcps.jamtugether.base.dagger.AppInjector;
import de.pcps.jamtugether.databinding.ActivityMainBinding;
import de.pcps.jamtugether.storage.Preferences;

public class MainActivity extends AppCompatActivity {

    @Inject
    Preferences preferences;

    private AppBarConfiguration appBarConfiguration;

    private NavController navController;

    private Integer navDestinationId;
    private Integer startDestinationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInjector.inject(this);

        DataBindingUtil.setContentView(this, R.layout.activity_main);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavInflater navInflater = navController.getNavInflater();
        NavGraph graph = navInflater.inflate(R.navigation.navigation);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> navDestinationId = destination.getId());

        if(preferences.userNeverChoseInstrument()) {
            startDestinationId = R.id.mainInstrumentFragment;
        } else {
            startDestinationId = R.id.menuFragment;
        }
        graph.setStartDestination(startDestinationId);


        navController.setGraph(graph);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration);
    }

    @Override
    public void onBackPressed() {
        if(navDestinationId.equals(startDestinationId)) {
            super.onBackPressed();
        } else {
            NavigationUI.navigateUp(navController, appBarConfiguration);
        }
    }
}