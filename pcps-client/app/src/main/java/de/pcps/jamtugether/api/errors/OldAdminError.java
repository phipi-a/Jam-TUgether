package de.pcps.jamtugether.api.errors;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.api.errors.base.Error;

public class OldAdminError extends Error {

    public OldAdminError() {
        super(R.string.old_admin_error_title, R.string.old_admin_error_message);
    }
}
