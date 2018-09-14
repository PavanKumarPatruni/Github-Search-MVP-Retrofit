package com.pavan.githubassignment.repo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pavan.githubassignment.R;
import com.pavan.githubassignment.api.models.Owner;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContributorAdapter extends RecyclerView.Adapter {

    private List<Owner> contributorList;
    private OnItemClickListener onItemClickListener;

    public ContributorAdapter() {
        contributorList = new ArrayList<>();
    }

    void updateList(List<Owner> contributorList) {
        this.contributorList = contributorList;
        notifyDataSetChanged();
    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_contributor, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).bindData(contributorList.get(position));
    }

    @Override
    public int getItemCount() {
        return contributorList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageViewProfile)
        ImageView imageViewProfile;

        @BindView(R.id.textViewName)
        TextView textViewName;

        MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        void bindData(Owner owner) {
            textViewName.setText(owner.getLogin());

            Picasso.get().load(owner.getAvatarUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(imageViewProfile);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(contributorList.get(getAdapterPosition()));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Owner owner);
    }
}
