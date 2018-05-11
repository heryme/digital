package com.digitalscale.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digitalscale.R;
import com.digitalscale.model.FoodHistory;

import java.util.ArrayList;

/**
 * Created by Vishal Gadhiya on 4/3/2017.
 */

public class FoodHistoryAdapter extends RecyclerView.Adapter<FoodHistoryAdapter.MyViewHolder> {

    ArrayList<FoodHistory> foodHistoryList;

    public FoodHistoryAdapter(ArrayList<FoodHistory> list) {
        this.foodHistoryList = list;
    }

    @Override
    public FoodHistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_history, null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FoodHistoryAdapter.MyViewHolder holder, int position) {
        FoodHistory foodHistory = foodHistoryList.get(position);
        holder.tvFoodName.setText(foodHistory.getFoodName());
        holder.tvFoodDesc.setText(foodHistory.getDescription());
        holder.tvFoodCal.setText("" + Float.parseFloat(foodHistory.getQty()));
    }

    @Override
    public int getItemCount() {
        return foodHistoryList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvFoodName;
        public TextView tvFoodDesc;
        public TextView tvFoodCal;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvFoodName = (TextView) itemView.findViewById(R.id.tv_item_food_history_name);
            tvFoodDesc = (TextView) itemView.findViewById(R.id.tv_item_food_history_desc);
            tvFoodCal = (TextView) itemView.findViewById(R.id.tv_item_food_history_qty);
        }
    }
}
