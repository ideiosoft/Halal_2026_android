package com.dapl.halaladvisor.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dapl.halaladvisor.MainActivity;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.PrefUtils;
import com.dapl.halaladvisor.extra.SessionManagement;

import java.util.ArrayList;


public class WelcomeActivity extends AppCompatActivity {
 
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
     //private TextView[] dots;
    private int[] layouts;
    private TextView btnNext;
    ArrayList<ImageView> dots;
  TextView btnGetStarted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Log.d("sdfsfsdfsdfsdfsf", "onCreate: "+intent.getExtras());
        // Retrieve the data from the intent extras
        String notificationType = intent.getStringExtra("notification_type");


        Log.d("notificationType", "onCreate: "+notificationType);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
 
        setContentView(R.layout.activity_welcome_screen);
 
        viewPager = (ViewPager) findViewById(R.id.view_pager);
     //   dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnGetStarted = (TextView) findViewById(R.id.btnGetStarted);


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.layout_three,
                R.layout.layout_two,
                R.layout.layout_one,
                };
 
        // adding bottom dots
    //    addBottomDots(0);
 
        // making notification bar transparent
      //  changeStatusBarColor();
 
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

/*        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, RegistrationActivity.class));
                finish();            }
        });*/
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SessionManagement sessionManagement= new SessionManagement(WelcomeActivity.this);
                sessionManagement.setInfoStatus(true);


                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    startActivity(new Intent( WelcomeActivity.this, RegistrationActivity.class));
                    finish();
                }
            }
        });

        addDots();
    }

    public void addDots() {
        dots = new ArrayList<>();
        LinearLayout dotsLayout = (LinearLayout)findViewById(R.id.dots);
        for(int i = 0; i < 3; i++) {
            ImageView dot = new ImageView(this);
            dot.setMinimumHeight(20);
            dot.setMinimumWidth(20);
            dot.setImageDrawable(getResources().getDrawable(R.drawable.tab_indicator_default));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin=10;
            dotsLayout.addView(dot, params);
            dots.add(dot);
        }
        selectDot(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void selectDot(int idx) {
        Resources res = getResources();
        for(int i = 0; i < 3; i++) {
            int drawableId = (i==idx)?(R.drawable.tab_indicator_selected):(R.drawable.tab_indicator_default);
            Drawable drawable = res.getDrawable(drawableId);
            dots.get(i).setImageDrawable(drawable);
        }
    }
 /*
    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];
 
        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);
 
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }
 
        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }*/
 
    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }
 
    private void launchHomeScreen() {
         startActivity(new Intent(  WelcomeActivity.this, LogInActivity.class));
      //  finish();
    }
 
    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
 
        @Override
        public void onPageSelected(int position) {
         //   addBottomDots(position);
 
            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnGetStarted.setText("Get Started");
             //   btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnGetStarted.setText("Next");
                //btnSkip.setVisibility(View.VISIBLE);
            }
            if (position==0){
                btnGetStarted.setText("Next");
            }
        }
 
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
 
        }
 
        @Override
        public void onPageScrollStateChanged(int arg0) {
 
        }
    };
 
    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {   
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
 
    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
 
        public MyViewPagerAdapter() {
        }
 
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
 
            return view;
        }
 
        @Override
        public int getCount() {
            return layouts.length;
        }
 
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }
 
 
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}