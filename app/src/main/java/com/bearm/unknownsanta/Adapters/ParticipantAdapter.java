package com.bearm.unknownsanta.Adapters;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bearm.unknownsanta.Model.Participant;
import com.bearm.unknownsanta.Model.ParticipantViewModel;
import com.bearm.unknownsanta.R;

import java.util.List;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ViewHolder> {

    public List<Participant> participantList;
    public Application application;

    ParticipantViewModel participantViewModel;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvDate;
        ImageView ivAvatar;
        ImageView btnDelete;



        public ViewHolder(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.tv_part_name);
            tvDate = v.findViewById(R.id.tv_part_email);

            ivAvatar = v.findViewById(R.id.iv_rndm_avatar);
            btnDelete = v.findViewById(R.id.btn_delete);



        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ParticipantAdapter(List<Participant> myDataset, Application application) {
        participantList = myDataset;
        this.application = application;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ViewModelProvider.AndroidViewModelFactory myViewModelProviderFactory = new ViewModelProvider.AndroidViewModelFactory(application);
        participantViewModel = new ViewModelProvider((ViewModelStoreOwner) this, myViewModelProviderFactory).get(ParticipantViewModel.class);


        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.participant_list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvName.setText(participantList.get(position).name);
        holder.tvDate.setText(participantList.get(position).email);

        String avatarName = participantList.get(position).avatarName;

        if (avatarName != null) {
            holder.ivAvatar.setImageResource(Integer.parseInt(avatarName));
        }


        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                participantList.remove(participantList.get(position));
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return participantList.size();
    }

    public void setParticipants(List<Participant> participants) {
        this.participantList = participants;
        notifyDataSetChanged();
    }

    public void deleteParticipant() {
        participantViewModel.delete();

    }

}
