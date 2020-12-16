package de.pcps.jamtugether.api;

import de.pcps.jamtugether.utils.TimeUtils;

public class Constants {

    public static final String BASE_URL = "http://vm4.sese.tu-berlin.de:3000/api/";

    public static final String BEARER_TOKEN_FORMAT = "Bearer %s";

    public static final long SOUNDTRACK_FETCHING_INTERVAL = TimeUtils.ONE_SECOND * 20;
}
