package com.example.kupal.sunotes.fcm_notification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kupal.sunotes.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentActivity extends AppCompatActivity {
    TextView titletxt,msgtxt;
    EditText editText;
    Button button;
    private DatabaseReference ref;
    private FirebaseAuth mauth;
    String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_student);
        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        mauth = FirebaseAuth.getInstance();
        Bundle extras = getIntent().getExtras();
        String ques = extras.getString("title");
        String ans = extras.getString("message");
        titletxt= (TextView) findViewById(R.id.question);
        msgtxt= (TextView) findViewById(R.id.answer);

        button= (Button) findViewById(R.id.post);

        titletxt.setText(ques);
        msgtxt.setText(ans);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editText= (EditText) findViewById(R.id.postans);
                answer = (String)editText.getText().toString();
                String userid = mauth.getCurrentUser().getUid();
                DatabaseReference userdb = ref.child(userid);
                userdb.child("answer").setValue(answer);


            }
        });
    }
}
