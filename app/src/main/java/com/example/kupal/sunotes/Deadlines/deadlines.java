package com.example.kupal.sunotes.Deadlines;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

public class deadlines extends AppCompatActivity {

    private Button postButton;
    private EditText postText;
    deadlinedata courseData;
    LinearLayoutManager lm;
    deadlineAdaptar courseFireBaseRecyclerAdaptar;
    RecyclerView recyclerView;
    private String mParam1;
    private String mParam2;
    private LinearLayoutManager linearLayoutManager;
    private String course = "",login_type= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deadlines);

        Bundle extras = getIntent().getExtras();
        course = extras.getString("course_value");
        login_type = extras.getString("login_type");
        postButton = (Button) findViewById(R.id.post);
        postText = (EditText) findViewById(R.id.postText);

        DatabaseReference childRef = FirebaseDatabase.getInstance().getReference().child("Deadline").getRef();
        courseFireBaseRecyclerAdaptar = new deadlineAdaptar(FeedItem.class, R.layout.custum_row_feed, deadlineAdaptar.FeedViewHolder.class
                ,childRef, this);
        courseData = new deadlinedata();
        recyclerView= (RecyclerView) findViewById(R.id.drecyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new VerticalSpace(50));
        recyclerView.setAdapter(courseFireBaseRecyclerAdaptar);
        if (courseData.getSize() == 0) {
            courseData.setAdapter(courseFireBaseRecyclerAdaptar);
            courseData.setContext(this);
            courseData.initializeDataFromCloud();
        }

        if(login_type.contains("Student")){
            postButton.setVisibility(View.GONE);
            postText.setVisibility(View.GONE);
        }

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Deadline");
                FirebaseAuth mauth = FirebaseAuth.getInstance();
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.NOTIFY_PREF), Context.MODE_PRIVATE);
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


        courseFireBaseRecyclerAdaptar.setOnItemClickListener(new deadlineAdaptar.RecyclerItemClickListener() {
            @Override
            public void onItemClick(View v, final int position) {
                if(courseData.getItem(position) != null){
                    //do stuff
                }
            }

            @Override
            public void onOverFlowButtonClick(View view, final int position) {
                PopupMenu popup = new PopupMenu(deadlines.this, view);
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
}
