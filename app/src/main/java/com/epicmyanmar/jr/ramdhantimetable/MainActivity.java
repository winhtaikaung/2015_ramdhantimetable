package com.epicmyanmar.jr.ramdhantimetable;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.epicmyanmar.jr.ramdhantimetable.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import API.RetrofitAPI;
import adapter.DrawerList_Adapter;
import butterknife.ButterKnife;
import butterknife.InjectView;
import dao.dao_TimeTable;
import db_helper.dbhelp;
import fragments.Fragment_timetable;
import model.TimeTable;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends AppCompatActivity {

    private CharSequence Title;
    @InjectView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.left_drawer) LinearLayout mDrawerListLayout;
    @InjectView(R.id.left_drawer_lv) ListView mDrawerList;

    @InjectView(R.id.title_action2) TextView mTitle;
    ActionBarDrawerToggle mDrawerToggle;

    String[] DrawerMenuList;
    int[] DrawerIcons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/notosans_R.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        initialize();
        binddataTOList();

        makeFragmentSelection(0);




    }



    private void initialize(){

        setSupportActionBar(toolbar);
        toolbar.setTitle("Ramdhan TimeTable");

        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        //set toggle to naviation drawer on tollbar
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                // getActionBar().setTitle(mTitle);
                // toolbar.setTitle("PSI DataCapture");
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                // toolbar.setTitle(Title);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    void binddataTOList(){
        DrawerMenuList=new String[]{"Calendar", "Setting","About"};
        DrawerIcons=new int[]{R.drawable.ic_calendar, R.drawable.ic_setting,R.drawable.ic_info};
        DrawerList_Adapter drawerList_adapter=new DrawerList_Adapter(this,DrawerMenuList,DrawerIcons);
        drawerList_adapter.notifyDataSetChanged();
        mDrawerList.setAdapter(drawerList_adapter);
        mDrawerList.setOnItemClickListener(new DrawerListItemClickListener());
        setListViewHeightBasedOnChildren(mDrawerList);
    }


    protected class DrawerListItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            makeFragmentSelection(position);
        }

    }

    void makeFragmentSelection(int position){
        FragmentManager fragmentManager=getSupportFragmentManager();
        switch (position){
            case 0:
                getSupportActionBar().setTitle(DrawerMenuList[position]);
                fragmentManager.beginTransaction().replace(R.id.content_frame,new Fragment_timetable()).commit();
                break;
            case 1:
                toolbar.setTitle(DrawerMenuList[position]);
                //fragmentManager.beginTransaction().replace(R.id.content_frame, new Fragment_Sample()).commit();
                break;
            case 2:
                //Do action here
                toolbar.setTitle(DrawerMenuList[position]);
                break;
            case 3:
                //Do action here
                toolbar.setTitle(DrawerMenuList[position]);
                break;
            case 4:
                //Do action here
                toolbar.setTitle(DrawerMenuList[position]);
                break;
        }
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerListLayout);


    }



    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = listView.getPaddingTop()
                + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup)
                listItem.setLayoutParams(new AbsListView.LayoutParams(
                        AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        toolbar.inflateMenu(R.menu.menu_main);
        return true;
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
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
