package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.epicmyanmar.jr.ramdhantimetable.R;

import java.util.ArrayList;
import java.util.List;

import model.TimeTable;

/**
 * Created by jr on 6/16/15.
 */
public class TimeTable_Adapter extends BaseAdapter {
    List<TimeTable> timeTableList=new ArrayList<>();
    Context mContext;
    LayoutInflater inflater;

    public TimeTable_Adapter(Context _context,List<TimeTable> _timetableList){
        super();
        timeTableList=_timetableList;
        mContext=_context;
        inflater=(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
    }

    static class ViewHolder{
        TextView imgview_mainDate;
        TextView tv_sehri_time;
        TextView tv_iftari_time;
        TextView tv_chris_date;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder vh;
        if(view!=null){
            vh=(ViewHolder) view.getTag();
        }else{
            vh=new ViewHolder();
            view=inflater.inflate(R.layout.timetable_listitem,parent,false);
            vh.imgview_mainDate=(TextView) view.findViewById(R.id.tv_main_date);
            vh.tv_sehri_time=(TextView) view.findViewById(R.id.tv_sehri_time);
            vh.tv_iftari_time=(TextView) view.findViewById(R.id.tv_iftari_time);
            vh.tv_chris_date=(TextView) view.findViewById(R.id.tv_chris_date);
            view.setTag(vh);
        }

        vh.imgview_mainDate.setText(timeTableList.get(position).getMain_date());
        vh.tv_sehri_time.setText(timeTableList.get(position).getSehri_time());
        vh.tv_iftari_time.setText(timeTableList.get(position).getIftari_time());
        vh.tv_chris_date.setText(timeTableList.get(position).getChris_date());
        return view;
    }

    @Override
    public int getCount() {
        return timeTableList.size();
    }

    @Override
    public Object getItem(int position) {
        return timeTableList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }




}
