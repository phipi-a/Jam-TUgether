package de.pcps.jamtugether.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;

import de.pcps.jamtugether.model.User;
import de.pcps.jamtugether.ui.menu.MenuFragmentDirections;
import de.pcps.jamtugether.ui.menu.create.CreateRoomFragmentDirections;
import de.pcps.jamtugether.ui.menu.join.JoinRoomFragmentDirections;
import de.pcps.jamtugether.ui.onboarding.OnBoardingFragmentDirections;

public class NavigationUtils {

    public static void navigateBack(@NonNull NavController nc) {
        nc.popBackStack();
    }

    public static void navigateToMenu(@NonNull NavController nc) {
        nc.navigate(OnBoardingFragmentDirections.actionOnBoardingFragmentToMenuFragment());
    }

    public static void navigateToSettings(@NonNull NavController nc) {
        nc.navigate(MenuFragmentDirections.actionMenuFragmentToSettingsFragment());
    }

    public static void navigateToCreateRoom(@NonNull NavController nc) {
        nc.navigate(MenuFragmentDirections.actionMenuFragmentToCreateRoomFragment());
    }

    public static void navigateToJoinRoom(@NonNull NavController nc) {
        nc.navigate(MenuFragmentDirections.actionMenuFragmentToJoinRoomFragment());
    }

    public static void navigateToRoomAsRegular(@NonNull NavController nc, int roomID, @NonNull User user, @NonNull String password, @NonNull String token) {
        nc.navigate(JoinRoomFragmentDirections.actionJoinRoomFragmentToRoomFragment(roomID, user, password, token, false));
    }

    public static void navigateToRoomAsAdmin(@NonNull NavController nc, int roomID, @NonNull User user, @NonNull String password, @NonNull String token) {
        nc.navigate(CreateRoomFragmentDirections.actionCreateRoomFragmentToRoomFragment(roomID, user, password, token, true));
    }

    public static void replaceFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, @IdRes int layout) {
        fragmentManager.beginTransaction().replace(layout, fragment).commit();
    }

    public static void navigateToAppDetailsSettings(@NonNull Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }
}
