package com.sweetmay.advancedcryptoindicators;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SearchRVAdapter extends RecyclerView.Adapter<SearchRVAdapter.ViewHolder> {

    private LinkedList<String> valuesList;
    private OnSearchRVItemClick onSearchRVItemClick;

    public SearchRVAdapter(OnSearchRVItemClick onSearchRVItemClick){
        this.onSearchRVItemClick = onSearchRVItemClick;
        valuesList =  new LinkedList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setSearchItem(position);
        onClick(holder, position);
    }

    private void onClick(@NonNull ViewHolder holder, int position) {
        holder.searchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearchRVItemClick.onRVSearchClick(valuesList.get(position));
            }
        });
    }

    public void invalidateRV(LinkedList<String> coins){
        valuesList = coins;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(valuesList == null || valuesList.size() == 0) {
            return 0;
        }else {
            return valuesList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView searchItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            searchItem = itemView.findViewById(R.id.search_item);
        }

        public void setSearchItem(int position){
            searchItem.setText(valuesList.get(position));
        }
    }
}
