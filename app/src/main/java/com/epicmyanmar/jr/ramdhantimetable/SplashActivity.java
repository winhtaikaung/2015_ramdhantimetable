package com.epicmyanmar.jr.ramdhantimetable;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import API.RetrofitAPI;
import com_functions.Common_helper;
import com_functions.MySharedPreference;
import dao.dao_TimeTable;
import db_helper.dbhelp;
import model.TimeTable;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/notosans_R.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        MySharedPreference.getInstance(SplashActivity.this).setBooleanPreference("DB_CRATED",false);

        setContentView(R.layout.activity_splash);
        Common_helper common_helper=new Common_helper(this);
        if(!common_helper.isNetworkConnected()) {
            BuildNoInternet();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        dao_TimeTable dao_timeTable=new dao_TimeTable(getApplicationContext());
        try{
            dao_timeTable.getTimetablefromlocal();
            Log.e("DB_SIZE", "" + dao_timeTable.getTimetablefromlocal().size());
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            finish();
            startActivity(intent);
        }catch (Exception ex){
            grabdata();
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            finish();
            startActivity(intent);
        }





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    void grabdata(){
        RetrofitAPI.getInstance(this).getService().getTimetables(new Callback<String>() {
            @Override
            public void success(String s, Response response) {


                    dbhelp dbhelp=new dbhelp(SplashActivity.this);
                    dbhelp.MakeDB();
                    InsertLocaldb(getTimeTableListFromJson(s));

                    Log.i("DB_CREATED","db created");




                /**/
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    void InsertLocaldb(List<TimeTable> lst){
        dbhelp dbhelp=new dbhelp(SplashActivity.this);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void BuildNoInternet() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Mobile needs DataSync for first Time installation, do you want to enable DataConnection it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
