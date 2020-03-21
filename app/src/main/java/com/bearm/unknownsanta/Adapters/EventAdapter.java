package com.bearm.unknownsanta.Adapters;


import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bearm.unknownsanta.Model.Event;
import com.bearm.unknownsanta.R;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private List<Event> eventList;
    private Context context;
    private int index;

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvDate;
        Button btnSelect;
        ImageView ivEmail;
        LinearLayout layoutEventItem;


        MyViewHolder(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.tv_ev_name);
            tvDate = v.findViewById(R.id.tv_ev_date);
            btnSelect = v.findViewById(R.id.btn_select);
            ivEmail = v.findViewById(R.id.iv_email_sent);
            layoutEventItem = v.findViewById(R.id.cv_event);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EventAdapter(List<Event> myDataset, Context myContext) {
        eventList = myDataset;
        context = myContext;
        index = -1;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.tvName.setText(eventList.get(position).getName());
        holder.tvDate.setText(eventList.get(position).getDate());

        if(eventList.get(position).isEmailSent()){
            holder.ivEmail.setVisibility(View.VISIBLE);
        } else {
            holder.ivEmail.setVisibility(View.GONE);
        }

        holder.layoutEventItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSelectedEvent(position);
                index = position;
                notifyDataSetChanged();
            }
        });

        //Checks selected element to change its background color
        if (index == position) {
            holder.layoutEventItem.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        } else {
            holder.layoutEventItem.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
        }
    }

    //Saves selected event into in SharedPreferences2
    private void saveSelectedEvent(int position) {
        SharedPreferences eventSelected =
                context.getSharedPreferences("my_us_event", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = eventSelected.edit();
        editor.putString("eventId2", String.valueOf(eventList.get(position).getId()));
        editor.putString("eventName2", eventList.get(position).getName());
        editor.putString("eventPlace2", eventList.get(position).getPlace());
        editor.putString("eventDate2", eventList.get(position).getDate());
        editor.putString("eventExpense2", eventList.get(position).getExpense());
        editor.putBoolean("eventIsEmailSent2", eventList.get(position).isEmailSent());

        editor.apply();
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void setEvents(List<Event> events) {
        this.eventList = events;
        notifyDataSetChanged();
    }

}
