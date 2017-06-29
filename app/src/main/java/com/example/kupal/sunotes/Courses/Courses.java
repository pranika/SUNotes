package com.example.kupal.sunotes.Courses;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.kupal.sunotes.MainActivityPackage.frontPage;
import com.example.kupal.sunotes.R;

import java.util.HashMap;

public class Courses extends AppCompatActivity implements CourseOne.OnFragmentInteractionListener, frontPage.OnfrontPageInteractionListener, CourseOne.OnCardItemClickedListener, CourseTwo.OnSecondCardItemClickedListener{//

    private Fragment currentFragment;
    private MyPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                Toast.makeText(Courses.this,
                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFrontPageInteraction(Uri uri) {

    }

    @Override
    public void onCardItemClicked(HashMap<String, ?> movie, View sr) {
        Toast.makeText(this, String.valueOf(String.valueOf(movie.get("description"))), Toast.LENGTH_SHORT).show();
        if(movie!=null){
            Intent intent = new Intent(this,Answer_activity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onSecondCardItemClicked(HashMap<String, ?> movie, View sr) {
        Toast.makeText(this, "create a detail fragment here", Toast.LENGTH_SHORT).show();
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return CourseOne.newInstance("one", "Page # 1");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return CourseTwo.newInstance("two", "Page # 2");
                default:
                    return null;
            }
        }

        CharSequence getCourseName(int value){
            String val = "";
            switch (value) {
                case 1: value = 1;
                    val = "CSE 681";
                    break;
                default:value = 2;
                    val = "CIS 600";
                    break;
            }
            return val;
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return getCourseName(position);
        }

    }
}
