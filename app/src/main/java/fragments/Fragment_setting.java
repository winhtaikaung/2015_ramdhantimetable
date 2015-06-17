package fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epicmyanmar.jr.ramdhantimetable.R;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.rey.material.app.Dialog;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import API.RetrofitAPI;
import adapter.TimeTable_Adapter;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com_functions.Common_helper;
import dao.dao_TimeTable;
import db_helper.dbhelp;
import model.TimeTable;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by jr on 6/17/15.
 */
public class Fragment_setting extends Fragment {
    View view;
    Common_helper common_helper;

    View processBackground,error_view;
    ProgressWheel progressView;
    @InjectView(R.id.tv_syncdata)  TextView tv_syncdata;
    @InjectView(R.id.fab_sync)
    FloatingActionButton fab_sync;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        progressView= (ProgressWheel)view.findViewById(R.id.progress_pv_circular_inout_colors);
        processBackground= view.findViewById(R.id.process_background);
        ButterKnife.inject(this,view);
        tv_syncdata.setOnClickListener(new OnSyncbuttonCLick());
        fab_sync.setOnClickListener(new OnSyncbuttonCLick());

        return view;
    }


    private void showProgress(){

        progressView.setVisibility(View.VISIBLE);
        processBackground.setVisibility(View.VISIBLE);

        processBackground.bringToFront();
        progressView.bringToFront();

    }

    private void dismissProgress(){
        progressView.setVisibility(View.INVISIBLE);
        processBackground.setVisibility(View.INVISIBLE);
    }

    protected class OnSyncbuttonCLick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.tv_syncdata || v.getId()==R.id.fab_sync ){
                common_helper=new Common_helper(getActivity());
                if(common_helper.isNetworkConnected()){
                    new MaterialDialog.Builder(getActivity())
                            .title("Are you sure to sync data From Server")
                            .content("By syncing data from server will download latest update data")
                            .positiveText("Yes")
                            .negativeText("No")
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    super.onPositive(dialog);
                                    grabdata();
                                    dialog.setContent("Syncing data from Server");
                                    dialog.setTitle("Please Wait....");
                                    dialog.show();
                                    //dialog.dismiss();
                                }

                                @Override
                                public void onNegative(MaterialDialog dialog) {
                                    super.onNegative(dialog);
                                    dialog.dismiss();
                                }
                            }).show();

                }else{

                }
            }
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    void grabdata(){
        showProgress();
        dao_TimeTable dao_timeTable=new dao_TimeTable(getActivity());
        dao_timeTable.droptable("tb_timetable");
        RetrofitAPI.getInstance(getActivity()).getService().getTimetables(new Callback<String>() {
            @Override
            public void success(String s, Response response) {


                dbhelp dbhelp=new dbhelp(getActivity());
                dbhelp.MakeDB();

                InsertLocaldb(getTimeTableListFromJson(s));

                dao_TimeTable dao_timeTable=new dao_TimeTable(getActivity());
                dao_timeTable.getTimetablefromlocal();
                Log.e("DB__AFTER__SIZE", "" + dao_timeTable.getTimetablefromlocal().size());
                dismissProgress();


                /**/
            }

            @Override
            public void failure(RetrofitError error) {

                Log.e("SYNC_ERROR", "" + error.getMessage());
            }
        });
    }

    void InsertLocaldb(List<TimeTable> lst){
        dbhelp dbhelp=new dbhelp(getActivity());
        for(TimeTable timeTable : lst){
            String query="INSERT INTO tb_timetable VALUES ('" +timeTable.getId()+"','"+timeTable.getChris_date()+"','"+timeTable.getDetail_info()+"','"+timeTable.getMain_date()+"','"+timeTable.getSehri_time()+"','"+timeTable.getIftari_time()+"','"+timeTable.is_kaderi()+"');";
            Log.i("DB_QUERY",query);
            dbhelp.executeQuery(query);
        }

    }

    List<TimeTable> getTimeTableListFromJson(String json){
        List<TimeTable> timeTableList=new ArrayList<>() ;
        try{
            JSONObject obj=new JSONObject(json);
            if(!obj.isNull("data")){
                JSONArray jsonArray= obj.getJSONArray("data");
                int i=0;
                while (jsonArray.length()>i){
                    JSONObject timetable=jsonArray.getJSONObject(i);
                    TimeTable t=new TimeTable();

                    if(!timetable.isNull("_id")){
                        t.setId(timetable.getString("_id"));
                    }
                    if(!timetable.isNull("chris_date")){
                        t.setChris_date(timetable.getString("chris_date"));
                    }
                    if(!timetable.isNull("detail_info")){
                        t.setDetail_info(timetable.getString("detail_info"));
                    }
                    if(!timetable.isNull("main_date")){
                        t.setMain_date(timetable.getString("main_date"));
                    }
                    if(!timetable.isNull("sehri_time")){
                        t.setSehri_time(timetable.getString("sehri_time"));
                    }
                    if(!timetable.isNull("iftari_time")){
                        t.setIftari_time(timetable.getString("iftari_time"));
                    }
                    if(!timetable.isNull("is_kaderi")){
                        t.setIs_kaderi(timetable.getBoolean("is_kaderi"));
                    }

                    timeTableList.add(t);

                    i++;

                }

            }
        }catch(Exception e){
            return timeTableList;
        }
        return timeTableList;
    }
}
