package com.example.kupal.sunotes.Courses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kupal.sunotes.R;

import java.util.HashMap;

public class AnswerFragment extends Fragment {

    //<------------------------------------Data members --------------------------------->

    HashMap<String,?> movie;
    TextView movieName;
    ImageView imgView;
    TextView desc;
    RatingBar rating;
    TextView stars;
    TextView textRating;
    TextView dirName;
    ShareActionProvider shareActionProvider;

    //<------------------------------------Constructor --------------------------------->

    public AnswerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        movie = (HashMap<String, ?>) getArguments().getSerializable("movie");
        View view = inflater.inflate(R.layout.fragment_answer,container,false);
        setHasOptionsMenu(true);
        Toast.makeText(getActivity(), "Fragment TEst", Toast.LENGTH_SHORT).show();
//        movieName = (TextView) view.findViewById(R.id.movieName);
//        //movieName.setText((String)movie.get("name")+" ("+(String)movie.get("year")+")");
//        desc = (TextView) view.findViewById(R.id.desc);
//        //desc.setText((String) movie.get("description"));
//        imgView= (ImageView) view.findViewById(R.id.imgView3);
//        //imgView.setImageResource((Integer) movie.get("image"));
//        rating = (RatingBar) view.findViewById(R.id.ratingBar);
//        //double mRating = (Double) movie.get("rating");
//        //float convRating = (float) mRating/2.0f;
//        //rating.setRating(convRating);
//        textRating = (TextView) view.findViewById(R.id.textRating);
//        //textRating.setText(""+(convRating*2)+"/10");
//        stars = (TextView) view.findViewById(R.id.stars);
//        //stars.setText((String) movie.get("stars"));
//        dirName = (TextView) view.findViewById(R.id.directorName);
//        //dirName.setText((String)movie.get("director"));

        return view;
    }

    //<------------------------------------Called when creating a new instance --------------------------------->

    public static AnswerFragment newInstance(HashMap<String,?> item) {
        Bundle args = new Bundle();
        AnswerFragment fragment = new AnswerFragment();
        //args.putSerializable("movie",item);
        fragment.setArguments(args);
        return fragment;
    }

    //<------------------------------------Option menu of the toolbar --------------------------------->

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(menu.findItem(R.id.shareAction)==null){
            inflater.inflate(R.menu.movie_fragment_action_provider,menu);
        }
        MenuItem share = menu.findItem(R.id.shareAction);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(share);

        Intent intentShare = new Intent(Intent.ACTION_SEND);
        //intentShare.setType("text/plain");
        //intentShare.putExtra(Intent.EXTRA_TEXT,(String)movie.get("name")+ movie.get("description"));

        //set intent
        shareActionProvider.setShareIntent(intentShare);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
