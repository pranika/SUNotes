package com.example.kupal.sunotes.fcm_notification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.kupal.sunotes.R;

public class PostQuestion extends AppCompatActivity {

    private RadioGroup questiontype,coursegroup;

    private Button button;
    private RadioButton reply;
    String type, coursegrp = "";
    int selectedId=0;
    Intent intent=new Intent();
    private int courseID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_question);
        Bundle extras = getIntent().getExtras();
        final String course=extras.getString("course_value");
        final String login_type = extras.getString("login_type");

        questiontype=(RadioGroup)findViewById(R.id.radiogroup);
        coursegroup = (RadioGroup) findViewById(R.id.radiocourse);
        button= (Button) findViewById(R.id.posttype);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("YES / NO")) {
                    intent = new Intent(getApplicationContext(), YesNo.class);
                    intent.putExtra("course_value",coursegrp);
                    intent.putExtra("login_type", login_type);
                    startActivity(intent);
                }
                else if(type.endsWith("Multiple Choice")) {
                    intent = new Intent(getApplicationContext(), MCQ.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(getApplicationContext(),"Please Select Option",Toast.LENGTH_LONG).show();

            }
        });
    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        selectedId = questiontype.getCheckedRadioButtonId();

        switch (view.getId()) {
            case R.id.yes_no:
                if (checked)
                    reply = (RadioButton) findViewById(selectedId);
                type = reply.getText().toString().trim();

                break;
            case R.id.mcq:
                if (checked)
                    reply = (RadioButton) findViewById(selectedId);
                type = reply.getText().toString().trim();
                break;
        }

        courseID = coursegroup.getCheckedRadioButtonId();

        switch (view.getId()) {
            case R.id.cse_600:

                if (checked)
                    reply = (RadioButton) findViewById(courseID);
                coursegrp = reply.getText().toString().trim();
                break;
            case R.id.cse_681:
                if (checked)
                    reply = (RadioButton) findViewById(courseID);
                coursegrp = reply.getText().toString().trim();
                break;
            case R.id.cse_687:
                if (checked)
                    reply = (RadioButton) findViewById(courseID);
                coursegrp = reply.getText().toString().trim();
                break;
        }
    }
}
