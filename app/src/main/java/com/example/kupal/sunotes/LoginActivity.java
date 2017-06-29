package com.example.kupal.sunotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kupal.sunotes.MainActivityPackage.Main;
import com.example.kupal.sunotes.MainActivityPackage.UploadImage;
import com.example.kupal.sunotes.fcm_notification.MySingleton;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    private static final String USER_CREATION_SUCCESS = "Successfully created user";
    private static final String USER_CREATION_ERROR = "User creation error";
    private static final String EMAIL_INVALID = "email is invalid :";
    private static final String COURSE = "course";
    private static final String USERID = "userid";
    private static final String NAME = "name", url = "URL";
    String app_server_url = "http://www.sunotes.ngrok.io/token_insert.php";
    String app_server_update = "http://www.sunotes.ngrok.io/token_update.php";
    EditText userNameET;
    EditText passwordET;
    FirebaseAuth mAuth;
    Boolean isStudent = false;
    String logintype = "", coursetype = "",strEditText = "";
    int selectedId = 0, courseid = 0;
    private EditText name;
    private RadioGroup loginas, course;
    private static String USERNAME = "priv.file", COURSENAME = "priv.file2";
    private Button button, uploadimage;
    private RadioButton radiobutton;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String userid = "", course_user = "", login_type = "", name_user = "";
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginas = (RadioGroup) findViewById(R.id.radiogroup);
        course = (RadioGroup) findViewById(R.id.radiocourse);
        ref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    if (getFileInfo(USERNAME).equals("Student")) {
                        SharedPreferences sharedPreferences = getApplicationContext().
                                getSharedPreferences(getString(R.string.NOTIFY_PREF), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(COURSE, getFileInfo(COURSENAME));
                        editor.putString(USERID, getFileInfo(USERNAME));

                        editor.commit();
                        final String token = sharedPreferences.getString(getString(R.string.NOTIFY_TOKEN), "");
                        final String course = sharedPreferences.getString(COURSE, "NO COURSE FOUND");
                        final String studentid = sharedPreferences.getString(USERID, "no user id");
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, app_server_update,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {}
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {}
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("notify_token", token);
                                params.put("course", course_user);
                                params.put("studentid", userid);
                                return params;
                            }
                        };
                        MySingleton.getInstance(getApplicationContext()).addtoRequestQue(stringRequest);
                        userid = user.getUid();
                        Intent myIntent = new Intent(getApplicationContext(), Main.class);
                        myIntent.putExtra("course_value", getFileInfo(COURSENAME));
                        myIntent.putExtra("login_type", getFileInfo(USERNAME));
                        startActivity(myIntent);
                        finish();
                    }else{
                        Intent myIntent = new Intent(getApplicationContext(), Main.class);
                        myIntent.putExtra("course_value", getFileInfo(COURSENAME));
                        myIntent.putExtra("login_type", getFileInfo(USERNAME));
                        startActivity(myIntent);
                        finish();
                    }
                }
            }
        };

        userNameET = (EditText) findViewById(R.id.edit_text_email);
        passwordET = (EditText) findViewById(R.id.edit_text_password);
        name = (EditText) findViewById(R.id.edit_text_name);
        uploadimage = (Button) findViewById(R.id.upload);
        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UploadImage.class);
                startActivityForResult(intent,1);
            }
        });

        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//student login token insert
                isStudent = true;
                writeToFile("Student",USERNAME);
                writeToFile("CSE 600",COURSENAME);
                SharedPreferences sharedPreferences = getApplicationContext().
                        getSharedPreferences(getString(R.string.NOTIFY_PREF), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.commit();
                final String token = sharedPreferences.getString(getString(R.string.NOTIFY_TOKEN), "");
                final String course = sharedPreferences.getString(COURSE, "NO COURSE FOUND");
                final String studentid = sharedPreferences.getString(USERID, "no user id");


                StringRequest stringRequest = new StringRequest(Request.Method.POST, app_server_update,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("notify_token", token);
                        params.put("course", course);
                        params.put("studentid", studentid);
                        return params;
                    }
                };
                MySingleton.getInstance(getApplicationContext()).addtoRequestQue(stringRequest);

                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(false)
                        .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                        )).build(), RC_SIGN_IN);
            }
        });

        Button profLogin = (Button) findViewById(R.id.proflogin);
        profLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStudent = false;
                writeToFile("Instructor",USERNAME);
                writeToFile("CSE 600",COURSENAME);
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(false)
                                .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                                ))
                                .build(),
                        RC_SIGN_IN);
            }
        });

        Button createButton = (Button) findViewById(R.id.button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.

        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                strEditText = data.getStringExtra("url");
            }
        }
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                Intent myIntent = new Intent(getApplicationContext(), Main.class);
                if(isStudent){
                    myIntent.putExtra("login_type","Student");
                    writeToFile("Student",USERNAME);
                    writeToFile("CSE 600",COURSENAME);
                }else{
                    myIntent.putExtra("login_type","Instructor");
                    writeToFile("Instructor",USERNAME);
                    writeToFile("CSE 600",COURSENAME);
                }
                myIntent.putExtra("course_value", coursetype);
                startActivity(myIntent);
                finish();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar("Sign in cancelled");
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar("No network connnection");
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackbar("Error occured while signing in");
                    return;
                }
            }
            showSnackbar("Error occured while signing in");
        }
    }

    public void showSnackbar(String s) {
        Snackbar snackbar = Snackbar.make(userNameET, s, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    // Validate email address for new accounts.
    private boolean isEmailValid(String email) {
        boolean isGoodEmail = (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            userNameET.setError(EMAIL_INVALID + email);
            return false;
        }
        return true;
    }


    private void createUser() {
        final String nametext = name.getText().toString();
        if (!(TextUtils.isEmpty(userNameET.getText().toString()) || TextUtils.isEmpty(passwordET.getText().toString()))||  !isEmailValid(userNameET.getText().toString())) {

            mAuth.createUserWithEmailAndPassword(userNameET.getText().toString(), passwordET.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (!task.isSuccessful()) {
                        Snackbar snackbar = Snackbar.make(userNameET, USER_CREATION_ERROR, Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    } else {
                        Snackbar snackbar = Snackbar.make(userNameET, USER_CREATION_SUCCESS, Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        userid = mAuth.getCurrentUser().getUid();
                        DatabaseReference userdb = ref.child("Users").child(userid);
                        userdb.child("id").setValue(userid);
                        userdb.child("LoginType").setValue(logintype);
                        userdb.child("Course").setValue(coursetype);
                        userdb.child("Name").setValue(nametext);
                        userdb.child("ImageUrl").setValue(strEditText);

                        SharedPreferences sharedPreferences = getApplicationContext().
                                getSharedPreferences(getString(R.string.NOTIFY_PREF), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(COURSE, coursetype);
                        editor.putString(NAME, nametext);
                        editor.putString(USERID, userid);
                        editor.putString(nametext,strEditText);

                        editor.commit();
                        Toast.makeText(LoginActivity.this, "Image URL :: " + strEditText, Toast.LENGTH_SHORT).show();

                        if (logintype.equals("Student")) {
                            final String token = sharedPreferences.getString(getString(R.string.NOTIFY_TOKEN), "");
                            final String course = sharedPreferences.getString(COURSE, "NO COURSE FOUND");
                            final String studentid = sharedPreferences.getString(USERID, "no user id");

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, app_server_url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {}
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {}
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("notify_token", token);
                                    params.put("course", course);
                                    params.put("studentid", studentid);
                                    return params;
                                }
                            };
                            MySingleton.getInstance(getApplicationContext()).addtoRequestQue(stringRequest);
                        }

                        Intent intent = new Intent(getApplicationContext(), Main.class);
                        intent.putExtra("course_value", coursetype);
                        intent.putExtra("name",nametext);
                        intent.putExtra("login_type", logintype);
                        startActivity(intent);
                        finish();
                    }
                }


            });
        }
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        selectedId = loginas.getCheckedRadioButtonId();
        switch (view.getId()) {
            case R.id.radio_pirates:
                if (checked)
                    radiobutton = (RadioButton) findViewById(selectedId);
                logintype = radiobutton.getText().toString().trim();
                break;
            case R.id.radio_ninjas:
                if (checked)
                    radiobutton = (RadioButton) findViewById(selectedId);
                logintype = radiobutton.getText().toString().trim();
                break;
        }

        courseid = course.getCheckedRadioButtonId();

        switch (view.getId()) {
            case R.id.cse_600:
                if (checked)
                    radiobutton = (RadioButton) findViewById(courseid);
                coursetype = radiobutton.getText().toString().trim();
                break;
            case R.id.cse_681:
                if (checked)
                    radiobutton = (RadioButton) findViewById(courseid);
                coursetype = radiobutton.getText().toString().trim();
                break;
            case R.id.cse_687:
                if (checked)
                    radiobutton = (RadioButton) findViewById(courseid);
                coursetype = radiobutton.getText().toString().trim();
                break;
        }
    }

    public  void writeToFile(String val, String filename){
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(filename, Context.MODE_PRIVATE);
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
            fs = openFileInput(filename);
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
