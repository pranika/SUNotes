package com.example.kupal.sunotes.BusTimingPackage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kupal.sunotes.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ToRecyclerViewAdapter extends RecyclerView.Adapter<ToRecyclerViewAdapter.ViewHolder> {
    OnItemClickListener tItemClickListener;
    private List<Map<String, ?>> movieData;
    private Context context;
    private List<String> busNameList = new ArrayList<String>();
    private List<String> busTimeList = new ArrayList<String>();
    String busNameString;
    String busTimeString;

    public interface OnItemClickListener {
        public void onItemClick(View view, int pos, String busName);
    }

    public void setOnItemClickListener(final OnItemClickListener tItemClickListener) {
        this.tItemClickListener = tItemClickListener;
    }

    public ToRecyclerViewAdapter(Context myContext, List<String> nameList, List<String> timeList) {
        this.busNameList = nameList;
        this.busTimeList = timeList;
        this.context = myContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View recyclerView;
        recyclerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_from_campus, parent, false);
        ViewHolder vh = new ViewHolder(recyclerView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        busNameString = busNameList.get(position);
        busTimeString = busTimeList.get(position);
        holder.bindCardData();
    }

    @Override
    public int getItemCount() {
        return busNameList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView busName, busTime;

        public ViewHolder(View itemView) {
            super(itemView);
            busName = (TextView) itemView.findViewById(R.id.busName);
            busTime = (TextView) itemView.findViewById(R.id.busTimeText);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tItemClickListener != null) {
                        tItemClickListener.onItemClick(v, getPosition(), busNameList.get(getPosition()));
                    }
                }
            });
        }

        public void bindCardData() {
            busName.setText(busNameString);
            busTime.setText(busTimeString);
        }
    }
}

