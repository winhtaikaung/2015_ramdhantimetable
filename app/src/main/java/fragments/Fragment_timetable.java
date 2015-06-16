package fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.epicmyanmar.jr.ramdhantimetable.DetailActivity;
import com.epicmyanmar.jr.ramdhantimetable.R;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.rey.material.widget.ProgressView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import API.RetrofitAPI;
import adapter.TimeTable_Adapter;
import butterknife.ButterKnife;
import butterknife.InjectView;
import dao.dao_TimeTable;
import db_helper.dbhelp;
import model.TimeTable;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by jr on 6/16/15.
 */
public class Fragment_timetable extends Fragment {
    View view;
    ListView listView;


    View processBackground,error_view;
    View progressView;
    TextView error_message;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_timetable, container, false);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        listView=(ListView) view.findViewById(R.id.timetable_lv);
        progressView= view.findViewById(R.id.progress_pv_circular_inout_colors);
        processBackground= view.findViewById(R.id.process_background);
        error_view=view.findViewById(R.id.layout_errorrview);
        error_message=(TextView) view.findViewById(R.id.tv_errormessage);

        listView.setOnItemClickListener(new OnTimeTableItemClick());



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        dao_TimeTable dao_timeTable=new dao_TimeTable(getActivity());
        if(dao_timeTable.getTimetablefromlocal().size()!=0){


            TimeTable_Adapter timeTable_adapterdapter=new TimeTable_Adapter(getActivity(),dao_timeTable.getTimetablefromlocal());
            listView.setAdapter(timeTable_adapterdapter);
            dismissProgress();
        }else{

            grabdata();



        }
    }

    void grabdata(){

        RetrofitAPI.getInstance(getActivity()).getService().getTimetables(new Callback<String>() {
            @Override
            public void success(String s, Response response) {


                dbhelp dbhelp=new dbhelp(getActivity());
                dbhelp.MakeDB();
                InsertLocaldb(getTimeTableListFromJson(s));

                dao_TimeTable dao_timeTable=new dao_TimeTable(getActivity());
                dao_timeTable.getTimetablefromlocal();
                Log.e("DB__AFTER__SIZE", "" + dao_timeTable.getTimetablefromlocal().size());


                TimeTable_Adapter timeTable_adapterdapter=new TimeTable_Adapter(getActivity(),dao_timeTable.getTimetablefromlocal());
                listView.setAdapter(timeTable_adapterdapter);
                progressView.setVisibility(View.INVISIBLE);

                /**/
            }

            @Override
            public void failure(RetrofitError error) {
                dismissProgress();
                //showErrorView("Couldn't get data from Server!");
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    void showErrorView(String errmsg){
        error_view.setVisibility(View.VISIBLE);
        listView.setVisibility(View.INVISIBLE);
        error_message.setText(errmsg);
    }

    private void showProgress(){

        progressView.setVisibility(View.VISIBLE);
        processBackground.setVisibility(View.VISIBLE);
        progressView.bringToFront();
        processBackground.bringToFront();
    }

    private void dismissProgress(){
        progressView.setVisibility(View.INVISIBLE);
        processBackground.setVisibility(View.INVISIBLE);
    }

    protected class OnTimeTableItemClick implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent(getActivity(), DetailActivity.class);
            startActivity(intent);
        }
    }
}
