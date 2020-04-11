package com.bearm.unknownsanta.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bearm.unknownsanta.Model.Event;
import com.bearm.unknownsanta.R;
import com.bearm.unknownsanta.Helpers.SharedPreferencesHelper;
import com.bearm.unknownsanta.ViewModels.EventViewModel;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private List<Event> eventList;
    private Context context;
    private int index;
    private EventViewModel eventViewModel;
    SharedPreferencesHelper sharedPreferencesHelper;

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvDate;
        Button btnSelect;
        ImageView ivEmail;
        ImageView ivDelete;
        ImageView ivIcon;
        LinearLayout layoutEventItem;


        MyViewHolder(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.tv_ev_name);
            tvDate = v.findViewById(R.id.tv_ev_date);
            btnSelect = v.findViewById(R.id.btn_select);
            ivEmail = v.findViewById(R.id.iv_email_sent);
            ivDelete = v.findViewById(R.id.btn_event_delete);
            ivIcon = v.findViewById(R.id.iv_event_icon);
            layoutEventItem = v.findViewById(R.id.cv_event);

            sharedPreferencesHelper =  new SharedPreferencesHelper(context);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EventAdapter(List<Event> myDataset, EventViewModel eViewModel, Context myContext) {
        this.eventList = myDataset;
        this.context = myContext;
        this.index = -1;
        this.eventViewModel = eViewModel;

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

        String iconName = eventList.get(position).getIconName();
        if (iconName != null) {
            holder.ivIcon.setImageResource(Integer.parseInt(iconName));
        } else {
            holder.ivIcon.setImageResource(R.drawable.ic_gift);
        }

        if (eventList.get(position).isEmailSent()) {
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

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent(eventList.get(position));
                eventList.remove(eventList.get(position));
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
            }
        });

        //Checks selected element to change its background color
        if (index == position) {
            holder.layoutEventItem.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        } else {
            holder.layoutEventItem.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
        }
    }

    private void deleteEvent(Event event) {
        eventViewModel.deleteEvent(event.getId());
    }

    //Saves selected event into SharedPreferences2
    private void saveSelectedEvent(int position) {

        sharedPreferencesHelper.saveSelectedEvent(String.valueOf(eventList.get(position).getId()),eventList.get(position).getName(),
                eventList.get(position).getPlace(), eventList.get(position).getDate(), eventList.get(position).getExpense(),
                eventList.get(position).isAssignationDone(), eventList.get(position).isEmailSent());
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
