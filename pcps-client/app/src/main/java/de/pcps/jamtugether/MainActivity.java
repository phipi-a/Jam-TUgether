package de.pcps.jamtugether;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;

import javax.inject.Inject;

import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.storage.Preferences;

public class MainActivity extends AppCompatActivity {

    @Inject
    Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInjector.inject(this);

        DataBindingUtil.setContentView(this, R.layout.activity_main);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavInflater navInflater = navController.getNavInflater();
        NavGraph graph = navInflater.inflate(R.navigation.navigation);

        graph.setStartDestination(R.id.on_boarding_fragment);

        /*if (!preferences.userCompletedOnBoarding()) {
            graph.setStartDestination(R.id.onboarding_fragment);
        } else {
            graph.setStartDestination(R.id.menu_fragment);
        }*/

        navController.setGraph(graph);
    }
}