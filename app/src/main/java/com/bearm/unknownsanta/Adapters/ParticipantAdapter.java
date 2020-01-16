package com.bearm.unknownsanta.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bearm.unknownsanta.Model.Participant;
import com.bearm.unknownsanta.ViewModels.ParticipantViewModel;
import com.bearm.unknownsanta.R;

import java.util.List;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.MyViewHolder> {

    private List<Participant> participantList;

    private ParticipantViewModel participantViewModel;

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvEmail;
        ImageView ivAvatar;
        ImageView btnDelete;


        MyViewHolder(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.tv_part_name);
            tvEmail = v.findViewById(R.id.tv_part_email);

            ivAvatar = v.findViewById(R.id.iv_rndm_avatar);
            btnDelete = v.findViewById(R.id.btn_delete);


        }

    }

    public ParticipantAdapter(List<Participant> myDataset, ParticipantViewModel participantViewModel) {
        this.participantList = myDataset;

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

        if (avatarName != null) {
            holder.ivAvatar.setImageResource(Integer.parseInt(avatarName));
        }


        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteParticipant(participantList.get(position));
                participantList.remove(participantList.get(position));
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

}
