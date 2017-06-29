package com.example.kupal.sunotes.fcm_notification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.kupal.sunotes.R;
import com.example.kupal.sunotes.RecycleClass;
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

public class StudentAnswer extends AppCompatActivity {
    private static final String NAME = "name";
    private DatabaseReference ref;
    String answer = "";
    int selectedId = 0;
    private FirebaseAuth mauth;
    private RadioGroup answer_student;

    private Button button;
    private RadioButton reply;
    Intent intent;

    Bundle extras;
    TextView ques;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_student_answer);
        answer_student= (RadioGroup) findViewById(R.id.radiocourse);

        ques = (TextView) findViewById(R.id.question_student);
        extras = getIntent().getExtras();

        button= (Button) findViewById(R.id.postans);
        ques.setText(extras.getString("question"));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref = FirebaseDatabase.getInstance().getReference().child("Answers");
                FirebaseAuth mauth = FirebaseAuth.getInstance();
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.NOTIFY_PREF), Context.MODE_PRIVATE);
                Date timestamp = new Date();
                Calendar calendar = new GregorianCalendar();
                final int day = calendar.get(Calendar.DAY_OF_WEEK);
                final SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
                String time = new SimpleDateFormat("HH:mm").format(calendar.getTime());
                final String userid = mauth.getCurrentUser().getUid();
                final DatabaseReference userdb = ref.child(String.valueOf(Integer.MAX_VALUE - timestamp.getTime() % 1000000000));
                userdb.child("id").setValue(String.valueOf(Integer.MAX_VALUE - timestamp.getTime() % 1000000000));
                userdb.child("description").setValue(answer);
                userdb.child("link").setValue(userid);
                userdb.child("primaryID").setValue("PRIMARYID");
                userdb.child("pubDate").setValue(String.valueOf(time+"  "+getDay(day)));
                final String username = sharedPreferences.getString("name","");
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
                            intent = new Intent(getApplicationContext(), RecycleClass.class);
                            startActivity(intent);
                            valuse++;
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
        });
    }


    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        selectedId = answer_student.getCheckedRadioButtonId();

        switch (view.getId()) {
            case R.id.ans_yes:

                if (checked)
                    reply = (RadioButton) findViewById(selectedId);
                answer = reply.getText().toString().trim();
                break;
            case R.id.ans_no:
                if (checked)
                    reply = (RadioButton) findViewById(selectedId);
                answer = reply.getText().toString().trim();
                break;
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
}
