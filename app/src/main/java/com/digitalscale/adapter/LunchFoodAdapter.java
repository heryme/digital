package com.digitalscale.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digitalscale.R;
import com.digitalscale.model.FoodHistory;
import com.digitalscale.utility.FontUtility;

import java.util.ArrayList;

/**
 * Created by Vishal Gadhiya on 4/13/2017.
 */

public class LunchFoodAdapter extends BaseAdapter {

    Context context;
    ArrayList<FoodHistory> list;
    private LayoutInflater inflater;

    public LunchFoodAdapter(Context context, ArrayList<FoodHistory> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        if (list.size() > 0) {
            return list.get(i);
        } else return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_food_history, null);
            viewHolder = new ViewHolder();
            viewHolder.ll = (LinearLayout)convertView.findViewById(R.id.ll);
            viewHolder.tvFoodName = (TextView) convertView.findViewById(R.id.tv_item_food_history_name);
            viewHolder.tvFoodDesc = (TextView) convertView.findViewById(R.id.tv_item_food_history_desc);
            viewHolder.tvFoodQty = (TextView) convertView.findViewById(R.id.tv_item_food_history_qty);
            viewHolder.tvFoodKcal = (TextView) convertView.findViewById(R.id.tv_item_food_history_kcal);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        FoodHistory foodHistory = (FoodHistory) getItem(i);

        if (foodHistory != null) {

            //Set Font Style
            FontUtility.condLight(viewHolder.tvFoodName,context);
            FontUtility.condLight( viewHolder.tvFoodDesc,context);

            /*viewHolder.tvFoodName.setText(foodHistory.getFoodName().toString().trim());
            viewHolder.tvFoodDesc.setText(foodHistory.getDescription().toString().trim());
            viewHolder.tvFoodCal.setText(foodHistory.getQty() + " gm");*/

            viewHolder.tvFoodName.setText(foodHistory.getFoodName().toString().trim());
            viewHolder.tvFoodDesc.setText(foodHistory.getDescription().toString().trim());
            viewHolder.tvFoodQty.setText(foodHistory.getQty() + " "+foodHistory.getQtyUnit());
            viewHolder.tvFoodKcal.setText(foodHistory.getKcal()+" kcal");
        }

        viewHolder.ll.setOnClickListener(null);

        return convertView;
    }

    public class ViewHolder
    {
        LinearLayout ll;
        TextView tvFoodName;
        TextView tvFoodDesc;
        TextView tvFoodQty;
        TextView tvFoodKcal;
    }

    public void updateList(ArrayList<FoodHistory> list)
    {
        this.list = list;
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });

    }
}
