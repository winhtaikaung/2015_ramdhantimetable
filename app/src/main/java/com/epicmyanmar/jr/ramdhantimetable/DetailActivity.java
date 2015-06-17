package com.epicmyanmar.jr.ramdhantimetable;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DetailActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    String id,maindate,sehritime,iftaritime,chrisdate,detail;
    boolean is_kadri;

    private int mParallaxImageHeight;

    Toolbar toolbar;
    @InjectView(R.id.scrollView_detail)
    ObservableScrollView scrollView_detail;

    @InjectView(R.id.header_imageView)
    ImageView headerimage_view;

    @InjectView(R.id.tv_maindate)
    TextView tv_main_date;

    @InjectView(R.id.tv_Time_sehri)
    TextView tv_Time_sehri;

    @InjectView(R.id.tv_iftari_time)
    TextView tv_iftari_time;

    @InjectView(R.id.tv_detail_info)
    TextView tv_detail_info;

    @InjectView(R.id.tv_chris_date)
    TextView tv_chris_date;


    final ColorDrawable cd = new ColorDrawable(Color.rgb(68, 74, 83));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Padauk.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        cd.setAlpha(0);
        setContentView(R.layout.activity_detail);
        ButterKnife.inject(this);

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_detail);



        initialize();

        Intent intent = getIntent();
       id= intent.getStringExtra("id");
        chrisdate=intent.getStringExtra("chris_date");
        detail=intent.getStringExtra("detail_info");
        maindate=intent.getStringExtra("main_date");
        sehritime=intent.getStringExtra("sehri_time");
        iftaritime=intent.getStringExtra("iftari_time");
       is_kadri= intent.getBooleanExtra("is_kaderi",true);

        tv_main_date.setText(maindate);
        tv_Time_sehri.setText(sehritime+" am");
        tv_iftari_time.setText(iftaritime+" pm");
        tv_chris_date.setText(chrisdate);

        tv_detail_info.setText((!detail.equals(""))?detail:"none");
        if(is_kadri){
            Toast.makeText(this,"Today kaderi",Toast.LENGTH_LONG).show();
        }




        scrollView_detail.setScrollViewCallbacks(this);

        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.detail_header_image_Height);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(scrollView_detail.getCurrentScrollY(), false, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_detail, menu);
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
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.main_color);
        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        ViewHelper.setTranslationY(headerimage_view, scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {
       //Toast.makeText(this,"on Down",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        //Toast.makeText(this,"Cancel",Toast.LENGTH_LONG).show();
    }

    private void initialize() {

        setSupportActionBar(toolbar);
        toolbar.setTitle("");



        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
            toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.main_color)));
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}
