package com.sweetmay.advancedcryptoindicators;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SearchRVAdapter extends RecyclerView.Adapter<SearchRVAdapter.ViewHolder> {
    private Map<String, String> coinsAndImages;
    private ArrayList<String> coins;
    private OnSearchRVItemClick onSearchRVItemClick;

    public SearchRVAdapter(OnSearchRVItemClick onSearchRVItemClick){
        this.onSearchRVItemClick = onSearchRVItemClick;
        coinsAndImages =  new LinkedHashMap<>();
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
                onSearchRVItemClick.onRVSearchClick(coins.get(position));
            }
        });
    }

    public void invalidateRV(Map<String, String> coinAndImage){
        coinsAndImages = coinAndImage;
        coins = new ArrayList<>(coinsAndImages.keySet());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(coinsAndImages == null || coinsAndImages.size() == 0) {
            return 0;
        }else {
            return coinsAndImages.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView materialCardView;
        TextView searchItem;
        ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon_coin_search);
            materialCardView = itemView.findViewById(R.id.card_view_search);
            searchItem = itemView.findViewById(R.id.search_item);
        }

        public void setSearchItem(int position){
            searchItem.setText(coins.get(position));
            Picasso.get().load(coinsAndImages.get(coins.get(position))).into(icon);
        }
    }
}
