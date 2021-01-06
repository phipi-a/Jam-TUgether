package de.pcps.jamtugether.api.errors;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.api.errors.base.Error;

public class PageNotFoundError extends Error {

    public PageNotFoundError() {
        super(R.string.page_not_found_error_title, R.string.page_not_found_error_message);
    }
}
