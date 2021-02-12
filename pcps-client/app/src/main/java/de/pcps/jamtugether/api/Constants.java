package de.pcps.jamtugether.api;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.utils.TimeUtils;

public class Constants {

    @NonNull
    public static final String BASE_URL = "https://vm4.sese.tu-berlin.de:3000/api/";

    @NonNull
    public static final String BEARER_TOKEN_FORMAT = "Bearer %s";

    public static final long SOUNDTRACK_FETCHING_INTERVAL = TimeUtils.TEN_SECONDS;
    public static final long ADMIN_STATUS_FETCHING_INTERVAL = TimeUtils.TEN_SECONDS;

    public static final long SOUNDTRACK_RECORDING_COUNT_DOWN = TimeUtils.ONE_SECOND * 3;
}
