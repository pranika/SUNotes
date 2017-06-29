package com.example.kupal.sunotes.RSSFeedDem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.kupal.sunotes.R;

import java.util.ArrayList;
import java.util.Map;

public class SuLive extends AppCompatActivity{

    FeedData feedData;
    ArrayList<FeedItem> feedDataBase;
    ArrayList<Map<String,?>> feedMaps;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this,"Entered SuLive Class!",Toast.LENGTH_LONG ).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        //taskResult = (ReadRss.TaskResult) getApplicationContext();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        ReadRss rss = new ReadRss(this,recyclerView);
        rss.execute();
    }
}
