package fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epicmyanmar.jr.ramdhantimetable.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by jr on 6/17/15.
 */
public class Fragment_credits extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_credits,container,false);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Padauk.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        return view;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
