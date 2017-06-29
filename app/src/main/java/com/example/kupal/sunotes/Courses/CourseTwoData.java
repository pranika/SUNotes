package com.example.kupal.sunotes.Courses;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kupal on 4/21/2017.
 */

public class CourseTwoData {
    List<Map<String,?>> feedList;
    DatabaseReference mRef;
    SecondFirebaseRecyclerAdaptar myFirebaseRecylerAdapter;
    public Context mContext;

    public void setAdapter(SecondFirebaseRecyclerAdaptar mAdapter) {
        myFirebaseRecylerAdapter = mAdapter;
    }

    public void removeItemFromServer(Map<String,?> movie){
        if(movie!=null){
            String id = (String)movie.get("id");
            mRef.child(id).removeValue();
        }
    }

    public void addItemToServer(Map<String,?> movie){
        if(movie!=null){
            String id = (String) movie.get("id");
            mRef.child(id).setValue(movie);
        }
    }

    public DatabaseReference getFireBaseRef(){
        return mRef;
    }
    public void setContext(Context context){mContext = context;}

    public List<Map<String, ?>> getMoviesList() {
        return feedList;
    }

    public int getSize(){
        return feedList.size();
    }

    public HashMap getItem(int i){
        if (i >=0 && i < feedList.size()){
            return (HashMap) feedList.get(i);
        } else return null;
    }


    public void onItemRemovedFromCloud(HashMap item){
        int position = -1;
        String id=(String)item.get("id");
        for(int i=0;i<feedList.size();i++){
            HashMap movie = (HashMap)feedList.get(i);
            String mid = (String)movie.get("id");
            if(mid.equals(id)){
                position= i;
                break;
            }
        }
        if(position != -1){
            feedList.remove(position);
            //Toast.makeText(mContext, "Item Removed:" + id, Toast.LENGTH_SHORT).show();

        }
    }

    public void onItemAddedToCloud(HashMap item){
        int insertPosition = 0;
        String id=(String)item.get("id");
        for(int i=0;i<feedList.size();i++){
            HashMap movie = (HashMap)feedList.get(i);
            String mid = (String)movie.get("id");
            if(mid.equals(id)){
                return;
            }
            if(mid.compareTo(id)<0){
                insertPosition=i+1;
            }else{
                break;
            }
        }
        feedList.add(insertPosition,item);
        //Toast.makeText(mContext, "Item added:" + id, Toast.LENGTH_SHORT).show();

    }

    public void onItemUpdatedToCloud(HashMap item){
        String id=(String)item.get("id");
        for(int i=0;i<feedList.size();i++){
            HashMap movie = (HashMap)feedList.get(i);
            String mid = (String)movie.get("id");
            if(mid.equals(id)){
                feedList.remove(i);
                feedList.add(i,item);
                Log.d("My Test: NotifyChanged",id);
                break;
            }
        }

    }
    public void initializeDataFromCloud() {
        feedList.clear();

        mRef.addChildEventListener(new com.google.firebase.database.ChildEventListener() {

            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    HashMap<String,String> movie = (HashMap<String,String>)dataSnapshot.getValue();
                    onItemAddedToCloud(movie);
                }
                Log.d("MyTest: OnChildAdded", dataSnapshot.toString());


            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                Log.d("MyTest: OnChildChanged", dataSnapshot.toString());
                HashMap<String,String> movie = (HashMap<String,String>)dataSnapshot.getValue();
                onItemUpdatedToCloud(movie);
            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {
                Log.d("MyTest: OnChildRemoved", dataSnapshot.toString());
                HashMap<String,String> movie = (HashMap<String,String>)dataSnapshot.getValue();
                onItemRemovedFromCloud(movie);
            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public CourseTwoData(){

        feedList = new ArrayList<Map<String,?>>();
        mRef = FirebaseDatabase.getInstance().getReference().child("CourseSec").getRef();
        myFirebaseRecylerAdapter = null;
        mContext = null;
    }


    public int findFirst(String query){

        for(int i=0;i<feedList.size();i++){
            HashMap hm = (HashMap)getMoviesList().get(i);
            if(((String)hm.get("title")).toLowerCase().contains(query.toLowerCase())){
                return i;
            }
        }
        return 0;

    }


}
