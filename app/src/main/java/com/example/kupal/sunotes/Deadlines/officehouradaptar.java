package com.example.kupal.sunotes.Deadlines;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.kupal.sunotes.R;
import com.example.kupal.sunotes.RSSFeedDem.FeedItem;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

/**
 * Created by kupal on 4/27/2017.
 */

public class officehouradaptar extends FirebaseRecyclerAdapter<FeedItem, officehouradaptar.FeedViewHolder> {

    private Context mContext;
    private static officehouradaptar.RecyclerItemClickListener mItemClickListener;
    private int lastposition = -1;
    int layout;
    List<Map<String, ?>> mData;
    public boolean check = false;

    public interface RecyclerItemClickListener{
        public void onItemClick(View view, int position);
        //public void onItemLongClick(View view, int position);
        //public void onItemCheckBoxSelect(int position, boolean isChecked);
        public void onOverFlowButtonClick(View view, int position);
    }

    public void setOnItemClickListener(final officehouradaptar.RecyclerItemClickListener mItemClickLisn){
        mItemClickListener = mItemClickLisn;
    }


    public officehouradaptar(Class<FeedItem> modelClass, int modelLayout,
                           Class<officehouradaptar.FeedViewHolder> holder, DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, holder, ref);
        this.layout = modelLayout;
        this.mContext = context;
    }

    @Override
    protected void populateViewHolder(officehouradaptar.FeedViewHolder holder, FeedItem feed, int i) {
        YoYo.with(Techniques.FadeIn).playOn(holder.cardView);
        //TODO: Populate viewHolder by setting the movie attributes to cardview fields
        if(holder.Title!=null) {
            holder.Title.setText(feed.getTitle());
        }
        if(holder.Description!=null){
            holder.Description.setText(feed.getDescription());
        }
        if(holder.Thumbnail!=null){
            Picasso.with(mContext).load(feed.getThumbnailUrl()).into(holder.Thumbnail);
        }
        if(holder.Date!=null){
            holder.Date.setText(feed.getPubDate());
        }
    }

    public Bitmap stringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public officehouradaptar.FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View newView;
        newView = LayoutInflater.from(parent.getContext()).inflate(mModelLayout, parent, false);
        return new officehouradaptar.FeedViewHolder(newView);
    }

    //TODO: Populate ViewHolder and add listeners.
    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        TextView Title,Description,Date;
        ImageView Thumbnail,overflow;
        CardView cardView;

        public FeedViewHolder(View v) {
            super(v);
            Title= (TextView) itemView.findViewById(R.id.title_text);
            Description= (TextView) itemView.findViewById(R.id.description_text);
            Date= (TextView) itemView.findViewById(R.id.date_text);
            Thumbnail= (ImageView) itemView.findViewById(R.id.thumb_img);
            overflow = (ImageView) itemView.findViewById(R.id.overflow);
            cardView= (CardView) itemView.findViewById(R.id.cardview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });

            if (overflow != null) {
                overflow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mItemClickListener != null)
                            mItemClickListener.onOverFlowButtonClick(v, getPosition());
                    }
                });
            }
        }
    }
}
//<------------------------------------------End--------------------------------------------------->