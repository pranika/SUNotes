
package com.example.kupal.sunotes.fcm_notification;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rakeshh91 on 3/20/2017.
 */
public class StudentData {
	List<Map<String,?>> studentList;
	DatabaseReference mRef;
	StudentDataFireBaseAdaptar myFirebaseRecylerAdapter;
	Context mContext;


	public StudentData(){

		studentList = new ArrayList<Map<String,?>>();
		mRef = FirebaseDatabase.getInstance().getReference().child("Answers").getRef();
		myFirebaseRecylerAdapter = null;
		mContext = null;

	}

	public void setAdapter(StudentDataFireBaseAdaptar mAdapter) {

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

	public List<Map<String, ?>> getStudentList() {
		return studentList;
	}

	public int getSize(){
		return studentList.size();
	}

	public HashMap getItem(int i){
		if (i >=0 && i < studentList.size()){
			return (HashMap) studentList.get(i);
		} else return null;
	}


	public void onItemRemovedFromCloud(HashMap item){
		int position = -1;
		String id=(String)item.get("id");
		for(int i=0;i<studentList.size();i++){
			HashMap movie = (HashMap)studentList.get(i);
			String mid = (String)movie.get("id");
			if(mid.equals(id)){
				position= i;
				break;
			}
		}
		if(position != -1){
			studentList.remove(position);
			Toast.makeText(mContext, "Item Removed:" + id, Toast.LENGTH_SHORT).show();

		}
		if ( myFirebaseRecylerAdapter!= null ) {
			myFirebaseRecylerAdapter.notifyItemRemoved (position) ;
			myFirebaseRecylerAdapter.notifyDataSetChanged () ;
		}

	}

	public void onItemAddedToCloud(HashMap item){
		int insertPosition = 0;
		String id=(String)item.get("id");
		for(int i=0;i<studentList.size();i++){
			HashMap student = (HashMap)studentList.get(i);
			String mid = (String)student.get("id");
			if(mid.equals(id)){
				return;
			}
			if(mid.compareTo(id)<0){
				insertPosition=i+1;
			}else{
				break;
			}
		}
		studentList.add(insertPosition,item);
		// Toast.makeText(mContext, "Item added:" + id, Toast.LENGTH_SHORT).show();
		if(myFirebaseRecylerAdapter !=null)
		{
			myFirebaseRecylerAdapter.notifyItemInserted(insertPosition);
		}

	}

	public void onItemUpdatedToCloud(HashMap item){
		String id=(String)item.get("id");
		for(int i=0;i<studentList.size();i++){
			HashMap movie = (HashMap)studentList.get(i);
			String mid = (String)movie.get("id");
			if(mid.equals(id)){
				studentList.remove(i);
				studentList.add(i,item);
				Log.d("My Test: NotifyChanged",id);

				break;
			}
			if ( myFirebaseRecylerAdapter!= null ) {
				myFirebaseRecylerAdapter.notifyItemChanged(i);
			}

		}

	}
	public void initializeDataFromCloud() {
		studentList.clear();
		mRef.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s) {
				Log.d("MyTest: OnChildAdded", dataSnapshot.toString());
				HashMap movie = (HashMap)dataSnapshot.getValue();
				onItemAddedToCloud(movie);
			}

			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String s) {
				Log.d("MyTest: OnChildChanged", dataSnapshot.toString());
				HashMap movie = (HashMap)dataSnapshot.getValue();
				onItemUpdatedToCloud(movie);
			}

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot) {
				Log.d("MyTest: OnChildRemoved", dataSnapshot.toString());
				HashMap movie = (HashMap<String,String>)dataSnapshot.getValue();
				onItemRemovedFromCloud(movie);
			}

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String s) {

			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});

	}




}