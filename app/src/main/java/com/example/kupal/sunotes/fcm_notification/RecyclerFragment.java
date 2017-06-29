package com.example.kupal.sunotes.fcm_notification;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.kupal.sunotes.R;
import com.example.kupal.sunotes.RSSFeedDem.FeedItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class RecyclerFragment extends Fragment {

    public interface OnClicklistner {
        void showActivity(int position, HashMap movie);
    }

    private static String USERNAME = "priv.file", COURSENAME = "priv.file2";
    OnCardItemClickedListener onCardItemClickedListener ;
    private RecyclerView mRecyclerView;
    private StudentDataFireBaseAdaptar mAdapter;
    private LinearLayoutManager mLayoutManager;
    StudentData studentData;
    Button button;
    OnClicklistner mlistner;
    LruCache<String, Bitmap> bitmapLruCache;

    public RecyclerFragment() {
    }


    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
        setHasOptionsMenu(true);
        onCardItemClickedListener = (OnCardItemClickedListener)getActivity();
        button = (Button) rootView.findViewById(R.id.clear);
        if(getFileInfo(USERNAME).contains("Student")){
            button.setVisibility(View.GONE);
        }
        try {
            mlistner = (OnClicklistner) rootView.getContext();
        } catch (Exception e) {

        }
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Answers").getRef();
        mAdapter = new StudentDataFireBaseAdaptar(FeedItem.class,
                R.layout.custum_row_feed, StudentDataFireBaseAdaptar.MovieViewHolder.class,
                dref, getContext());
        studentData = new StudentData();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManagerWrapper(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        if (studentData.getSize() == 0) {
            studentData.setAdapter(mAdapter);
            studentData.setContext(getActivity());
            studentData.initializeDataFromCloud();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Clear", Toast.LENGTH_SHORT).show();
                onCardItemClickedListener.onCardItemClicked(studentData);
            }
        });
        return rootView;
    }

    public static RecyclerFragment newInstance() {

        Bundle args = new Bundle();
        RecyclerFragment fragment = new RecyclerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public class LinearLayoutManagerWrapper extends LinearLayoutManager {

        public LinearLayoutManagerWrapper(Context context) {
            super(context);
        }

        public LinearLayoutManagerWrapper(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public LinearLayoutManagerWrapper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }
    }

    public interface OnCardItemClickedListener {
        public void onCardItemClicked(StudentData sd);
    }

    public  void writeToFile(String val, String filename){
        FileOutputStream fos = null;
        try {
            fos = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(val.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFileInfo(String filename){
        FileInputStream fs = null;
        StringBuilder sb = new StringBuilder();
        try {
            fs = getActivity().openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(fs);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  String.valueOf(sb);
    }

}