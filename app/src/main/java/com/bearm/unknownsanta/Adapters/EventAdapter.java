package com.bearm.unknownsanta.Adapters;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bearm.unknownsanta.Helpers.SharedPreferencesHelper;
import com.bearm.unknownsanta.MainActivity;
import com.bearm.unknownsanta.Model.Event;
import com.bearm.unknownsanta.R;
import com.bearm.unknownsanta.Helpers.SharedPreferencesHelper;
import com.bearm.unknownsanta.ViewModels.EventViewModel;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private List<Event> eventList;
    private Context context;
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
            int resourceIdImage = context.getResources().getIdentifier(iconName, "drawable",
                    context.getPackageName());
            //use this id to set the image anywhere
            holder.ivIcon.setImageResource(resourceIdImage);
        } else {
            holder.ivIcon.setImageResource(R.drawable.ic_christmas_tree);
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
                goToSelectedEvent();

            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogConfirmation(position);
            }
        });

    }

    private void deleteEvent(Event event) {
        eventViewModel.deleteEvent(event.getId());
    }

    //Saves selected event into SharedPreferences2
    //TODO fix this
    private void saveSelectedEvent(int position) {
        SharedPreferencesHelper.setSelectedEventAsCurrent(String.valueOf(eventList.get(position).getId()),eventList.get(position).getName(),
                eventList.get(position).getPlace(), eventList.get(position).getDate(), eventList.get(position).getExpense(),
                eventList.get(position).isAssignationDone(), eventList.get(position).isEmailSent(), eventList.get(position).getIconName());
    }

    private void goToSelectedEvent() {
        Intent eventInfo = new Intent(context, MainActivity.class);
        eventInfo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(eventInfo);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void setEvents(List<Event> events) {
        this.eventList = events;
        notifyDataSetChanged();
    }

    //Asks for confirmation before deleting the event
    private void showAlertDialogConfirmation(final int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to delete this event?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteEvent(eventList.get(pos));
                        eventList.remove(eventList.get(pos));
                        notifyItemRangeChanged(pos, getItemCount());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

}
