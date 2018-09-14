package com.pavan.githubassignment.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pavan.githubassignment.R;
import com.pavan.githubassignment.api.models.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepoAdapter extends RecyclerView.Adapter {

    private LinearLayoutManager linearLayoutManager;
    private int lastVisibleItem, totalItemCount;

    private List<Item> itemList;
    private OnItemClickListener onItemClickListener;
    private boolean showPic;

    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public RepoAdapter(boolean showPic, RecyclerView recyclerView) {
        this.itemList = new ArrayList<>();
        this.showPic = showPic;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView,  int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (!loading && totalItemCount - 1 <= lastVisibleItem && lastVisibleItem > itemList.size() - 5) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    public void updateList(List<Item> itemList) {
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    public void clearList() {
        this.itemList.clear();
        notifyDataSetChanged();
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RepoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_repo_details, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((RepoViewHolder) holder).bindData(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class RepoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageViewProfile)
        ImageView imageViewProfile;

        @BindView(R.id.textViewName)
        TextView textViewName;

        @BindView(R.id.textViewFullName)
        TextView textViewFullName;

        @BindView(R.id.textViewWatchCommitCount)
        TextView textViewWatchCommitCount;

        RepoViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onItemClickListener.onItemClick(itemList.get(position));
        }

        void bindData(Item item) {
            if (item != null) {
                textViewName.setText(item.getName());
                textViewFullName.setText(item.getFullName());

                if (showPic) {
                    Picasso.get().load(item.getOwner().getAvatarUrl())
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher)
                            .into(imageViewProfile);
                } else {
                    imageViewProfile.setVisibility(View.GONE);
                }

                String stringBuilder = "Watcher Count: " +
                        item.getWatchersCount() +
                        ", Forks Count: " +
                        item.getForksCount();

                textViewWatchCommitCount.setText(stringBuilder);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

}