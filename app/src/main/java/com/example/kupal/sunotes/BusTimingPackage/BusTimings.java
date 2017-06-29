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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kupal.sunotes.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class BusTimings extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private OnFragmentInteractionListener onCardItemClicked;
    Date trialTime = null;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    FromRecyclerViewAdapter fromRecyclerViewAdapter;
    String busTimeJames, busTimeWestcott, busTimeDrumlins, busTimeEastCampus, selectRoute;
    int countBus = 0;
    List<String> fromBusNameList;
    List<String> fromBusTimeList;
    BusData busData;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public BusTimings() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BusTimings newInstance(String param1, String param2) {
        BusTimings fragment = new BusTimings();
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
        final View rootview = inflater.inflate(R.layout.fragment_bus_timings, container, false);
        SQLiteOpenHelper busDatabaseHelper = new BusSQLiteHelper(getActivity());
        busData = new BusData();
        fromBusNameList = new ArrayList<String>();
        fromBusTimeList = new ArrayList<String>();
        final SQLiteDatabase db = busDatabaseHelper.getReadableDatabase();
        onCardItemClicked = (OnFragmentInteractionListener) getActivity();

        Calendar calendar = new GregorianCalendar();
        int currentHour = Integer.valueOf(calendar.get(Calendar.HOUR));
        int currentMinute = Integer.valueOf(calendar.get(Calendar.MINUTE));
        int currentAMPM = Integer.valueOf(calendar.get(Calendar.AM_PM));
        final int day = calendar.get(Calendar.DAY_OF_WEEK);
        final SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        String time = new SimpleDateFormat("HH:mm").format(calendar.getTime());
        final SimpleDateFormat parserAMPM = new SimpleDateFormat("hh:mm aa");

        try {
            trialTime = parser.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (currentAMPM != 0) {
            currentHour = currentHour + 12;
        }

        //////////////If weekend/////////////////////////////////////////
        if (day == 7 || day == 1) {

            Cursor cursorJames = db.query("FROMSU_JAMES_WEEKEND", new String[]{"START_TIME_FROM_CAMPUS_JAMES"}, null, null, null, null, null);
            if (cursorJames.moveToFirst()) {
                try {
                    while (parser.parse(cursorJames.getString(0)).before(trialTime) && !cursorJames.isLast())
                        cursorJames.moveToNext();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


            if (!cursorJames.getString(0).equals(null)) {
                countBus++;
                fromBusNameList.add("James Street");
                fromBusTimeList.add(cursorJames.getString(0));
                busTimeJames = "Next Bus James Street at : " + cursorJames.getString(0);
            } else {
                busTimeJames = "No James Street Bus...";
            }

            ///////////////////////////////////////// Drumlins bus code//////////////////////////////////////////////
            Cursor cursorDrumlins = db.query("FROMSU_DRUMLINS_WEEKENDS", new String[]{"START_TIME_SU_DRUMLINS"}, null, null, null, null, null);
            if (cursorDrumlins.moveToFirst()) {
                try {
                    while (parser.parse(cursorDrumlins.getString(0)).before(trialTime) && !cursorDrumlins.isLast())
                        cursorDrumlins.moveToNext();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (!cursorDrumlins.getString(0).equals(null)) {
                busTimeDrumlins = "Next Bus Drumlins  at : " + cursorDrumlins.getString(0);
                countBus++;
                fromBusNameList.add("Drumlins");
                fromBusTimeList.add(cursorDrumlins.getString(0));
            } else {
                busTimeDrumlins = "No Drumlins Bus...";
            }
            // closing connection
            if (countBus == 0) {
                countBus++;
                fromBusNameList.add("Damn! No Bus!");
                fromBusTimeList.add("Don't Sweat it! Call an Escort or a Taxi");
            }

            if (day == 1) {
                Cursor cursorEastCampus = db.query("EASTCAMPUS_START_SUNDAY", new String[]{"STARTSTOP_TIME_EASTCAMPUS"}, null, null, null, null, null);
                if (cursorEastCampus.moveToFirst()) {
                    try {
                        while (parser.parse(cursorEastCampus.getString(0)).before(trialTime) && !cursorEastCampus.isLast())
                            cursorEastCampus.moveToNext();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (!cursorEastCampus.getString(0).equals(null)) {
                    busTimeEastCampus = "Next Bus East Campus  at : " + cursorEastCampus.getString(0);
                    countBus++;
                    fromBusNameList.add("East Campus");
                    fromBusTimeList.add(cursorEastCampus.getString(0));
                } else {
                    busTimeEastCampus = "No East Campus Bus...";
                }
            }


            if (day == 7) {
                Cursor cursorEastCampus = db.query("EASTCAMPUS_START", new String[]{"STARTSTOP_TIME_EASTCAMPUS"}, null, null, null, null, null);
                if (cursorEastCampus.moveToFirst()) {
                    try {
                        while (parser.parse(cursorEastCampus.getString(0)).before(trialTime) && !cursorEastCampus.isLast())
                            cursorEastCampus.moveToNext();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }


                if (!cursorEastCampus.getString(0).equals(null)) {
                    busTimeEastCampus = "Next Bus East Campus  at : " + cursorEastCampus.getString(0);
                    countBus++;
                    fromBusNameList.add("East Campus");
                    fromBusTimeList.add(cursorEastCampus.getString(0));
                } else {
                    busTimeEastCampus = "No East Campus Bus...";
                }
            }
        } else {
            //// james street code
            Cursor cursorJames = db.query("FROMSUROUTE_JAMES", new String[]{"START_TIME_FROM_CAMPUS_JAMES"}, null, null, null, null, null);
            if (cursorJames.moveToFirst()) {
                try {
                    while (parser.parse(cursorJames.getString(0)).before(trialTime) && !cursorJames.isLast())
                        cursorJames.moveToNext();
                } catch (ParseException e) {
                    cursorJames.getString(0).equals(null);
                    e.printStackTrace();
                }
            }
            if (!cursorJames.getString(0).equals(null)) {
                countBus++;
                fromBusNameList.add("James Street");
                fromBusTimeList.add(cursorJames.getString(0));
                busTimeJames = "Next Bus James Street at : " + cursorJames.getString(0);

            } else {
                busTimeJames = "No James Street Bus...";
            }
            // closing connection
            ///////////////////////////////////////// Westcott bus code//////////////////////////////////////////////
            Cursor cursorWestcott = db.query("FROMSU_WESTCOTT", new String[]{"START_TIME_SU_WESTCOTT"}, null, null, null, null, null);
            if (cursorWestcott.moveToFirst()) {
                try {
                    while (parser.parse(cursorWestcott.getString(0)).before(trialTime) && !cursorWestcott.isLast())
                        cursorWestcott.moveToNext();
                } catch (ParseException e) {

                    cursorWestcott.getString(0).equals(null);
                    e.printStackTrace();
                }
            }

            if (!cursorWestcott.getString(0).equals(null)) {
                busTimeWestcott = "Next Bus Westcott 530 at : " + cursorWestcott.getString(0);
                countBus++;
                fromBusNameList.add("Westcott 530");
                fromBusTimeList.add(cursorWestcott.getString(0));
            } else {
                busTimeWestcott = "No Westcott 530 Bus...";
            }
            ///////////////////////////////////////// Drumlins bus code//////////////////////////////////////////////
            Cursor cursorDrumlins = db.query("FROMSU_DRUMLINS", new String[]{"START_TIME_SU_DRUMLINS"}, null, null, null, null, null);
            if (cursorDrumlins.moveToFirst()) {
                try {
                    while (parser.parse(cursorDrumlins.getString(0)).before(trialTime) && !cursorDrumlins.isLast())
                        cursorDrumlins.moveToNext();
                } catch (ParseException e) {
                    cursorDrumlins.getString(0).equals(null);
                    e.printStackTrace();
                }
            }

            if (!cursorDrumlins.getString(0).equals(null)) {
                busTimeDrumlins = "Next Bus Drumlins  at : " + cursorDrumlins.getString(0);
                countBus++;
                fromBusNameList.add("Drumlins");
                fromBusTimeList.add(cursorDrumlins.getString(0));
            } else {
                busTimeDrumlins = "No Drumlins Bus...";
            }
            // closing connection
            ///////////////////////////////////////// East Campus bus code//////////////////////////////////////////////
            Cursor cursorEastCampus = db.query("EASTCAMPUS_START", new String[]{"STARTSTOP_TIME_EASTCAMPUS"}, null, null, null, null, null);
            if (cursorEastCampus.moveToFirst()) {
                try {
                    while (parser.parse(cursorEastCampus.getString(0)).before(trialTime) && !cursorEastCampus.isLast())
                        cursorEastCampus.moveToNext();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (!cursorEastCampus.getString(0).equals(null)) {
                busTimeEastCampus = "Next Bus East Campus  at : " + cursorEastCampus.getString(0);
                countBus++;
                fromBusNameList.add("East Campus");
                fromBusTimeList.add(cursorEastCampus.getString(0));
            } else {
                busTimeDrumlins = "No East Campus Bus...";
            }
            // closing connection
            if (countBus == 0) {
                countBus++;
                fromBusNameList.add("Damn! No Bus!");
                fromBusTimeList.add("Don't Sweat it! Call an Escort or a Taxi");
            }
        }

        int size = fromBusNameList.size();
        recyclerView = (RecyclerView) rootview.findViewById(R.id.busRv);
        fromRecyclerViewAdapter = new FromRecyclerViewAdapter(getActivity(), fromBusNameList, fromBusTimeList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(fromRecyclerViewAdapter);
        adapterAnimation();

        fromRecyclerViewAdapter.setOnItemClickListener(new FromRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos, String busName) {//
                onCardItemClicked.onCardItemClicked(busData, pos, busName);
            }
        });
        db.close();
        return rootview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void adapterAnimation() {
        ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(this.fromRecyclerViewAdapter);
        alphaAdapter.setDuration(500);
        recyclerView.setAdapter(alphaAdapter);
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

        void onCardItemClicked(BusData b, int pos, String busName);//
    }

}
