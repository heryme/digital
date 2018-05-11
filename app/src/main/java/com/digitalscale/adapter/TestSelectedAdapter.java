package com.digitalscale.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.digitalscale.R;
import com.digitalscale.model.Food;

import java.util.ArrayList;

/**
 * Created by Vishal Gadhiya on 4/13/2017.
 */

public class TestSelectedAdapter extends BaseAdapter {

    Context context;
    ArrayList<Food> selectedFoodList;
    private LayoutInflater inflater;
    boolean isFoodModeEdit;
    ViewHolder viewHolder;

    public TestSelectedAdapter(Context context, ArrayList<Food> list, boolean isFoodModeEdit) {
        this.context = context;
        //selectedFoodList = new ArrayList<>();
        this.selectedFoodList = list;
        this.isFoodModeEdit = isFoodModeEdit;
    }

    @Override
    public int getCount() {
        return this.selectedFoodList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.selectedFoodList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_selected_food, null);

            viewHolder.tvFoodName = (TextView) convertView.findViewById(R.id.tv_selected_food_name);
            viewHolder.tvFoodDesc = (TextView) convertView.findViewById(R.id.tv_selected_food_desc);
            viewHolder.tvWeightUnit = (TextView) convertView.findViewById(R.id.tv_selected_food_weight_unit);
            viewHolder.edtWeight = (EditText) convertView.findViewById(R.id.edt_selected_food_cal);
            //attach the TextWatcher listener to the EditText
            viewHolder.edtWeight.addTextChangedListener(new MyTextWatcher(convertView));
            viewHolder.ivUnitSetting = (ImageView) convertView.findViewById(R.id.iv_selected_food_setting);
            viewHolder.ivRemoveFood = (ImageView) convertView.findViewById(R.id.iv_selected_food_remove);

            //convertView.setTag(viewHolder);
        } /*else {
            viewHolder = (ViewHolder) convertView.getTag();
        }*/

        viewHolder.edtWeight = (EditText) convertView.findViewById(R.id.edt_selected_food_cal);
        final Food foodItem = (Food) getItem(i);
        viewHolder.tvFoodName.setText(foodItem.getFoodName());
        viewHolder.tvFoodDesc.setText(foodItem.getDescription());
        if (isFoodModeEdit)
            viewHolder.edtWeight.setText(foodItem.getQuantity());
        else
            viewHolder.edtWeight.setText("");

        viewHolder.tvWeightUnit.setText("gm");


        /*ivUnitSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unitSettingPopUp(holder.ivUnitSetting, holder.tvQuestion2GainWeightUnit, foodItem);
            }
        });*/

        viewHolder.ivRemoveFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedFoodList.contains(foodItem)) {
                    selectedFoodList.remove(foodItem);
                    notifyDataSetChanged();
                }
            }
        });


        viewHolder.edtWeight.setTag(foodItem);
        return convertView;
    }

    public ArrayList<Food> getAddedFoodList() {
        return selectedFoodList;
    }

    public class ViewHolder {
        TextView tvFoodName;
        TextView tvFoodDesc;
        TextView tvWeightUnit;
        EditText edtWeight;
        ImageView ivUnitSetting;
        ImageView ivRemoveFood;

    }

    public class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            EditText editText = (EditText) view.findViewById(R.id.edt_selected_food_cal);
            Food food = (Food) editText.getTag();
            Log.d("TAG", "food >> " + food);
            if (food != null) {
                food.setQuantity("" + s.toString());
            }
        }
    }
}
