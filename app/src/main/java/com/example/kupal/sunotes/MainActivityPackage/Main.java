package com.example.kupal.sunotes.MainActivityPackage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kupal.sunotes.BusTimingPackage.BusTime;
import com.example.kupal.sunotes.Courses.Courses;
import com.example.kupal.sunotes.Deadlines.deadlines;
import com.example.kupal.sunotes.Deadlines.officehours;
import com.example.kupal.sunotes.LoginActivity;
import com.example.kupal.sunotes.OtherUtilties.Fragment_DetailView;
import com.example.kupal.sunotes.R;
import com.example.kupal.sunotes.RSSFeedDem.SuLive;
import com.example.kupal.sunotes.RecycleClass;
import com.example.kupal.sunotes.fcm_notification.PostQuestion;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, frontPage.OnfrontPageInteractionListener,
        Fragment_DetailView.OnFragmentInteractionListener, frontPage.OnCardItemClickedListener {

    Button getHtmlString;
    private TextView textView;
    private OnTask1ClickedListener t1Listener;
    private String course_value, user;

    private Fragment currentFragment;
    private static String USERNAME = "priv.file", COURSENAME = "priv.file2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        course_value = extras.getString("course_value");
        if (getFileInfo(USERNAME) != "") {
            user = getFileInfo(USERNAME);
        } else {
            user = extras.getString("login_type");
        }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!user.contains("Student")) {//this value is not getting passed during sign in.....
                    Intent intentpq = new Intent(Main.this, PostQuestion.class);
                    intentpq.putExtra("course_value", course_value);
                    intentpq.putExtra("login_type", user);
                    startActivity(intentpq);
                } else {
                    Intent intentpf = new Intent(Main.this, RecycleClass.class);
                    intentpf.putExtra("course_value", course_value);
                    intentpf.putExtra("login_type", user);
                    startActivity(intentpf);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Toast.makeText(this, "Main! " + user, Toast.LENGTH_LONG).show();
        if (savedInstanceState != null) {
            if (getSupportFragmentManager().getFragment(savedInstanceState, "current") != null) {
                currentFragment = getSupportFragmentManager()
                        .getFragment(savedInstanceState, "current");
            } else {
                currentFragment = frontPage.newInstance("", "");
            }
        } else {
            currentFragment = frontPage.newInstance("", "");
        }

        getSupportFragmentManager().beginTransaction().
                replace(R.id.main_container, currentFragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (manager.getBackStackEntryCount() > 1 ) {
            manager.popBackStack();
        } else {
            // if there is only one entry in the backstack, show the home screen
            moveTaskToBack(true);
        }
    }

    //<---------------------------Somehow need this for interaction-------------------------------------------->

    @Override
    public void onFrontPageInteraction(Uri uri) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "current", currentFragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.news) {
            Intent intent = new Intent(this, SuLive.class);
            startActivity(intent);
        } else if (id == R.id.courses) {
            Intent intent = new Intent(this, Courses.class);
            startActivity(intent);
        } else if (id == R.id.deadlines) {
            Intent intent = new Intent(this, deadlines.class);
            intent.putExtra("course_value", course_value);
            intent.putExtra("login_type", user);
            startActivity(intent);
        } else if (id == R.id.bus) {
            Intent intent = new Intent(this, BusTime.class);
            startActivity(intent);
        } else if (id == R.id.officeHours) {
            Intent intent = new Intent(this, officehours.class);
            intent.putExtra("course_value", course_value);
            intent.putExtra("login_type", user);
            startActivity(intent);
        } else if (id == R.id.logout) {
            writeToFile("", USERNAME);
            writeToFile("", COURSENAME);
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signOut();
            Intent intentlogin = new Intent(this, LoginActivity.class);
            startActivity(intentlogin);
            finish();
        } else if (id == R.id.results) {
            Intent intent = new Intent(this, RecycleClass.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onCardItemClicked(HashMap<String, ?> movie) {
        //Toast.makeText(this, "Implement click here", Toast.LENGTH_SHORT).show();
    }

    public interface OnTask1ClickedListener {
        public void onTask1ButtonClicked(Bundle savedInstanceState);
    }

    public void writeToFile(String val, String filename) {
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

    public String getFileInfo(String filename) {
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
        return String.valueOf(sb);
    }

}
