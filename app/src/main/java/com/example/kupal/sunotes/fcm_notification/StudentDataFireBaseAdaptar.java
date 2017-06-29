package com.example.kupal.sunotes.fcm_notification;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kupal.sunotes.R;
import com.example.kupal.sunotes.RSSFeedDem.FeedItem;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

/**
 * Created by kupal on 4/25/2017.
 */

public class StudentDataFireBaseAdaptar extends FirebaseRecyclerAdapter<FeedItem, StudentDataFireBaseAdaptar.MovieViewHolder> {
    static OnItemClickListner onItemClickListner;
    private Context mContext;
    LruCache<String,Bitmap> bitmapLruCache;


    public StudentDataFireBaseAdaptar(Class<FeedItem> modelClass, int modelLayout,
                                    Class<MovieViewHolder> holder, DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, holder, ref);
        this.mContext = context;
    }
    public interface OnItemClickListner{

        public void onClick(View view, int position);
        public void onflowclick(View v, int adapterPosition);
    }

    public void setOnItemClickListner(OnItemClickListner onItemClickListner)
    {
        this.onItemClickListner = onItemClickListner;

    }

    @Override
    protected void populateViewHolder(MovieViewHolder holder, FeedItem feed, int i) {

        //TODO: Populate viewHolder by setting the movie attributes to cardview fields
        if(holder.Title!=null) {
            holder.Title.setText(feed.getTitle());
        }
        if(holder.Description!=null){
            holder.Description.setText(feed.getDescription());
        }
        if(holder.Date!=null){
            holder.Date.setText(feed.getPubDate());
        }
        if(holder.Thumbnail!=null){
            Picasso.with(mContext).load(feed.getThumbnailUrl()).into(holder.Thumbnail);
        }
    }

    //TODO: Populate ViewHolder and add listeners.
    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView Title,Description,Date;
        ImageView Thumbnail;
        CardView cardView;
        public MovieViewHolder(View v) {
            super(v);
            Title= (TextView) itemView.findViewById(R.id.title_text);
            Description= (TextView) itemView.findViewById(R.id.description_text);
            Date= (TextView) itemView.findViewById(R.id.date_text);
            Thumbnail= (ImageView) itemView.findViewById(R.id.thumb_img);
            cardView= (CardView) itemView.findViewById(R.id.cardview);
        }
    }
}

