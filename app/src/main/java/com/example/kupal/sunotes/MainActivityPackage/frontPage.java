package com.example.kupal.sunotes.MainActivityPackage;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ProgressBar;

import com.example.kupal.sunotes.R;
import com.example.kupal.sunotes.RSSFeedDem.FeedData;
import com.example.kupal.sunotes.RSSFeedDem.FeedItem;
import com.example.kupal.sunotes.RSSFeedDem.VerticalSpace;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;

public class frontPage extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String url = "URL";
    String userName = "Kunal Paliwal";
    private Button postButton;
    private EditText postText;
    FeedData feedData;
    ProgressBar progressBar;
    LinearLayoutManager lm;
    MyFirebaseRecylerAdapter myFirebaseRecylerAdapter;
    OnCardItemClickedListener onCardItemClickedListener ;
    RecyclerView recyclerView;
    private String mParam1;
    private String mParam2;
    private SharedPreferences sharedPreferences;
    private OnfrontPageInteractionListener mListener;

    public frontPage() {}

    public static frontPage newInstance(String param1, String param2) {
        frontPage fragment = new frontPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_front_page,container,false);
        mListener = (OnfrontPageInteractionListener) rootview.getContext();
        postButton = (Button) rootview.findViewById(R.id.post);
        postText = (EditText) rootview.findViewById(R.id.postText);
        progressBar = (ProgressBar) rootview.findViewById(R.id.progress_bar);
        onCardItemClickedListener = (OnCardItemClickedListener) getContext();
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.NOTIFY_PREF), Context.MODE_PRIVATE);

        DatabaseReference childRef = FirebaseDatabase.getInstance().getReference().child("feed").getRef();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        myFirebaseRecylerAdapter = new MyFirebaseRecylerAdapter(FeedItem.class, R.layout.custum_row_feed, MyFirebaseRecylerAdapter.FeedViewHolder.class
                ,childRef, getActivity());
        feedData = new FeedData();
        recyclerView= (RecyclerView) rootview.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new VerticalSpace(50));
        recyclerView.setAdapter(myFirebaseRecylerAdapter);
        if (feedData.getSize() == 0) {
            feedData.setAdapter(myFirebaseRecylerAdapter);
            feedData.setContext(getActivity());
            feedData.initializeDataFromCloud();
        }

        final Handler handler = new Handler();
        new Thread(new Runnable() {
            public void run() {
                try{
                    Thread.sleep(2000);
                }
                catch (Exception e) { } // Just catch the InterruptedException

                // Now we use the Handler to post back to the main thread
                handler.post(new Runnable() {
                    public void run() {
                        // Set the View's visibility back on the main UI Thread
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }).start();

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("feed");
                FirebaseAuth mauth = FirebaseAuth.getInstance();
                SharedPreferences sharedPreferences = getActivity().
                        getSharedPreferences(getString(R.string.NOTIFY_PREF), Context.MODE_PRIVATE);
                Date timestamp = new Date();
                Calendar calendar = new GregorianCalendar();
                final int day = calendar.get(Calendar.DAY_OF_WEEK);
                final SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
                String time = new SimpleDateFormat("HH:mm").format(calendar.getTime());
                final String userid = mauth.getCurrentUser().getUid();
                String value  = String.valueOf(postText.getText());
                final DatabaseReference userdb = ref.child(String.valueOf(Integer.MAX_VALUE - timestamp.getTime() % 1000000000));
                userdb.child("id").setValue(String.valueOf(Integer.MAX_VALUE - timestamp.getTime() % 1000000000));
                userdb.child("description").setValue(value);
                userdb.child("link").setValue(userid);
                userdb.child("primaryID").setValue("PRIMARYID");
                userdb.child("pubDate").setValue(time + ", " + getDay(day));
                userdb.child("thumbnailUrl").setValue("url");
                final String username = sharedPreferences.getString("name","");
                userdb.child("title").setValue("Posted by: " + username);
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
                Query q= userRef.orderByKey().equalTo(userid);
                q.addValueEventListener(new ValueEventListener() {
                    int valuse = 0;
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            HashMap<String,?> movie = (HashMap<String,?>)dataSnapshot.getValue();
                            HashMap val1 =  (HashMap)movie.get(userid);
                            String val = String.valueOf(val1.get("ImageUrl"));
                            userdb.child("title").setValue("Posted by: " + String.valueOf(val1.get("Name")));
                            userdb.child("thumbnailUrl").setValue(val);
                            valuse++;
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
        });

        myFirebaseRecylerAdapter.setOnItemClickListener(new MyFirebaseRecylerAdapter.RecyclerItemClickListener() {

            @Override
            public void onItemClick(View v, final int position) {
                if(feedData.getItem(position) != null){
                    onCardItemClickedListener.onCardItemClicked(feedData.getItem(position));
                }
            }

            @Override
            public void onOverFlowButtonClick(View view, final int position) {
                PopupMenu popup = new PopupMenu(getActivity(), view);
                popup.setOnMenuItemClickListener(
                        new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                HashMap movie;
                                int id = item.getItemId();
                                switch (id) {
                                    case R.id.delete:
                                        movie = (HashMap) ((HashMap) feedData.getItem(position));
                                        feedData.removeItemFromServer(movie);
                                        return true;
                                    default:
                                        return false;
                                }}
                        });
                MenuInflater menuInflater = popup.getMenuInflater();
                menuInflater.inflate(R.menu.contextual_action_bar_menu, popup.getMenu());
                popup.show();
            }
        });

        itemAnimator();
        adapterAnimation();
        return rootview;
    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFrontPageInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnfrontPageInteractionListener) {
            mListener = (OnfrontPageInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    String getDay(int i){
        String dayString = "";
        switch (i){
            case 1: i=1;
                dayString = "Sunday";
                break;
            case 2: i=1;
                dayString = "Monday";
                break;
            case 3: i=1;
                dayString = "Tuesday";
                break;
            case 4: i=1;
                dayString = "Wednesday";
                break;
            case 5: i=1;
                dayString = "Thursday";
                break;
            case 6: i=1;
                dayString = "Friday";
                break;
            case 7: i=1;
                dayString = "Saturday";
                break;
            default:dayString = "DayTime";
                break;
        }
        return  dayString;
    }

    private void itemAnimator() {
        SlideInRightAnimator animator = new SlideInRightAnimator();
        animator.setInterpolator(new OvershootInterpolator());
        animator.setAddDuration(1000);
        animator.setRemoveDuration(500);
        recyclerView.setItemAnimator(animator);
    }

//<-------------------------------------ANIMATION---------------------------------------->

    private void adapterAnimation() {
        ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(myFirebaseRecylerAdapter);
        alphaAdapter.setDuration(500);
        recyclerView.setAdapter(alphaAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnfrontPageInteractionListener {
        // TODO: Update argument type and name
        void onFrontPageInteraction(Uri uri);
    }

    public interface OnCardItemClickedListener {
        public void onCardItemClicked(HashMap<String, ?> movie);
    }
}
