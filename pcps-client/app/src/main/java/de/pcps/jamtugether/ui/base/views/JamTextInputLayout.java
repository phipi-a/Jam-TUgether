package de.pcps.jamtugether.ui.base.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;

import de.pcps.jamtugether.R;

public class JamTextInputLayout extends TextInputLayout {

    public JamTextInputLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setErrorTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.inputTextErrorColor)));
        this.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primaryTextColor)));
    }

    @Override
    public void setError(@Nullable CharSequence errorText) {
        super.setError(errorText);
        setErrorEnabled(errorText != null);
    }
}
