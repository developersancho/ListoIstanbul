package sf.listoistanbul.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import sf.cuboidcirclebutton.CuboidButton;
import sf.listoistanbul.R;
import sf.slideupmenu.SlideUp;
import sf.slideupmenu.SlideUpBuilder;

/**
 * Created by mesutgenc on 9.09.2017.
 */

public class HomeFragment extends Fragment {

    Toast toast;
    private SlideUp slideUp;
    private View dim;
    private View sliderView;
    private CuboidButton cbtnSearch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        sliderView = rootView.findViewById(R.id.slideView);
        sliderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        dim = rootView.findViewById(R.id.dim);
        cbtnSearch = (CuboidButton) rootView.findViewById(R.id.cbtnSearch);

        slideUp = new SlideUpBuilder(sliderView)
                .withListeners(new SlideUp.Listener.Events() {
                    @Override
                    public void onSlide(float percent) {
                        dim.setAlpha(1 - (percent / 100));
                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {
                        if (visibility == View.GONE) {
                            cbtnSearch.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .withStartGravity(Gravity.BOTTOM)
                .withLoggingEnabled(true)
                .withGesturesEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .build();

        cbtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideUp.show();
                cbtnSearch.setVisibility(View.INVISIBLE);
            }
        });


        return rootView;
    }
}
