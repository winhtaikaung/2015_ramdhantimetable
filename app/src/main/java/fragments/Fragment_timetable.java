package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.epicmyanmar.jr.ramdhantimetable.R;

import butterknife.InjectView;
import dao.dao_TimeTable;

/**
 * Created by jr on 6/16/15.
 */
public class Fragment_timetable extends Fragment {
    View view;
    @InjectView(R.id.timetable_lv)ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_timetable, container, false);
        dao_TimeTable dao_timeTable=new dao_TimeTable(getActivity());
        dao_timeTable.getTimetablefromlocal();


        return view;
    }
}
