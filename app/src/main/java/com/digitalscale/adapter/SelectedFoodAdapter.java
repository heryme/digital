package com.digitalscale.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.digitalscale.R;
import com.digitalscale.model.Food;
import com.digitalscale.tools.Constant;
import com.digitalscale.utility.FontUtility;
import com.digitalscale.utility.FormulaUtility;

import java.util.ArrayList;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

/**
 * Created by Vishal Gadhiya on 4/4/2017.
 */

@SuppressLint("NewApi")
public class SelectedFoodAdapter extends RecyclerView.Adapter<SelectedFoodAdapter.MyViewHolder> {

    private static final String TAG = SelectedFoodAdapter.class.getSimpleName();

    Context mContext;
    ArrayList<Food> selectedFoodList;
    boolean isFoodModeEdit;
    RecyclerView recyclerView;

    MyCustomEditTextListener myCustomEditTextListener;

    private String[] mDataset;

    private boolean scrolling = true;

    private Food foodItem;

    /*public SelectedFoodAdapter(String[] myDataset) {
        mDataset = myDataset;
    }*/

    public SelectedFoodAdapter(Context context, ArrayList<Food> list, boolean isFoodModeEdit, RecyclerView recyclerView) {
        this.mContext = context;
        this.selectedFoodList = list;
        this.isFoodModeEdit = isFoodModeEdit;
        this.recyclerView = recyclerView;
        mDataset = new String[selectedFoodList.size()];

        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                // super.onScrollStateChanged(recyclerView, newState);
                scrolling = scrollState != SCROLL_STATE_IDLE;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_food, null);

        return new SelectedFoodAdapter.MyViewHolder(view, new MyCustomEditTextListener());
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        //Set Font Style
        FontUtility.condBold(holder.tvFoodName,mContext);
        FontUtility.condLight(holder.tvFoodDesc,mContext);
        FontUtility.condLight(holder.tvWeightUnit,mContext);


        foodItem = selectedFoodList.get(position);
        holder.tvFoodName.setText(foodItem.getFoodName());
        holder.tvFoodDesc.setText(foodItem.getDescription());

        holder.myCustomEditTextListener.updatePosition(holder.getAdapterPosition(), holder);
        //holder.edtQuestion2GainWeight.setText(mDataset[holder.getAdapterPosition()]);


        //holder.edtQuestion2GainWeight.setText("");
        if (foodItem != null && foodItem.getQuantity().length() > 0)
            holder.edtWeight.setText(foodItem.getQuantity());
        else
            holder.edtWeight.setText("");

        Log.i(TAG, "foodItem.getCalorie() >> " + foodItem.getCalorie());

        if (isFoodModeEdit) {
            holder.tvCalculatedCal.setText(foodItem.getCalorie() + " Kcal");
            holder.edtWeight.setText(foodItem.getQuantity());
            holder.tvWeightUnit.setText("g");
        }else {
            holder.tvCalculatedCal.setText(foodItem.getCalorie() + " Kcal");
        }


        holder.tvWeightUnit.setText(foodItem.getWeightReadingUnit());


        holder.ivUnitSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unitSettingPopUp(holder.ivUnitSetting, holder.tvWeightUnit, holder.edtWeight, foodItem);
            }
        });

        holder.ivRemoveFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"remove item position >> "+position);
                Log.d(TAG,"selectedFoodList.get(position).getFoodName() >> "+selectedFoodList.get(position).getFoodName());
                Log.d(TAG,"foodItem.getFoodName() >> "+foodItem.getFoodName());
                Log.d(TAG,"selectedFoodList.indexOf(foodItem) >> "+selectedFoodList.indexOf(foodItem));

                //if (selectedFoodList.contains(foodItem)) {
                    selectedFoodList.remove(position);
                    notifyDataSetChanged();
                //}
            }
        });

        holder.edtWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Log.d("TAG","hasFocus >> "+hasFocus);
                    holder.edtWeight.setSelection(0,holder.edtWeight.getText().length());
                    recyclerView.scrollToPosition(getItemCount());
                }
            }
        });


        /*holder.edtWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.edtWeight.setCursorVisible(true);
                holder.edtWeight.setSelection(0, holder.edtWeight.getText().length());
                holder.edtWeight.setSelectAllOnFocus(true);
                holder.edtWeight.selectAll();
            }
        });*/
        /* */
        /*holder.edtWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "In");
                if (true) {
                    String foodWeight = editable.toString();
                    if (foodWeight != null && foodWeight.length() > 0) {
                        String weightInGm = FormulaUtility.convertOldToNewUnit(holder.tvWeightUnit.getText().toString(), Constant.UNIT_GM, foodWeight);
                        String tempCal = FormulaUtility.calculateCalories(weightInGm, foodItem.getFoodKcal(), foodItem.getFoodQty());
                        holder.tvCalculatedCal.setText(tempCal + " kcal");
                        //Log.i(TAG,"tempCal >> "+tempCal);
                        foodItem.setCalorie(tempCal);
                    } else {
                        foodItem.setCalorie("0");
                        holder.tvCalculatedCal.setText(0 + " kcal");
                    }
                }
            }
        });*/

        /*holder.tvQuestion2GainWeightUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodItem.setQuantity(holder.edtQuestion2GainWeight.getText().toString());
                Log.d("TAG", "edt value >> " + holder.edtQuestion2GainWeight.getText().toString());
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return selectedFoodList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;/*super.getItemViewType(position);*/
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvFoodName;
        TextView tvFoodDesc;
        TextView tvWeightUnit;
        EditText edtWeight;
        ImageView ivUnitSetting;
        ImageView ivRemoveFood;
        TextView tvCalculatedCal;
        public MyCustomEditTextListener myCustomEditTextListener;

        public MyViewHolder(View itemView, MyCustomEditTextListener myCustomEditTextListener) {
            super(itemView);

            tvFoodName = (TextView) itemView.findViewById(R.id.tv_selected_food_name);
            tvFoodDesc = (TextView) itemView.findViewById(R.id.tv_selected_food_desc);
            tvWeightUnit = (TextView) itemView.findViewById(R.id.tv_selected_food_weight_unit);
            edtWeight = (EditText) itemView.findViewById(R.id.edt_selected_food_cal);
            ivUnitSetting = (ImageView) itemView.findViewById(R.id.iv_selected_food_setting);
            ivRemoveFood = (ImageView) itemView.findViewById(R.id.iv_selected_food_remove);
            tvCalculatedCal = (TextView) itemView.findViewById(R.id.tv_selected_food_calculated_cal);

            this.myCustomEditTextListener = myCustomEditTextListener;
            edtWeight.addTextChangedListener(myCustomEditTextListener);
        }
    }

    private void unitSettingPopUp(ImageView imageView, final TextView tvWeightUnit, final EditText edtWeight, final Food foodItem) {
        PopupMenu popupMenu = new PopupMenu(mContext, imageView);
        //Inflating the Popup using xml file
        popupMenu.getMenuInflater()
                .inflate(R.menu.menu_weight_reading_unit, popupMenu.getMenu());
        String tempVal;

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            String selectedUnit, tempVal;
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                selectedUnit = foodItem.getWeightReadingUnit();
                tempVal = edtWeight.getText().toString();

                switch (item.getItemId()) {
                    case R.id.menu_weight_reading_gm:
                        performConversion(Constant.UNIT_GM);
                        break;
                    case R.id.menu_weight_reading_lb:
                        performConversion(Constant.UNIT_LB);
                        break;
                    case R.id.menu_weight_reading_oz:
                        performConversion(Constant.UNIT_OZ);
                        break;
                    case R.id.menu_weight_reading_ml:
                        performConversion(Constant.UNIT_ML);
                        break;
                }
                return true;
            }

            private void performConversion(String unit) {
                if (tempVal != null && tempVal.length() > 0) {
                    tvWeightUnit.setText(unit);
                    foodItem.setWeightReadingUnit(unit);
                    edtWeight.setText(FormulaUtility.convertOldToNewUnit(unit, selectedUnit, tempVal));
                } else {
                    edtWeight.requestFocus();
                    edtWeight.setCursorVisible(true);
                    edtWeight.setError("Enter weight");
                }
            }
        });
        popupMenu.show();
    }

    public ArrayList<Food> getAddedFoodList() {
        return selectedFoodList;
    }


    /*public boolean checkEditTxtValue() {
        recyclerView.invalidate();
        int childCount = recyclerView.getAdapter().getItemCount();
        Log.d("childCount >> ", "" + childCount);

        for (int i = 0; i < childCount; i++) {
            if (recyclerView.findViewHolderForAdapterPosition(i) instanceof MyViewHolder) {
                Log.d(TAG, "i >> " + i);
                MyViewHolder childHolder = (MyViewHolder) recyclerView.findViewHolderForAdapterPosition(i);*//*findViewHolderForLayoutPosition(i);*//*
                String temp = childHolder.edtWeight.getText().toString();
                Log.d("temp >> ", temp);
                if (temp.length() <= 0) {
                    childHolder.edtWeight.requestFocus();
                    childHolder.edtWeight.setCursorVisible(true);
                    childHolder.edtWeight.setError("Calories is required");
                    return false;
                } else {
                    if (Float.valueOf(temp) == 0f) {
                        childHolder.edtWeight.requestFocus();
                        childHolder.edtWeight.setSelection(childHolder.edtWeight.getText().length() - 1, 0);
                        childHolder.edtWeight.setError("0 is not valid quantity");
                        return false;
                    }else {
                        Log.d("TAG", "getAddedFoodList().get(i) >> " + getAddedFoodList().get(i));
                        getAddedFoodList().get(i).setQuantity(temp);
                    }
                }
            }
        }
        return true;
    }*/

    public boolean checkEditTxtValue() {
        recyclerView.invalidate();
        final int childCount = selectedFoodList.size();
        Log.d("childCount >> ", "" + childCount);
        final int a;
        for (int i = 0; i < childCount; i++) {
            //String cal = selectedFoodList.get(i).getQuantity();

            String temp = selectedFoodList.get(i).getQuantity();
            Log.d("temp >> ", temp);
            if (temp.length() <= 0) {
                a = i;
                recyclerView.scrollToPosition(i);
                new Thread() {
                    @Override
                    public void run() {
                        ((Activity) (mContext)).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(300);
                                    MyViewHolder childHolder = (MyViewHolder) recyclerView.findViewHolderForLayoutPosition(a);
                                    childHolder.edtWeight.requestFocus();
                                    childHolder.edtWeight.setCursorVisible(true);
                                    childHolder.edtWeight.setError("Calorie is required");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                }.start();


                return false;
            } else {
                if (Float.valueOf(temp) == 0f) {
                    recyclerView.scrollToPosition(i);
                    a = i;

                    new Thread() {
                        @Override
                        public void run() {
                            ((Activity) (mContext)).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(300);
                                        MyViewHolder childHolder = (MyViewHolder) recyclerView.findViewHolderForLayoutPosition(a);
                                        Log.d(TAG, "childHolder >> " + childHolder);
                                        childHolder.edtWeight.requestFocus();
                                        childHolder.edtWeight.setCursorVisible(true);
                                        childHolder.edtWeight.setError("0 is not valid quantity");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        }
                    }.start();

                    return false;
                } else {
                    Log.d("TAG", "getAddedFoodList().get(i) >> " + getAddedFoodList().get(i));
                    getAddedFoodList().get(i).setQuantity(temp);
                }
            }

        }
        return true;
    }

    private class MyCustomEditTextListener implements TextWatcher {

        private int positionTemp;
        private MyViewHolder holderTemp;

        public void updatePosition(int position, MyViewHolder holder) {
            this.positionTemp = position;
            this.holderTemp = holder;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            Food food = selectedFoodList.get(positionTemp);
            //food = selectedFoodList.get(positionTemp);
            food.setQuantity(this.holderTemp.edtWeight.getText().toString());

            //foodItem.setCalorie(String.valueOf(positionTemp));
            Log.d("TAG", "foodItem.getCalorie() >> " + food.getCalorie());
            Log.d("TAG", "editable.toString() >> " + editable.toString());


            String foodWeight = editable.toString();
            if (foodWeight != null && foodWeight.length() > 0) {
                String weightInGm = FormulaUtility.convertOldToNewUnit(holderTemp.tvWeightUnit.getText().toString(), Constant.UNIT_GM, foodWeight);
                String tempCal = FormulaUtility.calculateCalories(weightInGm, food.getFoodKcal(), food.getFoodQty());
                holderTemp.tvCalculatedCal.setText(tempCal + " kcal");
                Log.i(TAG,"tempCal >> "+tempCal);
                food.setCalorie(tempCal);
            } else {
                food.setCalorie("0");
                holderTemp.tvCalculatedCal.setText(0 + " kcal");
            }

        }
    }
}
