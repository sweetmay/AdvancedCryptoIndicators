package com.sweetmay.advancedcryptoindicators.Fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.sweetmay.advancedcryptoindicators.R;

import java.util.ArrayList;

public class BounceRVAdapter extends RecyclerView.Adapter<BounceRVAdapter.ViewHolder> {

    private ArrayList<BounceEntity> valuesList = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bounce_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        if(valuesList == null || valuesList.size() == 0) {
            return 0;
        }else {
            return valuesList.size();
        }
    }

    public void invalidateRV(BounceEntity entity) {
        valuesList.add(entity);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        MaterialCardView materialCardView;
        TextView coin;
        TextView indicator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            materialCardView = itemView.findViewById(R.id.bounce_card_view);
            coin = itemView.findViewById(R.id.coin_name_bounce);
            indicator = itemView.findViewById(R.id.indicator_bounce);
        }
            void setData(int position){
                BounceEntity entity = valuesList.get(position);
                coin.setText(entity.getCoin());
                indicator.setText(entity.getIndicator());
            }
        }
    }

