package com.example.kupal.sunotes.BusTimingPackage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kupal.sunotes.R;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class BusFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private TextView textView;
    private GoogleApiClient client;
    String selectRoute = new String();
    Date trialTime = null;
    Date trialTime2 = null;
    List<String> toBusNameList = new ArrayList<String>();
    List<String> toBusTimeList = new ArrayList<String>();
    String newTimeJamesW1, newTimeJamesW2, newTimeDrumlinsW1, newTimeDrumlinsW2, newTimeSatEC1, newTimeSatEC2, newTimeSunEC1, newTimeSunEC2;
    String newTimeJames1, newTimeJames2, newTimeDrumlins1, newTimeDrumlins2, newTimeWestcott1, newTimeWestcott2, newTimeEC1, newTimeEC2;
    BusData busData = new BusData();
    FromRecyclerViewAdapter fromRecyclerViewAdapter;
    int day;
    private OnFragmentInteractionListener mListener;

    public BusFragment() {
        // Required empty public constructor
    }

    public static BusFragment newInstance(String param1, String param2) {
        BusFragment fragment = new BusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_from_bus_timings, container, false);
        SQLiteOpenHelper busDatabaseHelper = new BusSQLiteHelper(getActivity());
        final SQLiteDatabase db = busDatabaseHelper.getReadableDatabase();

        selectRoute = mParam1;
        mListener = (OnFragmentInteractionListener) getActivity();
        Calendar calendar = new GregorianCalendar();
        int currentHour = Integer.valueOf(calendar.get(Calendar.HOUR));
        int currentMinute = Integer.valueOf(calendar.get(Calendar.MINUTE));
        int currentAMPM = Integer.valueOf(calendar.get(Calendar.AM_PM));
        day = calendar.get(Calendar.DAY_OF_WEEK);
        final SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        String time = new SimpleDateFormat("HH:mm").format(calendar.getTime());

        try {
            trialTime = parser.parse(time); /////////////////made changes here shud be time
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar caltrialTime2 = new GregorianCalendar();
        caltrialTime2.add(Calendar.MINUTE, -45);
        String time2 = new SimpleDateFormat("HH:mm").format(caltrialTime2.getTime());

        try {
            trialTime2 = parser.parse(time2);        // made changes here should be time2
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (currentAMPM != 0) {
            currentHour = currentHour + 12;
        }

        if (day == 7 || day == 1) {
            toBusNameList.clear();
            toBusTimeList.clear();
            //////////////////////////////////JAMES STREET////////////////////////////////////////////////////////////////////////
            Cursor cursorStart = db.query("JAMES_START_WEEKEND", new String[]{"LODI_TIME"}, null, null, null, null, null);
            if (cursorStart.moveToFirst()) {
                do {                //labelFrom.add(cursorToSU.getString(0));
                } while (cursorStart.moveToNext());
            }
            //loop to find the next James Street bus time at the firt stop that is greater than current time
            if (cursorStart.moveToFirst()) {
                try {
                    while (parser.parse(cursorStart.getString(0)).before(trialTime) && !cursorStart.isLast())
                        cursorStart.moveToNext();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            // compare and find the user selced bus stop from spinner with the rows stored in cursorTOSU
            // and get the TIMEGAP from corresponding row element
            Cursor cursorToSU = db.query("TOSU_JAMES_WEEKEND", new String[]{"TO_STOP", "TIME_GAP"}, null, null, null, null, null);
            int count = 0;
            cursorToSU.moveToFirst();
            if (cursorToSU.moveToFirst()) {
                do {
                    count++;


                } while (cursorToSU.moveToNext());
            }

            int i = 0;
            if (cursorToSU.moveToFirst()) {

                do {
                    i++;

                    cursorToSU.moveToNext();
                } while (i < count && !cursorToSU.getString(0).equals(selectRoute));
            }
            i++;

            if (i > count) {// store the value of time gap found in an integere variable
                String busTime = "No James Street Bus From this Stop";
            } else {
                int addTime = Integer.valueOf(cursorToSU.getString(1));
                // convert and add the time to get the correct new Time for the bus at the stop
                // selected by user
                Date d = null;
                try {
                    d = parser.parse(cursorStart.getString(0));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                cal.add(Calendar.MINUTE, addTime);
                newTimeJamesW1 = parser.format(cal.getTime());
                // Display string for the EditText
                String busTime = "Next Bus James Street at : " + newTimeJamesW1;
                toBusNameList.add("James Street");
                toBusTimeList.add(newTimeJamesW1);
            }
            // closing connection
            cursorStart.close();
            cursorToSU.close();
            ////////////////////James Second Search////////////////////////////////////////

            Cursor cursorStart2 = db.query("JAMES_START_WEEKEND", new String[]{"LODI_TIME"}, null, null, null, null, null);
            if (cursorStart2.moveToFirst()) {
                do {                //labelFrom.add(cursorToSU.getString(0));

                } while (cursorStart2.moveToNext());
            }
            //loop to find the next James Street bus time at the first stop that is greater than current time
            if (cursorStart2.moveToFirst()) {
                try {
                    while (parser.parse(cursorStart2.getString(0)).before(trialTime2) && !cursorStart2.isLast())
                        cursorStart2.moveToNext();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            // compare and find the user selced bus stop from spinner with the rows stored in cursorTOSU
            // and get the TIMEGAP from corresponding row element
            Cursor cursorToSU2 = db.query("TOSU_JAMES_WEEKEND", new String[]{"TO_STOP", "TIME_GAP"}, null, null, null, null, null);
            int count2 = 0;
            cursorToSU2.moveToFirst();
            if (cursorToSU2.moveToFirst()) {
                do {
                    count2++;
                } while (cursorToSU2.moveToNext());
            }
            int i2 = 0;
            if (cursorToSU2.moveToFirst()) {

                do {
                    i2++;
                    cursorToSU2.moveToNext();
                } while (i2 < count2 && !cursorToSU2.getString(0).equals(selectRoute));
            }
            i2++;
            if (i2 > count2) {// store the value of time gap found in an integere variable
                String busTime2 = "No James Street Bus From this Stop";
            } else {
                int addTime2 = Integer.valueOf(cursorToSU2.getString(1));
                // convert and add the time to get the correct new Time for the bus at the stop
                // selected by user
                Date d = null;
                try {
                    d = parser.parse(cursorStart2.getString(0));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                cal.add(Calendar.MINUTE, addTime2);
                newTimeJamesW2 = parser.format(cal.getTime());
                // Display string for the EditText
                String busTime2 = "Next Bus James Street at : " + newTimeJamesW2;
                if (!newTimeJamesW2.equals(newTimeJamesW1)) {
                    toBusNameList.add("James Street");
                    toBusTimeList.add(newTimeJamesW2);
                }
            }
            // closing connection
            cursorStart2.close();
            cursorToSU2.close();
//////////////////////////////////////////////////////////////DRUMLINS/////////////////////////////////////////////////////////////////////
            Cursor cursorDStartTime = db.query("DRUMLINS_START_WEEKENDS", new String[]{"TOWARDS_START_TIME_DRUMLINS"}, null, null, null, null, null);
            if (cursorDStartTime.moveToFirst()) {
                do {                //labelFrom.add(cursorToSU.getString(0));

                } while (cursorDStartTime.moveToNext());
            }
            //loop to find the next James Street bus time at the first stop that is greater than current time
            if (cursorDStartTime.moveToFirst()) {
                try {
                    while (parser.parse(cursorDStartTime.getString(0)).before(trialTime) && !cursorDStartTime.isLast())
                        cursorDStartTime.moveToNext();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            // compare and find the user selced bus stop from spinner with the rows stored in cursorTOSU
            // and get the TIMEGAP from corresponding row element
            Cursor cursorDrumlins = db.query("TOSU_DRUMLINS_WEEKENDS", new String[]{"TOWARDS_STOPNAMES_DRUMLINS", "TIME_GAP_DRUMLINS"}, null, null, null, null, null);
            int countD = 0;
            cursorDrumlins.moveToFirst();
            if (cursorDrumlins.moveToFirst()) {
                do {
                    countD++;
                } while (cursorDrumlins.moveToNext());
            }

            int k = 0;
            if (cursorDrumlins.moveToFirst()) {

                do {
                    k++;
                    cursorDrumlins.moveToNext();
                } while (k < countD && !cursorDrumlins.getString(0).equals(selectRoute));
            }
            k++;

            if (k > countD) {
                String busTime = "No Drumlins bus at this Stop";
            } else {
                // store the value of time gap found in an integere variable
                int addTime = Integer.valueOf(cursorDrumlins.getString(1));
                // convert and add the time to get the correct new Time for the bus at the stop
                // selected by user
                Date d = null;
                try {
                    d = parser.parse(cursorDStartTime.getString(0));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                cal.add(Calendar.MINUTE, addTime);
                newTimeDrumlinsW1 = parser.format(cal.getTime());
                // Display string for the EditText
                String busTime = "Next Drumlins Bus at : " + newTimeDrumlinsW1;
                toBusNameList.add("Drumlins");
                toBusTimeList.add(newTimeDrumlinsW1);
            }
            cursorDStartTime.close();
            cursorDrumlins.close();
//////////////////////////////////////////DRUMLINS SECOND LOOPS//////////////////////////////////////////////////
            Cursor cursorDStartTime2 = db.query("DRUMLINS_START_WEEKENDS", new String[]{"TOWARDS_START_TIME_DRUMLINS"}, null, null, null, null, null);
            if (cursorDStartTime2.moveToFirst()) {
                do {                //labelFrom.add(cursorToSU.getString(0));

                } while (cursorDStartTime2.moveToNext());
            }
            //loop to find the next James Street bus time at the first stop that is greater than current time
            if (cursorDStartTime2.moveToFirst()) {
                try {
                    while (parser.parse(cursorDStartTime2.getString(0)).before(trialTime2) && !cursorDStartTime2.isLast())
                        cursorDStartTime2.moveToNext();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            // compare and find the user2 selced bus stop from spinner with the rows stored in cursorTOSU
            // and get the TIMEGAP from corresponding row element
            Cursor cursorDrumlins2 = db.query("TOSU_DRUMLINS_WEEKENDS", new String[]{"TOWARDS_STOPNAMES_DRUMLINS", "TIME_GAP_DRUMLINS"}, null, null, null, null, null);
            int countD2 = 0;
            cursorDrumlins2.moveToFirst();
            if (cursorDrumlins2.moveToFirst()) {
                do {
                    countD2++;
                } while (cursorDrumlins2.moveToNext());
            }
            int k2 = 0;
            if (cursorDrumlins2.moveToFirst()) {

                do {
                    k2++;
                    cursorDrumlins2.moveToNext();
                } while (k2 < countD2 && !cursorDrumlins2.getString(0).equals(selectRoute));
            }
            k2++;

            if (k2 > countD2) {
                String busTime = "No Drumlins bus at this Stop";
            } else {
                // store the value of time gap found in an integere variable
                int addTime = Integer.valueOf(cursorDrumlins2.getString(1));
                // convert and add the time to get the correct new Time for the bus at the stop
                // selected by user
                Date d = null;
                try {
                    d = parser.parse(cursorDStartTime2.getString(0));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                cal.add(Calendar.MINUTE, addTime);
                newTimeDrumlinsW2 = parser.format(cal.getTime());
                // Display string for the EditText
                String busTime = "Next Drumlins Bus at : " + newTimeDrumlinsW2;
                if (!newTimeDrumlinsW2.equals(newTimeDrumlins1)) {
                    toBusNameList.add("Drumlins");
                    toBusTimeList.add(newTimeDrumlinsW2);
                }
            }
            cursorDStartTime2.close();
            cursorDrumlins2.close();

            if (day == 1) {
                /////////////////////////////////////////////////EAST CAMPUS//////////////////////////////////////////////////////////////////////////////
                Cursor cursorStartTime = db.query("EASTCAMPUS_START_SUNDAY", new String[]{"STARTSTOP_TIME_EASTCAMPUS"}, null, null, null, null, null);
                if (cursorStartTime.moveToFirst()) {
                    do {                //labelFrom.add(cursorToSU.getString(0));

                    } while (cursorStartTime.moveToNext());
                }
                if (cursorStartTime.moveToFirst()) {
                    try {
                        while (parser.parse(cursorStartTime.getString(0)).before(trialTime) && !cursorStartTime.isLast())
                            cursorStartTime.moveToNext();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                // compare and find the user selced bus stop from spinner with the rows stored in cursorTOSU
                // and get the TIMEGAP from corresponding row element
                Cursor cursorEcampus = db.query("EASTCAMPUS_SUNDAY", new String[]{"TOWARDS_STOPNAMES_EASTCAMPUS", "TIME_GAP_EASTCAMPUS"}, null, null, null, null, null);
                int countE = 0;
                cursorEcampus.moveToFirst();
                if (cursorEcampus.moveToFirst()) {
                    do {
                        countE++;
                    } while (cursorEcampus.moveToNext());
                }

                int l = 0;
                if (cursorEcampus.moveToFirst()) {
                    do {
                        l++;
                        cursorEcampus.moveToNext();
                    } while (l < countE && !cursorEcampus.getString(0).equals(selectRoute));
                }
                l++;

                if (l > countE) {
                    String busTime = "No East Campus bus at this Stop";
                } else {
                    // store the value of time gap found in an integere variable
                    int addTime = Integer.valueOf(cursorEcampus.getString(1));
                    // convert and add the time to get the correct new Time for the bus at the stop
                    // selected by user
                    Date d = null;
                    try {
                        d = parser.parse(cursorStartTime.getString(0));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d);
                    cal.add(Calendar.MINUTE, addTime);
                    newTimeSunEC1 = parser.format(cal.getTime());

                    // Display string for the EditText
                    String busTime = "Next East Campus Bus at : " + newTimeSunEC1;
                    toBusNameList.add("East Campus");
                    toBusTimeList.add(newTimeSunEC1);
                }
                // closing connection
                cursorStartTime.close();
                cursorEcampus.close();
                /////////////EAST CAMPUS SECOND LOOP///////////////////////////////////
                Cursor cursorStartTime2 = db.query("EASTCAMPUS_START_SUNDAY", new String[]{"STARTSTOP_TIME_EASTCAMPUS"}, null, null, null, null, null);
                if (cursorStartTime2.moveToFirst()) {
                    do {                //labelFrom.add(cursorToSU.getString(0));

                    } while (cursorStartTime2.moveToNext());
                }
                //loop to find the next James Street bus time at the first stop that is greater than current time
                if (cursorStartTime2.moveToFirst()) {
                    try {
                        while (parser.parse(cursorStartTime2.getString(0)).before(trialTime2) && !cursorStartTime2.isLast())
                            cursorStartTime2.moveToNext();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                // compare and find the user selced bus stop from spinner with the rows stored in cursorTOSU
                // and get the TIMEGAP from corresponding row element
                Cursor cursorEcampus2 = db.query("EASTCAMPUS_SUNDAY", new String[]{"TOWARDS_STOPNAMES_EASTCAMPUS", "TIME_GAP_EASTCAMPUS"}, null, null, null, null, null);
                int countE2 = 0;
                cursorEcampus2.moveToFirst();
                if (cursorEcampus2.moveToFirst()) {
                    do {
                        countE2++;
                    } while (cursorEcampus2.moveToNext());
                }
                int l2 = 0;
                if (cursorEcampus2.moveToFirst()) {

                    do {
                        l2++;

                        cursorEcampus2.moveToNext();
                    } while (l2 < countE2 && !cursorEcampus2.getString(0).equals(selectRoute));
                }
                l2++;

                if (l2 > countE2) {
                    String busTime = "No East Campus bus at this Stop";
                } else {
                    // store the value of time gap found in an integere variable
                    int addTime = Integer.valueOf(cursorEcampus2.getString(1));
                    // convert and add the time to get the correct new Time for the bus at the stop
                    // selected by user
                    Date d = null;
                    try {
                        d = parser.parse(cursorStartTime2.getString(0));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d);
                    cal.add(Calendar.MINUTE, addTime);
                    newTimeSunEC2 = parser.format(cal.getTime());
                    // Display string for the EditText
                    String busTime = "Next East Campus Bus at : " + newTimeSunEC2;
                    if (!newTimeSunEC2.equals(newTimeSunEC2)) {
                        toBusNameList.add("East Campus");
                        toBusTimeList.add(newTimeSunEC2);
                    }
                }
                // closing connection
                cursorStartTime2.close();
                cursorEcampus2.close();
            }
            if (day == 7) {
                /////////////////////////////////////////////////EAST CAMPUS//////////////////////////////////////////////////////////////////////////////
                Cursor cursorStartTime = db.query("EASTCAMPUS_START", new String[]{"STARTSTOP_TIME_EASTCAMPUS"}, null, null, null, null, null);
                if (cursorStartTime.moveToFirst()) {
                    do {                //labelFrom.add(cursorToSU.getString(0));

                    } while (cursorStartTime.moveToNext());
                }
                //loop to find the next James Street bus time at the first stop that is greater than current time
                if (cursorStartTime.moveToFirst()) {
                    try {
                        while (parser.parse(cursorStartTime.getString(0)).before(trialTime) && !cursorStartTime.isLast())
                            cursorStartTime.moveToNext();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                // compare and find the user selced bus stop from spinner with the rows stored in cursorTOSU
                // and get the TIMEGAP from corresponding row element
                Cursor cursorEcampus = db.query("EASTCAMPUS", new String[]{"TOWARDS_STOPNAMES_EASTCAMPUS", "TIME_GAP_EASTCAMPUS"}, null, null, null, null, null);
                int countE = 0;
                cursorEcampus.moveToFirst();
                if (cursorEcampus.moveToFirst()) {
                    do {
                        countE++;
                    } while (cursorEcampus.moveToNext());
                }
                int l = 0;
                if (cursorEcampus.moveToFirst()) {
                    do {
                        l++;
                        cursorEcampus.moveToNext();
                    } while (l < countE && !cursorEcampus.getString(0).equals(selectRoute));
                }
                l++;

                if (l > countE) {
                    String busTime = "No East Campus bus at this Stop";
                } else {
                    // store the value of time gap found in an integere variable
                    int addTime = Integer.valueOf(cursorEcampus.getString(1));
                    // convert and add the time to get the correct new Time for the bus at the stop
                    // selected by user
                    Date d = null;
                    try {
                        d = parser.parse(cursorStartTime.getString(0));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d);
                    cal.add(Calendar.MINUTE, addTime);
                    newTimeSatEC1 = parser.format(cal.getTime());
                    // Display string for the EditText
                    String busTime = "Next East Campus Bus at : " + newTimeSatEC1;
                    toBusNameList.add("East Campus");
                    toBusTimeList.add(newTimeSatEC1);
                }
                // closing connection
                cursorStartTime.close();
                cursorEcampus.close();
                /////////////EAST CAMPUS SECOND LOOP///////////////////////////////////
                Cursor cursorStartTime2 = db.query("EASTCAMPUS_START", new String[]{"STARTSTOP_TIME_EASTCAMPUS"}, null, null, null, null, null);
                if (cursorStartTime2.moveToFirst()) {
                    do {                //labelFrom.add(cursorToSU.getString(0));

                    } while (cursorStartTime2.moveToNext());
                }
                //loop to find the next James Street bus time at the first stop that is greater than current time
                if (cursorStartTime2.moveToFirst()) {
                    try {
                        while (parser.parse(cursorStartTime2.getString(0)).before(trialTime2) && !cursorStartTime2.isLast())
                            cursorStartTime2.moveToNext();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                // compare and find the user selced bus stop from spinner with the rows stored in cursorTOSU
                // and get the TIMEGAP from corresponding row element
                Cursor cursorEcampus2 = db.query("EASTCAMPUS", new String[]{"TOWARDS_STOPNAMES_EASTCAMPUS", "TIME_GAP_EASTCAMPUS"}, null, null, null, null, null);
                int countE2 = 0;
                cursorEcampus2.moveToFirst();
                if (cursorEcampus2.moveToFirst()) {
                    do {
                        countE2++;
                    } while (cursorEcampus2.moveToNext());
                }
                int l2 = 0;
                if (cursorEcampus2.moveToFirst()) {
                    do {
                        l2++;
                        cursorEcampus2.moveToNext();
                    } while (l2 < countE2 && !cursorEcampus2.getString(0).equals(selectRoute));
                }
                l2++;
                if (l2 > countE2) {
                    String busTime = "No East Campus bus at this Stop";
                } else {
                    // store the value of time gap found in an integere variable
                    int addTime = Integer.valueOf(cursorEcampus2.getString(1));
                    // convert and add the time to get the correct new Time for the bus at the stop
                    // selected by user
                    Date d = null;
                    try {
                        d = parser.parse(cursorStartTime2.getString(0));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d);
                    cal.add(Calendar.MINUTE, addTime);
                    newTimeSatEC2 = parser.format(cal.getTime());
                    // Display string for the EditText
                    String busTime = "Next East Campus Bus at : " + newTimeSatEC2;
                    if (!newTimeSatEC2.equals(newTimeSatEC1)) {
                        toBusNameList.add("East Campus");
                        toBusTimeList.add(newTimeSatEC2);
                    }
                }
                // closing connection
                cursorStartTime2.close();
                cursorEcampus2.close();
            }

            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.fromBusRv);
            ToRecyclerViewAdapter toRecyclerViewAdapter = new ToRecyclerViewAdapter(getContext(), toBusNameList, toBusTimeList);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(toRecyclerViewAdapter);
            toRecyclerViewAdapter.setOnItemClickListener(new ToRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, String bName) {

                }
            });
        } else {
            toBusNameList.clear();
            toBusTimeList.clear();
//////////////////////////////////////weekdays//////////////////////////////////////
            //  else {
//////////////////////////////////////////////////JAMES STREET////////////////////////////////////////////////////////////////////////
            Cursor cursorStart = db.query("JAMES_START", new String[]{"LODI_TIME"}, null, null, null, null, null);
            if (cursorStart.moveToFirst()) {
                do {                //labelFrom.add(cursorToSU.getString(0));

                } while (cursorStart.moveToNext());
            }
            //loop to find the next James Street bus time at the first stop that is greater than current time
            if (cursorStart.moveToFirst()) {
                try {
                    while (parser.parse(cursorStart.getString(0)).before(trialTime) && !cursorStart.isLast())
                        cursorStart.moveToNext();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            // compare and find the user selced bus stop from spinner with the rows stored in cursorTOSU
            // and get the TIMEGAP from corresponding row element
            Cursor cursorToSU = db.query("TOSUROUTE_JAMES", new String[]{"TO_STOP", "TIME_GAP"}, null, null, null, null, null);
            int count = 0;
            cursorToSU.moveToFirst();
            if (cursorToSU.moveToFirst()) {
                do {
                    count++;
                } while (cursorToSU.moveToNext());
            }
            int i = 0;
            if (cursorToSU.moveToFirst()) {

                do {
                    i++;
                    cursorToSU.moveToNext();
                } while (i < count && !cursorToSU.getString(0).equals(selectRoute));
            }
            i++;
            if (i > count) {// store the value of time gap found in an integere variable
                String busTime = "No James Street Bus From this Stop";
            } else {
                int addTime = Integer.valueOf(cursorToSU.getString(1));
                // convert and add the time to get the correct new Time for the bus at the stop
                // selected by user
                Date d = null;
                try {
                    d = parser.parse(cursorStart.getString(0));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                cal.add(Calendar.MINUTE, addTime);
                newTimeJames1 = parser.format(cal.getTime());
                // Display string for the EditText
                String busTime = "Next Bus James Street at : " + newTimeJames1;
                toBusNameList.add("James Street");
                toBusTimeList.add(newTimeJames1);
            }
            // closing connection
            cursorStart.close();
            cursorToSU.close();
///////////////////////////////James Second Loop////////////////////////
            Cursor cursorStart2 = db.query("JAMES_START", new String[]{"LODI_TIME"}, null, null, null, null, null);
            if (cursorStart2.moveToFirst()) {
                do {                //labelFrom.add(cursorToSU.getString(0));

                } while (cursorStart2.moveToNext());
            }
            //loop to find the next James Street bus time at the first stop that is greater than current time
            if (cursorStart2.moveToFirst()) {
                try {
                    while (parser.parse(cursorStart2.getString(0)).before(trialTime2) && !cursorStart2.isLast())
                        cursorStart2.moveToNext();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            // compare and find the user selced bus stop from spinner with the rows stored in cursorTOSU
            // and get the TIMEGAP from corresponding row element
            Cursor cursorToSU2 = db.query("TOSUROUTE_JAMES", new String[]{"TO_STOP", "TIME_GAP"}, null, null, null, null, null);
            int count2 = 0;
            cursorToSU2.moveToFirst();
            if (cursorToSU2.moveToFirst()) {
                do {
                    count2++;
                } while (cursorToSU2.moveToNext());
            }
            int i2 = 0;
            if (cursorToSU2.moveToFirst()) {
                do {
                    i2++;
                    cursorToSU2.moveToNext();
                } while (i2 < count2 && !cursorToSU2.getString(0).equals(selectRoute));
            }
            i2++;
            if (i2 > count2) {// store the value of time gap found in an integere variable
                String busTime = "No James Street Bus From this Stop";
            } else {
                int addTime = Integer.valueOf(cursorToSU2.getString(1));
                // convert and add the time to get the correct new Time for the bus at the stop
                // selected by user
                Date d = null;
                try {
                    d = parser.parse(cursorStart2.getString(0));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                cal.add(Calendar.MINUTE, addTime);
                newTimeJames2 = parser.format(cal.getTime());
                // Display string for the EditText
                String busTime = "Next Bus James Street at : " + newTimeJames2;
                if (!newTimeJames2.equals(newTimeJames1)) {
                    toBusNameList.add("James Street");
                    toBusTimeList.add(newTimeJames2);
                }
            }
            // closing connection
            cursorStart2.close();
            cursorToSU2.close();
//////////////////////////////////////////////////WESTCOTT 530//////////////////////////////////////////////////////////////////////////////
            Cursor cursorWStartTime = db.query("WESTCOTT_START", new String[]{"TOWARDS_START_TIME_WESTCOTT"}, null, null, null, null, null);
            if (cursorWStartTime.moveToFirst()) {
                do {                //labelFrom.add(cursorToSU.getString(0));

                } while (cursorWStartTime.moveToNext());
            }
            //loop to find the next James Street bus time at the first stop that is greater than current time
            if (cursorWStartTime.moveToFirst()) {
                try {
                    while (parser.parse(cursorWStartTime.getString(0)).before(trialTime) && !cursorWStartTime.isLast())
                        cursorWStartTime.moveToNext();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            // compare and find the user selced bus stop from spinner with the rows stored in cursorTOSU
            // and get the TIMEGAP from corresponding row element
            Cursor cursorToSUWestcott = db.query("TOSU_WESTCOTT", new String[]{"TOWARDS_STOPNAMES_WESTCOTT", "TIME_GAP_WESTCOTT"}, null, null, null, null, null);
            int countW = 0;
            cursorToSUWestcott.moveToFirst();
            if (cursorToSUWestcott.moveToFirst()) {
                do {
                    countW++;
                } while (cursorToSUWestcott.moveToNext());
            }
            int j = 0;
            if (cursorToSUWestcott.moveToFirst()) {

                do {
                    j++;
                    cursorToSUWestcott.moveToNext();
                }
                while (j < countW && !cursorToSUWestcott.getString(0).equals(selectRoute));
            }
            j++;
            if (j > countW) {
                String busTime = "No Westcott 530 bus at this Stop";
            } else {
                // store the value of time gap found in an integere variable
                int addTime = Integer.valueOf(cursorToSUWestcott.getString(1));
                // convert and add the time to get the correct new Time for the bus at the stop
                // selected by user
                Date d = null;
                try {
                    d = parser.parse(cursorWStartTime.getString(0));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                cal.add(Calendar.MINUTE, addTime);
                newTimeWestcott1 = parser.format(cal.getTime());
                // Display string for the EditText
                String busTime = "Next Bus Westcott Street at : " + newTimeWestcott1;
                toBusNameList.add("Westcott 530");
                toBusTimeList.add(newTimeWestcott1);
            }
            cursorWStartTime.close();
            cursorToSUWestcott.close();

/////////////////////////Westcott Second Loop///////////////////////////////
            Cursor cursorWStartTime2 = db.query("WESTCOTT_START", new String[]{"TOWARDS_START_TIME_WESTCOTT"}, null, null, null, null, null);
            if (cursorWStartTime2.moveToFirst()) {
                do {                //labelFrom.add(cursorToSU.getString(0));

                } while (cursorWStartTime2.moveToNext());
            }
            //loop to find the next James Street bus time at the first stop that is greater than current time
            if (cursorWStartTime2.moveToFirst()) {
                try {
                    while (parser.parse(cursorWStartTime2.getString(0)).before(trialTime2) && !cursorWStartTime2.isLast())
                        cursorWStartTime2.moveToNext();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            // compare and find the user selced bus stop from spinner with the rows stored in cursorTOSU
            // and get the TIMEGAP from corresponding row element
            Cursor cursorToSUWestcott2 = db.query("TOSU_WESTCOTT", new String[]{"TOWARDS_STOPNAMES_WESTCOTT", "TIME_GAP_WESTCOTT"}, null, null, null, null, null);
            int countW2 = 0;
            cursorToSUWestcott2.moveToFirst();
            if (cursorToSUWestcott2.moveToFirst()) {
                do {
                    countW2++;
                } while (cursorToSUWestcott2.moveToNext());
            }
            int j2 = 0;
            if (cursorToSUWestcott2.moveToFirst()) {

                do {
                    j2++;

                    cursorToSUWestcott2.moveToNext();
                }
                while (j2 < countW2 && !cursorToSUWestcott2.getString(0).equals(selectRoute));
            }
            j2++;


            if (j2 > countW2) {
                String busTime = "No Westcott 530 bus at this Stop";
            } else {
                // store the value of time gap found in an integere variable
                int addTime = Integer.valueOf(cursorToSUWestcott2.getString(1));


                // convert and add the time to get the correct new Time for the bus at the stop
                // selected by user
                Date d = null;
                try {
                    d = parser.parse(cursorWStartTime2.getString(0));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                cal.add(Calendar.MINUTE, addTime);
                newTimeWestcott2 = parser.format(cal.getTime());

                // Display string for the EditText
                String busTime = "Next Bus Westcott Street at : " + newTimeWestcott2;

                if (!newTimeWestcott2.equals(newTimeWestcott1)) {
                    toBusNameList.add("Westcott 530");
                    toBusTimeList.add(newTimeWestcott2);
                }
            }


            cursorWStartTime2.close();
            cursorToSUWestcott2.close();


//////////////////////////////////////////////////////////////DRUMLINS/////////////////////////////////////////////////////////////////////
            Cursor cursorDStartTime = db.query("DRUMLINS_START", new String[]{"TOWARDS_START_TIME_DRUMLINS"}, null, null, null, null, null);
            if (cursorDStartTime.moveToFirst()) {
                do {                //labelFrom.add(cursorToSU.getString(0));

                } while (cursorDStartTime.moveToNext());
            }


            //loop to find the next James Street bus time at the first stop that is greater than current time
            if (cursorDStartTime.moveToFirst()) {
                try {
                    while (parser.parse(cursorDStartTime.getString(0)).before(trialTime) && !cursorDStartTime.isLast())
                        cursorDStartTime.moveToNext();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


            // compare and find the user selced bus stop from spinner with the rows stored in cursorTOSU
            // and get the TIMEGAP from corresponding row element

            Cursor cursorDrumlins = db.query("TOSU_DRUMLINS", new String[]{"TOWARDS_STOPNAMES_DRUMLINS", "TIME_GAP_DRUMLINS"}, null, null, null, null, null);

            int countD = 0;
            cursorDrumlins.moveToFirst();
            if (cursorDrumlins.moveToFirst()) {
                do {
                    countD++;


                } while (cursorDrumlins.moveToNext());
            }
            int k = 0;
            if (cursorDrumlins.moveToFirst()) {

                do {
                    k++;

                    cursorDrumlins.moveToNext();
                } while (k < countD && !cursorDrumlins.getString(0).equals(selectRoute));
            }
            k++;


            if (k > countD) {
                String busTime = "No Drumlins bus at this Stop";
            } else {
                // store the value of time gap found in an integere variable
                int addTime = Integer.valueOf(cursorDrumlins.getString(1));


                // convert and add the time to get the correct new Time for the bus at the stop
                // selected by user
                Date d = null;
                try {
                    d = parser.parse(cursorDStartTime.getString(0));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                cal.add(Calendar.MINUTE, addTime);
                newTimeDrumlins1 = parser.format(cal.getTime());

                // Display string for the EditText
                String busTime = "Next Drumlins Bus at : " + newTimeDrumlins1;
                toBusNameList.add("Drumlins");
                toBusTimeList.add(newTimeDrumlins1);
            }


            cursorDStartTime.close();
            cursorDrumlins.close();

            ///SECOND LOOP DRUNLINS////////////////////////////////////////////////

            Cursor cursorDStartTime2 = db.query("DRUMLINS_START", new String[]{"TOWARDS_START_TIME_DRUMLINS"}, null, null, null, null, null);
            if (cursorDStartTime2.moveToFirst()) {
                do {                //labelFrom.add(cursorToSU.getString(0));
                    Log.d("Start Times :", cursorDStartTime2.getString(0));
                } while (cursorDStartTime2.moveToNext());
            }


            //loop to find the next James Street bus time at the first stop that is greater than current time
            if (cursorDStartTime2.moveToFirst()) {
                try {
                    while (parser.parse(cursorDStartTime2.getString(0)).before(trialTime2) && !cursorDStartTime2.isLast())
                        cursorDStartTime2.moveToNext();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            Cursor cursorDrumlins2 = db.query("TOSU_DRUMLINS", new String[]{"TOWARDS_STOPNAMES_DRUMLINS", "TIME_GAP_DRUMLINS"}, null, null, null, null, null);
            int countD2 = 0;
            cursorDrumlins2.moveToFirst();
            if (cursorDrumlins2.moveToFirst()) {
                do {
                    countD2++;


                } while (cursorDrumlins2.moveToNext());
            }

            int k2 = 0;
            if (cursorDrumlins2.moveToFirst()) {

                do {
                    k2++;

                    cursorDrumlins2.moveToNext();
                } while (k2 < countD2 && !cursorDrumlins2.getString(0).equals(selectRoute));
            }
            k2++;


            if (k2 > countD2) {
                String busTime = "No Drumlins bus at this Stop";
            } else {
                // store the value of time gap found in an integere variable
                int addTime = Integer.valueOf(cursorDrumlins2.getString(1));
                Date d = null;
                try {
                    d = parser.parse(cursorDStartTime2.getString(0));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                cal.add(Calendar.MINUTE, addTime);
                newTimeDrumlins2 = parser.format(cal.getTime());

                // Display string for the EditText
                String busTime = "Next Drumlins Bus at : " + newTimeDrumlins2;

                if (!newTimeDrumlins2.equals(newTimeDrumlins1)) {
                    toBusNameList.add("Drumlins");
                    toBusTimeList.add(newTimeDrumlins2);
                }
            }
            cursorDStartTime.close();
            cursorDrumlins.close();


/////////////////////////////////////////////////EAST CAMPUS//////////////////////////////////////////////////////////////////////////////
            Cursor cursorStartTime = db.query("EASTCAMPUS_START", new String[]{"STARTSTOP_TIME_EASTCAMPUS"}, null, null, null, null, null);
            if (cursorStartTime.moveToFirst()) {
                do {                //labelFrom.add(cursorToSU.getString(0));

                } while (cursorStartTime.moveToNext());
            }
            //loop to find the next James Street bus time at the first stop that is greater than current time
            if (cursorStartTime.moveToFirst()) {
                try {
                    while (parser.parse(cursorStartTime.getString(0)).before(trialTime) && !cursorStartTime.isLast())
                        cursorStartTime.moveToNext();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            // compare and find the user selced bus stop from spinner with the rows stored in cursorTOSU
            // and get the TIMEGAP from corresponding row element

            Cursor cursorEcampus = db.query("EASTCAMPUS", new String[]{"TOWARDS_STOPNAMES_EASTCAMPUS", "TIME_GAP_EASTCAMPUS"}, null, null, null, null, null);

            int countE = 0;
            cursorEcampus.moveToFirst();
            if (cursorEcampus.moveToFirst()) {
                do {
                    countE++;


                } while (cursorEcampus.moveToNext());
            }

            int l = 0;
            if (cursorEcampus.moveToFirst()) {

                do {
                    l++;

                    cursorEcampus.moveToNext();
                } while (l < countE && !cursorEcampus.getString(0).equals(selectRoute));
            }
            l++;

            if (l > countE) {
                String busTime = "No East Campus bus at this Stop";
            } else {
                // store the value of time gap found in an integere variable
                int addTime = Integer.valueOf(cursorEcampus.getString(1));


                // convert and add the time to get the correct new Time for the bus at the stop
                // selected by user
                Date d = null;
                try {
                    d = parser.parse(cursorStartTime.getString(0));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                cal.add(Calendar.MINUTE, addTime);
                newTimeEC1 = parser.format(cal.getTime());

                // Display string for the EditText
                String busTime = "Next East Campus Bus at : " + newTimeEC1;
                toBusNameList.add("East Campus");
                toBusTimeList.add(newTimeEC1);
            }


            // closing connection

            cursorStartTime.close();
            cursorEcampus.close();


            /////////////EAST CAMPUS SECOND LOOP///////////////////////////////////
            Cursor cursorStartTime2 = db.query("EASTCAMPUS_START", new String[]{"STARTSTOP_TIME_EASTCAMPUS"}, null, null, null, null, null);
            if (cursorStartTime2.moveToFirst()) {
                do {                //labelFrom.add(cursorToSU.getString(0));

                } while (cursorStartTime2.moveToNext());
            }


            //loop to find the next James Street bus time at the first stop that is greater than current time
            if (cursorStartTime2.moveToFirst()) {
                try {
                    while (parser.parse(cursorStartTime2.getString(0)).before(trialTime2) && !cursorStartTime2.isLast())
                        cursorStartTime2.moveToNext();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


            // compare and find the user selced bus stop from spinner with the rows stored in cursorTOSU
            // and get the TIMEGAP from corresponding row element

            Cursor cursorEcampus2 = db.query("EASTCAMPUS", new String[]{"TOWARDS_STOPNAMES_EASTCAMPUS", "TIME_GAP_EASTCAMPUS"}, null, null, null, null, null);

            int countE2 = 0;
            cursorEcampus2.moveToFirst();
            if (cursorEcampus2.moveToFirst()) {
                do {
                    countE2++;


                } while (cursorEcampus2.moveToNext());
            }

            int l2 = 0;
            if (cursorEcampus2.moveToFirst()) {

                do {
                    l2++;

                    cursorEcampus2.moveToNext();
                } while (l2 < countE2 && !cursorEcampus2.getString(0).equals(selectRoute));
            }
            l2++;


            if (l2 > countE2) {
                String busTime = "No East Campus bus at this Stop";
            } else {
                // store the value of time gap found in an integere variable
                int addTime = Integer.valueOf(cursorEcampus2.getString(1));


                // convert and add the time to get the correct new Time for the bus at the stop
                // selected by user
                Date d = null;
                try {
                    d = parser.parse(cursorStartTime2.getString(0));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                cal.add(Calendar.MINUTE, addTime);
                newTimeEC2 = parser.format(cal.getTime());
                // Display string for the EditText
                String busTime = "Next East Campus Bus at : " + newTimeEC2;
                if (!newTimeEC2.equals(newTimeEC1)) {
                    toBusNameList.add("East Campus");
                    toBusTimeList.add(newTimeEC2);
                }
            }
            cursorStartTime2.close();
            cursorEcampus2.close();
        }

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.fromBusRv);
        fromRecyclerViewAdapter = new FromRecyclerViewAdapter(getActivity(), toBusNameList, toBusTimeList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(fromRecyclerViewAdapter);
        fromRecyclerViewAdapter.setOnItemClickListener(new FromRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String bName) {
                Toast.makeText(getActivity(), "this is a test", Toast.LENGTH_LONG).show();
            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
