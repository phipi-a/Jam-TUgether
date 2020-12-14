package de.pcps.jamtugether.content.room;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import de.pcps.jamtugether.R;

public class JamTabView extends ConstraintLayout {

    private TextView tabTitleTextView;

    public JamTabView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tabTitleTextView = findViewById(R.id.tab_title_text_view);
    }

    public void setTitle(@StringRes int title) {
        this.tabTitleTextView.setText(title);
    }

    public void activate() {
        tabTitleTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryTextColor));
    }

    public void deactivate() {
        tabTitleTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
    }

    @NonNull
    public static JamTabView from(@NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return (JamTabView) inflater.inflate(R.layout.view_tab, parent, false);
    }
}
