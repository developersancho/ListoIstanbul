package customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by mesutgenc on 9.09.2017.
 */

public class AppBarTextView extends AppCompatTextView {
    public AppBarTextView(Context context) {
        super(context);
        init();
    }

    public AppBarTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AppBarTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Spiderman-Homecoming.otf");
            setTypeface(typeface);
        }
    }

}
