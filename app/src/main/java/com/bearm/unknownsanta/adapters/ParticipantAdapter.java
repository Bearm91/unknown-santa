package com.bearm.unknownsanta.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.RecyclerView;

import com.bearm.unknownsanta.databinding.ParticipantListItemBinding;
import com.bearm.unknownsanta.model.Participant;
import com.bearm.unknownsanta.viewModels.ParticipantViewModel;
import com.bearm.unknownsanta.R;

import java.util.List;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.MyViewHolder> {

    private List<Participant> participantList;
    private Context context;
    private ParticipantViewModel participantViewModel;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ParticipantListItemBinding itemBinding;

        MyViewHolder(ParticipantListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public void bind(Participant participant) {
            itemBinding.setParticipant(participant);
            itemBinding.executePendingBindings();
        }
    }

    public ParticipantAdapter(List<Participant> myDataset, ParticipantViewModel participantViewModel, Context context) {
        this.participantList = myDataset;
        this.context = context;
        this.participantViewModel = participantViewModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ParticipantListItemBinding itemBinding = ParticipantListItemBinding.inflate(layoutInflater, parent, false);

        return new MyViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Participant myParticipant = participantList.get(position);
        holder.bind(myParticipant);

        String avatarName = participantList.get(position).getAvatarName();

        int idReceiver = participantList.get(position).getIdReceiver();

        if (avatarName != null) {
            int resourceIdImage = context.getResources().getIdentifier(avatarName, "drawable",
                    context.getPackageName());
            //use this id to set the image anywhere
            holder.itemBinding.ivRndmAvatar.setImageResource(resourceIdImage);
        } else {
            holder.itemBinding.ivRndmAvatar.setImageResource(R.drawable.ic_deer);
        }

        if (idReceiver != 0) {
            holder.itemBinding.ivAssigned.setVisibility(View.VISIBLE);
        } else {
            holder.itemBinding.ivAssigned.setVisibility(View.INVISIBLE);
        }

        holder.itemBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogConfirmation(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return participantList.size();
    }

    //Loads participant list from database
    public void setParticipants(List<Participant> participants) {
        this.participantList = participants;
        notifyDataSetChanged();
    }

    //Deletes participant from database
    private void deleteParticipant(Participant participant) {
        participantViewModel.delete(participant);
    }

    //Asks for confirmation before deleting the participant
    private void showAlertDialogConfirmation(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myAlertDialogs));
        builder.setTitle(R.string.dialog_confirmation_title)
                .setMessage(R.string.dialog_delete_participant)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteParticipant(participantList.get(pos));
                        participantList.remove(participantList.get(pos));
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
