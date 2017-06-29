package com.example.kupal.sunotes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.example.kupal.sunotes.fcm_notification.RecyclerFragment;
import com.example.kupal.sunotes.fcm_notification.StudentData;

import java.util.List;
import java.util.Map;

public class RecycleClass extends AppCompatActivity implements RecyclerFragment.OnCardItemClickedListener{

    ActionBar actionBar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;

    RecyclerFragment fragment1 = new RecyclerFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_class);

        RecyclerFragment fragment = new RecyclerFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() > 0) {

        } else {
            FragmentManager fm = getSupportFragmentManager();
            int count = fm.getBackStackEntryCount();
            for(int i = 0; i < count; ++i) {
                fm.popBackStackImmediate();
            }
            super.onBackPressed();
        }
    }

    @Override
    public void onCardItemClicked(StudentData sd) {
        List<Map<String,?>> list = sd.getStudentList();
        for(int i=0;i<list.size();i++){
            sd.removeItemFromServer(list.get(i));
        }

    }
}
