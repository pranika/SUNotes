package com.example.kupal.sunotes.fcm_notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kupal.sunotes.R;


import java.util.HashMap;
import java.util.Map;

//activity_main_notify
public class MainActivityNotify extends AppCompatActivity {
    Button button;
    private static final String COURSE = "course";
    private static final String TITLE = "title";
    private static final String MESSAGE = "message";

    EditText title, body;
    String coursetype = "";


    String app_server_url = "http://www.sunotes.ngrok.io/send_notification.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_notify);
        title = (EditText) findViewById(R.id.question);
        body = (EditText) findViewById(R.id.answer);

        Bundle extras = getIntent().getExtras();


        if (extras.getString("login_type").equals("Instructor")) {
            Log.e("login", extras.getString("login_type"));
            coursetype = extras.getString("course_value").toString();
            Log.e("course", coursetype);


            button = (Button) findViewById(R.id.token_button);

            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String title_text = (String) title.getText().toString();
                    String message_text = (String) body.getText().toString();
                    System.out.println("value" + title_text);

                    SharedPreferences sharedPreferences = getApplicationContext().
                            getSharedPreferences(getString(R.string.NOTIFY_PREF), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(COURSE, coursetype);
                    editor.putString(TITLE, title_text);
                    editor.putString(MESSAGE, message_text);
                    editor.commit();


                    System.out.println("Course" + coursetype + "id");
                    sharedPreferences = getApplicationContext().
                            getSharedPreferences(getString(R.string.NOTIFY_PREF), Context.MODE_PRIVATE);
//                               final  String token = sharedPreferences.getString(getString(R.string.NOTIFY_TOKEN),"");
                    final String course = sharedPreferences.getString(COURSE, "NO COURSE FOUND");
                    final String ques = sharedPreferences.getString(TITLE, "NO TITLE FOUND");
                    final String ans = sharedPreferences.getString(MESSAGE, "no message found");
                    System.out.println("title" + ques);
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
                            params.put("title", ques);
                            params.put("message", ans);
//                                       params.put("professorid",professorid);

                            return params;
                        }
                    };
                    MySingleton.getInstance(MainActivityNotify.this).addtoRequestQue(stringRequest);
                }
            });

        }
    }
}
