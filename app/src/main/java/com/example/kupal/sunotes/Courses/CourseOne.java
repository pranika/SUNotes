package com.example.kupal.sunotes.Courses;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

import com.example.kupal.sunotes.R;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CourseOne.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CourseOne#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseOne extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button postButton;
    private EditText postText;
    CourseData courseData;
    LinearLayoutManager lm;
    CourseFireBaseRecyclerAdaptar courseFireBaseRecyclerAdaptar;
    public OnCardItemClickedListener onCardItemClickedListener;
    RecyclerView recyclerView;
    private String mParam1;
    private static final String NAME = "name", url = "URL";
    private String mParam2;
    //private static String default ="https://firebasestorage.googleapis.com/v0/b/sunotes-165703.appspot.com/o/Photos%2Fd_loadimage.png?alt=media&token=59885813-1654-49e5-8d47-ce8633c4c699";
    private LinearLayoutManager linearLayoutManager;

    private CourseOne.OnFragmentInteractionListener mListener;

    public CourseOne() {}

    public static CourseOne newInstance(String param1, String param2) {
        CourseOne fragment = new CourseOne();
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
        View rootview = inflater.inflate(R.layout.fragment_course_list,container,false);
        mListener = (CourseOne.OnFragmentInteractionListener) rootview.getContext();
        postButton = (Button) rootview.findViewById(R.id.post);
        postText = (EditText) rootview.findViewById(R.id.postText);
        onCardItemClickedListener = (OnCardItemClickedListener) getContext();

        DatabaseReference childRef = FirebaseDatabase.getInstance().getReference().child("Course").getRef();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        courseFireBaseRecyclerAdaptar = new CourseFireBaseRecyclerAdaptar(FeedItem.class, R.layout.custum_row_feed, CourseFireBaseRecyclerAdaptar.FeedViewHolder.class
                ,childRef, getActivity());
        courseData = new CourseData();
        recyclerView= (RecyclerView) rootview.findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new VerticalSpace(50));
        recyclerView.setAdapter(courseFireBaseRecyclerAdaptar);
        if (courseData.getSize() == 0) {
            courseData.setAdapter(courseFireBaseRecyclerAdaptar);
            courseData.setContext(getActivity());
            courseData.initializeDataFromCloud();
        }

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Course");
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

        courseFireBaseRecyclerAdaptar.setOnItemClickListener(new CourseFireBaseRecyclerAdaptar.RecyclerItemClickListener() {

            @Override
            public void onItemClick(View v, final int position) {
                if(courseData.getItem(position) != null){
                    onCardItemClickedListener.onCardItemClicked(courseData.getItem(position),v);
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
                                        movie = (HashMap) ((HashMap) courseData.getItem(position));
                                        courseData.removeItemFromServer(movie);
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

        return rootview;
    }

    public static String encode(String header) {
        char[] code = new char[32];
        for(int i = 0; i < header.length(); i++) {
            code[i % code.length] = (char)((int)code[i % code.length] ^ (int)header.charAt(i));
        }
        return new String(code);
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
        if (context instanceof CourseOne.OnFragmentInteractionListener) {
            mListener = (CourseOne.OnFragmentInteractionListener) context;
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


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public interface OnCardItemClickedListener {
        public void onCardItemClicked(HashMap<String, ?> movie, View sr);;
    }
}
