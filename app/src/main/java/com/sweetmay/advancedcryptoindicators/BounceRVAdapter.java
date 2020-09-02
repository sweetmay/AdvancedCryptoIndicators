package com.sweetmay.advancedcryptoindicators;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.transition.TransitionManager;;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BounceRVAdapter extends RecyclerView.Adapter<BounceRVAdapter.ViewHolder> {

    private Context context;
    private int mExpandedPosition= -1;
    private RecyclerView recyclerView;
    private ArrayList<BounceEntity> valuesList = new ArrayList<>();

    public BounceRVAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bounce_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(position);
        final boolean isExpanded = position == mExpandedPosition;
        holder.expandableView.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.materialCardView.setActivated(isExpanded);
        if(!isExpanded){
            holder.expandIcon.setImageResource(R.drawable.ic_expand_more_24px);
        }else {
            holder.expandIcon.setImageResource(R.drawable.ic_expand_less_24px);
        }
        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mExpandedPosition = isExpanded?-1:position;

                TransitionManager.beginDelayedTransition(recyclerView);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
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
        TextView buySellText;

        ArgbEvaluator evaluator;

        ImageView buySellIcon;
        ImageView iconCoin;

        LinearLayout expandableView;
        TextView possibleEntryPrice;
        TextView stopLoss;
        TextView target;
        ImageView expandIcon;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            evaluator = new ArgbEvaluator();
            materialCardView = itemView.findViewById(R.id.bounce_card_view);
            coin = itemView.findViewById(R.id.coin_name_bounce);
            indicator = itemView.findViewById(R.id.indicator_bounce);
            iconCoin = itemView.findViewById(R.id.icon_coin);
            buySellText = itemView.findViewById(R.id.buy_sell_text);
            buySellIcon = itemView.findViewById(R.id.buy_sell_icon);


            expandableView = itemView.findViewById(R.id.expandable);
            possibleEntryPrice = itemView.findViewById(R.id.buy_price);
            stopLoss = itemView.findViewById(R.id.stop_loss);
            target = itemView.findViewById(R.id.target);
            expandIcon = itemView.findViewById(R.id.expand_button);


        }
            void setData(int position){
                BounceEntity entity = valuesList.get(position);
                Integer color = (Integer) evaluator.evaluate(Float.parseFloat(entity.getIndicator())/100, Color.GREEN, Color.RED);
                coin.setText(entity.getCoin().toUpperCase());
                String tmp = context.getResources().getString(R.string.probability) + entity.getProbabilityRate() + "%";
                indicator.setText(tmp);
                indicator.setTextColor(color);

                if(Float.parseFloat(entity.getIndicator()) <=50){
                    buySellIcon.setImageResource(R.drawable.ic_trending_up_24px);
                    buySellText.setText(R.string.buy);
                }else {
                    buySellIcon.setImageResource(R.drawable.ic_trending_down_24px);
                    buySellText.setText(R.string.sell);
                }

                buySellIcon.getDrawable().setTint(color);
                Picasso.get().load(entity.getImageUrl()).into(iconCoin);

                tmp = context.getResources().getString(R.string.stop_loss) +  entity.getStopLoss() + entity.getLoss();
                stopLoss.setText(tmp);
                tmp = context.getResources().getString(R.string.target) + entity.getTarget() + entity.getProfit();
                target.setText(tmp);
                tmp = context.getResources().getString(R.string.entry) + entity.getEntryPrice();
                possibleEntryPrice.setText(tmp);
            }
    }
    }

