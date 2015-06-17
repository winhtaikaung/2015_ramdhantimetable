package com.epicmyanmar.jr.ramdhantimetable;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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


import com.afollestad.materialdialogs.MaterialDialog;

import adapter.DrawerList_Adapter;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com_functions.Common_helper;
import dao.dao_TimeTable;
import fragments.Fragment_credits;
import fragments.Fragment_setting;
import fragments.Fragment_timetable;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends AppCompatActivity {

    private CharSequence Title;
    @InjectView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    Toolbar toolbar;
    @InjectView(R.id.left_drawer) LinearLayout mDrawerListLayout;
    @InjectView(R.id.left_drawer_lv) ListView mDrawerList;

    @InjectView(R.id.title_action2) TextView mTitle;
    ActionBarDrawerToggle mDrawerToggle;

    String[] DrawerMenuList;
    int[] DrawerIcons;
    Common_helper common_helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Padauk.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);

        initialize();
        binddataTOList();
        common_helper=new Common_helper(this);
        dao_TimeTable dao_timeTable=new dao_TimeTable(MainActivity.this);
        if(dao_timeTable.getTimetablefromlocal().size()!=0) {
            makeFragmentSelection(0);

        }else{
            makeFragmentSelection(0);
        }





    }



    private void initialize(){


        toolbar.setTitle("Ramdhan TimeTable");

        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setOnMenuItemClickListener(new ToolbarMenuclickListener());


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
        DrawerMenuList=new String[]{"TimeTable", "Settings","Credits"};
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
    protected class ToolbarMenuclickListener implements Toolbar.OnMenuItemClickListener{

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_sehri_Dua) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title("Sehri Dua")
                        .content(getResources().getString(R.string.Sehri_dua))
                        .positiveText("OK")
                        .show();

                return true;
            }
            if(id==R.id.action_iftari_dua){
                new MaterialDialog.Builder(MainActivity.this)
                        .title("Iftari Dua")
                        .content(getResources().getString(R.string.Iftari_dua))
                        .positiveText("OK")
                        .show();

                return true;
            }
            if(id==R.id.action_about){
                new MaterialDialog.Builder(MainActivity.this)
                        .title("About")
                        .content(getResources().getString(R.string.about_me))
                        .positiveText("OK")
                        .show();

                return true;
            }


            return false;
        }
    }

    void makeFragmentSelection(int position){
        FragmentManager fragmentManager=getSupportFragmentManager();
        switch (position){
            case 0:
                toolbar.setTitle(DrawerMenuList[position]);
                fragmentManager.beginTransaction().replace(R.id.content_frame,new Fragment_timetable()).commit();
                break;
            case 1:
                toolbar.setTitle(DrawerMenuList[position]);
                fragmentManager.beginTransaction().replace(R.id.content_frame, new Fragment_setting()).commit();
                break;
            case 2:
                //Do action here
                toolbar.setTitle(DrawerMenuList[position]);
                fragmentManager.beginTransaction().replace(R.id.content_frame, new Fragment_credits()).commit();
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


        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return true;
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void BuildNoInternet() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn on Your Internet to sync Data From Server?")
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
}
