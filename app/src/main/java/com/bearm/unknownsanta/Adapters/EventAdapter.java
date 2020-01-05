package com.bearm.unknownsanta.Adapters;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bearm.unknownsanta.Model.Event;
import com.bearm.unknownsanta.R;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<Event> eventList;
    private Context context;
    private int eventId;
    public String eventDate;
    public String eventPlace;
    public String eventExpense;
    int index;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvDate;
        Button btnSelect;
        LinearLayout layoutEventItem;


        public ViewHolder(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.tv_ev_name);
            tvDate = v.findViewById(R.id.tv_ev_date);
            btnSelect = v.findViewById(R.id.btn_select);
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e("EVENTADAPTER", "onCreateViewHolder");
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.tvName.setText(eventList.get(position).name);
        holder.tvDate.setText(eventList.get(position).date);

        holder.layoutEventItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSelectedEvent(position);
                index = position;
                notifyDataSetChanged();
            }
        });

        if (index == position) {
            holder.layoutEventItem.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        } else {
            holder.layoutEventItem.setBackgroundColor(context.getResources().getColor(R.color.white_color));

        }
    }

    private void saveSelectedEvent(int position) {
        SharedPreferences eventSelected =
                context.getSharedPreferences("my_us_event", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = eventSelected.edit();
        editor.putString("eventId", String.valueOf(eventId));
        editor.putString("eventName", eventList.get(position).name);
        editor.putString("eventPlace", eventList.get(position).place);
        editor.putString("eventDate", eventList.get(position).date);
        editor.putString("eventExpense", eventList.get(position).expense);

        editor.apply();

        Log.e("EVENTADAPTER", "SharedPrefefences saved");
    }

    @Override
    public int getItemCount() {
        Log.e("EVENTADAPTER", "ItemCount= " + eventList.size());
        return eventList.size();
    }

    public void setEvents(List<Event> participants) {
        this.eventList = participants;
        notifyDataSetChanged();
    }

}
