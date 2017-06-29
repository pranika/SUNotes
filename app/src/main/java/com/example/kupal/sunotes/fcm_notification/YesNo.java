package com.example.kupal.sunotes.fcm_notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kupal.sunotes.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class YesNo extends AppCompatActivity {

    private DatabaseReference ref;
    String userid="";
    private FirebaseAuth mauth;
    Button button;
    EditText editText;

    private static final String COURSE = "course";
    private static final String TITLE = "title";
    private static final String MESSAGE = "message";

    private static final String QUESTION_ID = "question_id";
    private static final String PROFESSOR_ID = "professor_id";
    private static final String QUES_TYPE = "ques_type";
    EditText title,body;
    String coursetype="";
    String course_ques=  "",login_type = "'";
    String app_server_url = "http://www.sunotes.ngrok.io/send_notification.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yes_no);

        editText= (EditText) findViewById(R.id.edit_question);
        button= (Button) findViewById(R.id.postbutton);
        Bundle extras = getIntent().getExtras();
        course_ques=extras.getString("course_value");
        login_type=extras.getString("login_type");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ques=editText.getText().toString();

                mauth = FirebaseAuth.getInstance();
                String userid = mauth.getCurrentUser().getUid();

                ref = FirebaseDatabase.getInstance().getReference().child("Question");
                int questionid =1;
                ref.child("questionid").setValue(questionid);
                ref.child("question_name").setValue(ques);
                ref.child("user_id").setValue(userid);
                ref.child("question_type").setValue("YES/NO");

                String title_text = "QUIZ BY PROFESSOR";
                String message_text = ques;

                SharedPreferences sharedPreferences = getApplicationContext().
                        getSharedPreferences(getString(R.string.NOTIFY_PREF), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(COURSE, course_ques);
                editor.putString(TITLE, title_text);
                editor.putString(MESSAGE, message_text);
                editor.putString(QUESTION_ID,Integer.toString(questionid));
                editor.putString(PROFESSOR_ID, userid);
                editor.putString(QUES_TYPE, "YES/NO");



                editor.commit();


                System.out.println("Course" + coursetype + "id");
                sharedPreferences = getApplicationContext().
                        getSharedPreferences(getString(R.string.NOTIFY_PREF), Context.MODE_PRIVATE);
//                               final  String token = sharedPreferences.getString(getString(R.string.NOTIFY_TOKEN),"");
                final  String course = sharedPreferences.getString(COURSE, "NO COURSE FOUND");
                final  String title = sharedPreferences.getString(TITLE, "NO TITLE FOUND");
                final String message = sharedPreferences.getString(MESSAGE, "no message found");
                final  String ques_id = sharedPreferences.getString(QUESTION_ID, "NO ques FOUND");
                final  String prof_id = sharedPreferences.getString(PROFESSOR_ID, "NO prof FOUND");
                final String ques_type = sharedPreferences.getString(QUES_TYPE, "no ques type found");
                System.out.println("title"+ ques);
                //   System.out.println("COURSE"+course);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, app_server_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })

                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("course", course);
                        params.put("title", title);
                        params.put("message", message);
                        params.put("prof_id", prof_id);
                        params.put("ques_id", ques_id);
                        params.put("ques_type", ques_type);
//                                       params.put("professorid",professorid);

                        return params;
                    }
                };
                MySingleton.getInstance(getApplicationContext()).addtoRequestQue(stringRequest);

            }
        });



    }
}
