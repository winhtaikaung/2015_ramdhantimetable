package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import model.TimeTable;

/**
 * Created by jr on 6/16/15.
 */
public class TimeTable_Adapter extends BaseAdapter {
    List<TimeTable> timeTableList;
    Context mContext;
    LayoutInflater inflater;

    public TimeTable_Adapter(Context _context,List<TimeTable> _timetableList){
        super();
        timeTableList=_timetableList;
        mContext=_context;
        inflater=(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
    }

    static class ViewHolder{
        ImageView imgview_mainDate;
        TextView tv_sehri_time;
        TextView tv_iftari_time;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder vh;
        if(view!=null){
            vh=(ViewHolder) view.getTag();
        }else{
            vh=new ViewHolder();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


}
