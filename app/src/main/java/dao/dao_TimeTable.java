package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import db_helper.dbhelp;
import model.TimeTable;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.epicmyanmar.jr.ramdhantimetable.SplashActivity;

/**
 * Created by jr on 6/16/15.
 */
public class dao_TimeTable {

    private static dao_TimeTable _daotimetable;
    Context mContext;
    public dao_TimeTable(Context _Context){
        mContext=_Context;
    }

    public List<TimeTable> getTimetablefromlocal(){
        List<TimeTable> lstTimetable=new ArrayList<TimeTable>();
        dbhelp dbhelper=new dbhelp(mContext);
        String query="SELECT _id,chris_date,detail_info,main_date,sehri_time,iftari_time,is_kaderi FROM tb_timetable ORDER BY chris_date LIMIT 0,30";
        ArrayList datatable=new ArrayList();
        try {
            datatable = dbhelper.getDataTable(query);

            int i = 0;
            while (datatable.size() > i) {
                HashMap tablerow = new HashMap();
                tablerow = (HashMap) datatable.get(i);
                TimeTable obj = new TimeTable();
                obj.setId(tablerow.get("_id").toString());
                obj.setChris_date(tablerow.get("chris_date").toString());
                obj.setDetail_info(tablerow.get("detail_info").toString());
                obj.setMain_date(tablerow.get("main_date").toString());
                obj.setSehri_time(tablerow.get("sehri_time").toString());
                obj.setIftari_time(tablerow.get("iftari_time").toString());
                obj.setIs_kaderi(Boolean.parseBoolean(tablerow.get("is_kaderi").toString()));

                lstTimetable.add(obj);
                i++;
            }

        }catch (SQLiteException e){

        }
        return lstTimetable;

    }

    public void droptable(String Tablename){
        dbhelp dbhelp=new dbhelp(mContext);
        try {

            dbhelp.executeQuery("DELETE FROM" + Tablename);
        }catch(Exception e){
            Log.e("dao_Timetable/droptable",e.getMessage());
        }

    }

}
