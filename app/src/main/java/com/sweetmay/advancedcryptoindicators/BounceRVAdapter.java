package com.sweetmay.advancedcryptoindicators;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BounceRVAdapter extends RecyclerView.Adapter<BounceRVAdapter.ViewHolder> {


    private int mExpandedPosition= -1;
    private RecyclerView recyclerView;
    private ArrayList<BounceEntity> valuesList = new ArrayList<>();

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
        TextView value;
        TextView target;
        ImageView expandButton;

        ConstraintLayout parent;

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
            value = itemView.findViewById(R.id.indicator_value);
            target = itemView.findViewById(R.id.target);
            expandButton = itemView.findViewById(R.id.expand_button);

            parent = itemView.findViewById(R.id.parent_view_card);

        }
            void setData(int position){
                BounceEntity entity = valuesList.get(position);
                coin.setText(entity.getCoin().toUpperCase());
                indicator.setText(entity.getIndicator());

                if(Float.parseFloat(entity.getIndicator()) <=50){
                    buySellIcon.setImageResource(R.drawable.ic_trending_up_24px);
                    buySellText.setText(R.string.buy);
                }else {
                    buySellIcon.setImageResource(R.drawable.ic_trending_down_24px);
                    buySellText.setText(R.string.sell);
                }

                buySellIcon.getDrawable().setTint((Integer) evaluator.evaluate(Float.parseFloat(entity.getIndicator())/100, Color.GREEN, Color.RED));
                Picasso.get().load(entity.getImageUrl()).into(iconCoin);
            }
        }
    }

