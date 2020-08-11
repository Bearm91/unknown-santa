package com.bearm.unknownsanta.adapters;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bearm.unknownsanta.helpers.SharedPreferencesHelper;
import com.bearm.unknownsanta.MainActivity;
import com.bearm.unknownsanta.model.Event;
import com.bearm.unknownsanta.R;
import com.bearm.unknownsanta.viewModels.EventViewModel;
import com.bearm.unknownsanta.databinding.EventListItemBinding;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private List<Event> eventList;
    private Context context;
    private EventViewModel eventViewModel;


    class MyViewHolder extends RecyclerView.ViewHolder {
        private SharedPreferencesHelper sharedPreferencesHelper;
        private EventListItemBinding itemBinding;

        MyViewHolder(EventListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            sharedPreferencesHelper = new SharedPreferencesHelper(context);
        }

        public void bind(Event event){
            itemBinding.setEvent(event);
            itemBinding.executePendingBindings();
        }
    }

    public EventAdapter(List<Event> myDataset, EventViewModel eViewModel, Context myContext) {
        this.eventList = myDataset;
        this.context = myContext;
        this.eventViewModel = eViewModel;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        EventListItemBinding itemBinding = EventListItemBinding.inflate(layoutInflater, parent, false);
        return new MyViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Event myEvent = eventList.get(position);
        holder.bind(myEvent);

        String iconName = eventList.get(position).getIconName();
        if (iconName != null) {
            int resourceIdImage = context.getResources().getIdentifier(iconName, "drawable",
                    context.getPackageName());
            //use this id to set the image anywhere
            holder.itemBinding.ivEventIcon.setImageResource(resourceIdImage);
        } else {
            holder.itemBinding.ivEventIcon.setImageResource(R.drawable.ic_christmas_tree);
        }

        //Checks if white envelope should be visible (email is sent for this event)
        if (eventList.get(position).isEmailSent()) {
            holder.itemBinding.ivEmailSent.setVisibility(View.VISIBLE);
        } else {
            holder.itemBinding.ivEmailSent.setVisibility(View.GONE);
        }

        holder.itemBinding.layoutEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSelectedEvent(position);
                goToSelectedEvent();

            }
        });

        holder.itemBinding.btnEventDelete.setOnClickListener(new View.OnClickListener() {
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
        SharedPreferencesHelper.setSelectedEventAsCurrent(String.valueOf(eventList.get(position).getId()), eventList.get(position).getName(),
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
    private void showAlertDialogConfirmation(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.dialog_confirmation_title)
                .setMessage(R.string.dialog_delete_event_message)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteEvent(eventList.get(pos));
                        eventList.remove(eventList.get(pos));
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

}
