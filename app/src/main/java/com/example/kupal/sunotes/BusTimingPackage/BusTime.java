package com.example.kupal.sunotes.BusTimingPackage;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.kupal.sunotes.MapsActivity;
import com.example.kupal.sunotes.OtherUtilties.Fragment_DetailView;
import com.example.kupal.sunotes.R;

import java.util.ArrayList;
import java.util.List;

public class BusTime extends AppCompatActivity implements BusTimings.OnFragmentInteractionListener,
        BusFragment.OnFragmentInteractionListener {

    private Fragment currentFragment;
    private Spinner spinner1;
    private Button find;
    private Button from;
    private String selectRoute;
    private Cursor cursorStopNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_time);
        SQLiteOpenHelper busDatabaseHelper = new BusSQLiteHelper(this);
        final SQLiteDatabase db = busDatabaseHelper.getReadableDatabase();
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        find = (Button) findViewById(R.id.find);
        from = (Button) findViewById(R.id.from);
        List<String> labelFrom = new ArrayList<String>();
        cursorStopNames = db.query("STOPS_LIST", new String[]{"STOP_NAMES"}, null, null, null, null, null);
        cursorStopNames.moveToFirst();
        if (cursorStopNames.moveToFirst()) {
            do {
                labelFrom.add(cursorStopNames.getString(0));

            } while (cursorStopNames.moveToNext());
        }
        ArrayAdapter<String> myAdaptar = new ArrayAdapter<String>(this
                , R.layout.support_simple_spinner_dropdown_item, labelFrom);
        // Drop down layout style - list view with radio button
        myAdaptar.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner1.setAdapter(myAdaptar);

        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BusTime.this, MapsActivity.class);
                intent.putExtra("message", spinner1.getSelectedItem().toString());
                startActivity(intent);
            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectRoute = spinner1.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (savedInstanceState != null) {
            if (getSupportFragmentManager().getFragment(savedInstanceState, "current") != null) {
                currentFragment = getSupportFragmentManager().getFragment(savedInstanceState, "current");
            } else {
                currentFragment = BusTimings.newInstance(selectRoute, "");
            }
        } else {
            currentFragment = BusTimings.newInstance(selectRoute, "");
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.buscontainer, currentFragment).commit();

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFragment = BusFragment.newInstance(selectRoute, "");
                getSupportFragmentManager().beginTransaction().replace(R.id.buscontainer, currentFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onCardItemClicked(BusData b, int pos, String bName) {
        int newpos = b.findBus(bName);
        currentFragment = Fragment_DetailView.newInstance(b.getItem(newpos));
        getSupportFragmentManager().beginTransaction().replace(R.id.buscontainer, currentFragment).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() > 0) {

        } else {
            FragmentManager fm = getSupportFragmentManager();
            int count = fm.getBackStackEntryCount();
            for (int i = 0; i < count; ++i) {
                fm.popBackStackImmediate();
            }
            super.onBackPressed();
        }
    }
}
