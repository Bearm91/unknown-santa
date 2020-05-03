package com.bearm.unknownsanta.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.RecyclerView;

import com.bearm.unknownsanta.Model.Participant;
import com.bearm.unknownsanta.ViewModels.ParticipantViewModel;
import com.bearm.unknownsanta.R;

import java.util.List;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.MyViewHolder> {

    private List<Participant> participantList;
    private Context context;
    private ParticipantViewModel participantViewModel;

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvEmail;
        ImageView ivAvatar;
        ImageView btnDelete;
        ImageView ivCheck;


        MyViewHolder(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.tv_part_name);
            tvEmail = v.findViewById(R.id.tv_part_email);

            ivAvatar = v.findViewById(R.id.iv_rndm_avatar);
            ivCheck = v.findViewById(R.id.ic_assigned);
            btnDelete = v.findViewById(R.id.btn_delete);


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

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.participant_list_item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.tvName.setText(participantList.get(position).getName());
        holder.tvEmail.setText(participantList.get(position).getEmail());

        String avatarName = participantList.get(position).getAvatarName();

        int idReceiver = participantList.get(position).getIdReceiver();

        if (avatarName != null) {
            holder.ivAvatar.setImageResource(Integer.parseInt(avatarName));
        }

        if (idReceiver != 0){
            holder.ivCheck.setVisibility(View.VISIBLE);
        } else {
            holder.ivCheck.setVisibility(View.INVISIBLE);
        }

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
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
    private void showAlertDialogConfirmation(final int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myAlertDialogs));
        builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to delete this participant?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteParticipant(participantList.get(pos));
                        participantList.remove(participantList.get(pos));
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
