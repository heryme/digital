package com.digitalscale.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.digitalscale.BT.BTClientThread;
import com.digitalscale.BT.BTConstant;
import com.digitalscale.BT.BTDeviceConnectThread;
import com.digitalscale.BT.BTServerConnectThread;
import com.digitalscale.R;
import com.digitalscale.adapter.SelectedFoodAdapter;
import com.digitalscale.fragments.BreakfastFoodHistoryFragment;
import com.digitalscale.fragments.DiaryFragment;
import com.digitalscale.fragments.DinnerFoodHistoryFragment;
import com.digitalscale.fragments.LunchFoodHistoryFragment;
import com.digitalscale.fragments.SnacksFoodHistoryFragment;
import com.digitalscale.model.Food;
import com.digitalscale.model.FoodHistory;
import com.digitalscale.parser.FoodParser;
import com.digitalscale.services.FoodService;
import com.digitalscale.tools.Constant;
import com.digitalscale.utility.AndroidUtility;
import com.digitalscale.utility.BluetoothUtility;
import com.digitalscale.utility.DateUtility;
import com.digitalscale.utility.FontUtility;
import com.digitalscale.utility.FormulaUtility;
import com.digitalscale.utility.NetworkUtility;
import com.digitalscale.vollyrest.APIService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static com.digitalscale.activity.SplashActivity.selectedDate;
import static com.digitalscale.fragments.BreakfastFoodHistoryFragment.breakfastList;


/**
 * Created by Vishal Gadhiya on 3/28/2017.
 */

@Fullscreen
@WindowFeature(Window.FEATURE_NO_TITLE)
@EActivity(R.layout.activity_add_food_new)
public class AddFoodActivity extends MasterActivity {

    private static final String TAG = AddFoodActivity.class.getSimpleName();

    private static final String SEARCH_FOOD_REQUEST_TAG = "search_food";

    @ViewById(R.id.tv_add_food_food_type)
    TextView tv_add_food_food_type;

    @ViewById(R.id.btnAddTo)
    Button btnAddTo;

    @ViewById(R.id.iv_add_food_back)
    ImageView iv_add_food_back;

    @ViewById(R.id.edtAddFoodSearchFood)
    AutoCompleteTextView edtAddFoodSearchFood;

    @ViewById(R.id.rv_add_food_selected_food)
    RecyclerView rv_add_food_selected_food;

    @ViewById(R.id.iv_add_food_search)
    ImageView iv_add_food_search;

    @ViewById(R.id.iv_add_food_search_clear)
    ImageView iv_add_food_search_clear;

    @ViewById(R.id.pb_add_food_search)
    ProgressBar pb_add_food_search;

    @ViewById(R.id.pb_add_food_process)
    ProgressBar pb_add_food_process;

    @ViewById(R.id.tvAddFoodWeightReading)
    public static TextView tvAddFoodWeightReading;

    @ViewById(R.id.tv_add_food_unit)
    public static TextView tv_add_food_unit;

    @ViewById(R.id.tv_add_food_bt_status)
    public static TextView tv_add_food_bt_status;

    @ViewById(R.id.iv_add_food_bt_image)
    public static ImageView iv_add_food_bt_image;

    @ViewById(R.id.llAddFoodBTStatus)
    LinearLayout llAddFoodBTStatus;

    @ViewById(R.id.btnAddEditFoodReset)
    public static Button btnAddEditFoodReset;

    ArrayList<Food> foodList;
    public static ArrayList<Food> selectedFoodList;

    FoodItemAdapter adapter;
    SelectedFoodAdapter selectedFoodAdapter;

    String foodtype;

    boolean isFoodModeEdit = false;

    BTClientThread connectThread;
    BTDeviceConnectThread deviceConnectThread;
    BTServerConnectThread serverConnectThread;
    public BluetoothDevice deviceToConnect;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket curBTSocket = null;

    @AfterViews
    public void init() {

        // If device not support Bluetooth  then activity is close.
        if(!BluetoothUtility.checkBluetoothCompatibility())
            finish();

        BluetoothDevicesActivity.isAddEditFoodActivityCreated = true;
        //Set Font Style
        setFontStyle();

        foodtype = getIntent().getStringExtra("food_type");
        isFoodModeEdit = getIntent().getBooleanExtra("food_mode_edit", false);
        setFoodTitle(foodtype);

        tvAddFoodWeightReading.setText("" + BluetoothDevicesActivity.weightReadingInGram);

        listener();

        foodList = new ArrayList<>();
        adapter = new FoodItemAdapter(AddFoodActivity.this, R.layout.drop_down_item);
        edtAddFoodSearchFood.setAdapter(adapter);

        selectedFoodList = new ArrayList<>();
        LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_add_food_selected_food.setLayoutManager(llm);

        if (isFoodModeEdit) {
            ArrayList<Food> foodHistoryList = getFoodFooListFromFoodHistory(foodtype);
            if (foodHistoryList.size() > 0) {
                AddFoodActivity.selectedFoodList = foodHistoryList;
                selectedFoodAdapter = new SelectedFoodAdapter(AddFoodActivity.this, selectedFoodList, isFoodModeEdit, rv_add_food_selected_food);
                //testSelectedAdapter = new TestSelectedAdapter(AddFoodActivity.this, selectedFoodList, isFoodModeEdit);
                rv_add_food_selected_food.setAdapter(selectedFoodAdapter);
            } else {
                getFoodHistory(foodtype);
            }
        } else {
            selectedFoodAdapter = new SelectedFoodAdapter(AddFoodActivity.this, selectedFoodList, isFoodModeEdit, rv_add_food_selected_food);
            rv_add_food_selected_food.setAdapter(selectedFoodAdapter);
            //testSelectedAdapter = new TestSelectedAdapter(AddFoodActivity.this, selectedFoodList, isFoodModeEdit);
            //rv_add_food_selected_food.setAdapter(testSelectedAdapter);
        }

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        deviceToConnect = getPairedWeightPlateDevice();
        debugLog("BluetoothDevicesActivity.isSocketConnected >>" + BluetoothDevicesActivity.isSocketConnected);

        BluetoothDevicesActivity.weightReadingInGram = 0;
        tvAddFoodWeightReading.setText(String.valueOf(BluetoothDevicesActivity.weightReadingInGram));

        // Old condition when device is available
        /*if (deviceToConnect != null && !BluetoothDevicesActivity.isSocketConnected)
            autoConnectWeightPlate();
        else {
            tv_add_food_bt_status.setText("Connected");
            tv_add_food_bt_status.setTextColor(ContextCompat.getColor(AddFoodActivity.this, R.color.white));
            iv_add_food_bt_image.setImageResource(R.drawable.ic_bluetooth);
        }*/

        // new condition
        if(deviceToConnect == null)
        {
            tv_add_food_bt_status.setText(getString(R.string.lbl_disconnected));
            tv_add_food_bt_status.setTextColor(ContextCompat.getColor(AddFoodActivity.this, R.color.colorFontGry));
            iv_add_food_bt_image.setImageResource(R.drawable.ic_bluetooth_gry);
        }

        if(deviceToConnect != null && !BluetoothDevicesActivity.isSocketConnected)
            autoConnectWeightPlate();

        /*-----------------*/

        if (BluetoothDevicesActivity.isSocketConnected)
            btnAddEditFoodReset.setVisibility(View.VISIBLE);
        else
            btnAddEditFoodReset.setVisibility(View.INVISIBLE);


        AndroidUtility.hideSoftKeyboard(AddFoodActivity.this);
        /*InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtAddFoodSearchFood.getWindowToken(), 0);*/

    }

    /* Auto connect socket connection when "Weight Plate" is paired */
    private void autoConnectWeightPlate() {
        startAsServer();

        //toastMessage("Connecting to device " + deviceToConnect.getName());

        //Now this is not the server...
        killServerThread();

        //Connect to the other device which is a server...
        connectAsClient();
    }


    /**
     * Set food type
     * @param foodtype
     */
    private void setFoodTitle(String foodtype) {
        String type = "";
        if (foodtype.equalsIgnoreCase(Constant.BREAKFAST))
            type = getString(R.string.lbl_breakfast);/*"Breakfast";*/
        if (foodtype.equalsIgnoreCase(Constant.LUNCH))
            type = getString(R.string.lbl_lunch);/*"Lunch";*/
        if (foodtype.equalsIgnoreCase(Constant.DINNER))
            type = getString(R.string.lbl_dinner);/*"Dinner";*/
        if (foodtype.equalsIgnoreCase(Constant.SNACKS))
            type = getString(R.string.lbl_Snacks);/*"Snacks";*/

        tv_add_food_food_type.setText(type);
        btnAddTo.setText(getString(R.string.lbl_add_to) + " " + type);
    }

    private void getFoodHistory(String foodType) {
        final HashMap<String, String> param = new HashMap();
        param.put("user_id", getSession().getUserId());
        param.put("type", foodType);
        param.put("food_date", DateUtility.getCurrentDateIn_yyyy_mm_dd());

        FoodService.getFoodHistory(AddFoodActivity.this, param, pb_add_food_process, new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                debugLog("param >>" + param);
                debugLog("response >> " + response);
                //breakfastList.clear();
                ArrayList<FoodHistory> tempList = FoodParser.FoodHistoryParser.parseFoodHistoryResponse(AddFoodActivity.this, response);
                getFoodFooListFromFoodHistory(tempList);
                if (tempList != null && tempList.size() > 0) {
                    debugLog("tempList.size() >> " + tempList.size());
                    AddFoodActivity.selectedFoodList = getFoodFooListFromFoodHistory(tempList);
                    selectedFoodAdapter = new SelectedFoodAdapter(AddFoodActivity.this, AddFoodActivity.selectedFoodList, isFoodModeEdit, rv_add_food_selected_food);
                    //testSelectedAdapter = new TestSelectedAdapter(AddFoodActivity.this, AddFoodActivity.selectedFoodList, isFoodModeEdit);
                    rv_add_food_selected_food.setAdapter(selectedFoodAdapter);
                }
            }
        });
    }

    private void listener() {

        edtAddFoodSearchFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Food foodItem = foodList.get(position);
                if (!foodIsSelected(foodItem)) {
                    foodItem.setQuantity(AddFoodActivity.tvAddFoodWeightReading.getText().toString());
                    foodItem.setWeightReading(tvAddFoodWeightReading.getText().toString());
                    foodItem.setWeightReadingUnit(Constant.UNIT_GM);
                    String kcal = FormulaUtility.calculateCalories(tvAddFoodWeightReading.getText().toString(),foodItem.getFoodKcal(),foodItem.getFoodQty());
                    foodItem.setCalorie(kcal);
                    selectedFoodList.add(0,foodItem);
                    //Collections.reverse(selectedFoodList);

                    if (selectedFoodAdapter != null)
                    selectedFoodAdapter.notifyDataSetChanged();
                    btnAddEditFoodReset.performClick();
                    AndroidUtility.hideSoftKeyboard(AddFoodActivity.this);
                    /*if (testSelectedAdapter != null)
                        testSelectedAdapter.notifyDataSetChanged();*/

                } else {
                    Toast.makeText(getBaseContext(), getString(R.string.toast_msg_food_already_selected), Toast.LENGTH_SHORT).show();
                }
                edtAddFoodSearchFood.setText("");
                AddFoodActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_add_food_search.setVisibility(View.VISIBLE);
                        pb_add_food_search.setVisibility(View.GONE);
                        iv_add_food_search_clear.setVisibility(View.GONE);
                    }
                });
            }
        });

        edtAddFoodSearchFood.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() <= 0) {
                    iv_add_food_search_clear.setVisibility(View.GONE);
                    iv_add_food_search.setVisibility(View.VISIBLE);
                    pb_add_food_search.setVisibility(View.GONE);
                }
            }
        });


    }

    /* Check food is already selected or not */
    private boolean foodIsSelected(Food food) {
        if (selectedFoodList != null && selectedFoodList.size() > 0) {
            for (int i = 0; i < selectedFoodList.size(); i++) {
                if (selectedFoodList.get(i).getId().equalsIgnoreCase(food.getId())) {
                    return true;
                }
            }
        }

        return false;
    }

    private ArrayList<Food> searchFood(String string) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("search_text", string);
        AddFoodActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                iv_add_food_search.setVisibility(View.GONE);
                iv_add_food_search_clear.setVisibility(View.GONE);
                pb_add_food_search.setVisibility(View.VISIBLE);
            }
        });


        FoodService.searchFood(AddFoodActivity.this, param, SEARCH_FOOD_REQUEST_TAG, new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {

                AddFoodActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_add_food_search.setVisibility(View.GONE);
                        iv_add_food_search_clear.setVisibility(View.VISIBLE);
                        pb_add_food_search.setVisibility(View.GONE);
                    }
                });
                if (response.optString("status").equalsIgnoreCase("1")) {
                    try {
                        foodList.clear();
                        foodList = FoodParser.getFoodList(response.getJSONArray("data"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        AddFoodActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iv_add_food_search.setVisibility(View.GONE);
                                iv_add_food_search_clear.setVisibility(View.VISIBLE);
                                pb_add_food_search.setVisibility(View.GONE);
                            }
                        });
                    }
                } else {
                    foodList.clear();
                }
                adapter.notifyDataSetChanged();
            }
        }, new APIService.Error<VolleyError>() {
            @Override
            public void onError(VolleyError error) {
                AddFoodActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        debugLog("Search food error");
                        iv_add_food_search.setVisibility(View.GONE);
                        iv_add_food_search_clear.setVisibility(View.VISIBLE);
                        pb_add_food_search.setVisibility(View.GONE);
                    }
                });
            }
        });
        return foodList;
    }

    /* Clear search text */
    @Click
    public void iv_add_food_search_clear() {
        debugLog("Click clear button");
        edtAddFoodSearchFood.setText("");
        iv_add_food_search_clear.setVisibility(View.GONE);
        iv_add_food_search.setVisibility(View.VISIBLE);
    }

    @Click
    public void llAddFoodBTStatus() {
        startActivity(new Intent(AddFoodActivity.this, BluetoothDevicesActivity_.class));
    }

    //@Click
    public void tv_add_food_unit() {
        PopupMenu popupMenu = new PopupMenu(AddFoodActivity.this, tv_add_food_unit);

        //Inflating the Popup using xml file
        popupMenu.getMenuInflater()
                .inflate(R.menu.menu_weight_reading_unit, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String selectedUnit = AddFoodActivity.tv_add_food_unit.getText().toString();
                switch (item.getItemId()) {
                    case R.id.menu_weight_reading_gm:
                        tv_add_food_unit.setText(Constant.UNIT_GM);
                        AddFoodActivity.tvAddFoodWeightReading.setText(FormulaUtility.convertOldToNewUnit(selectedUnit, Constant.UNIT_GM, tvAddFoodWeightReading.getText().toString()));
                        break;
                    case R.id.menu_weight_reading_lb:
                        tv_add_food_unit.setText(Constant.UNIT_LB);
                        AddFoodActivity.tvAddFoodWeightReading.setText(FormulaUtility.convertOldToNewUnit(selectedUnit, Constant.UNIT_LB, tvAddFoodWeightReading.getText().toString()));
                        break;
                    case R.id.menu_weight_reading_oz:
                        tv_add_food_unit.setText(Constant.UNIT_OZ);
                        AddFoodActivity.tvAddFoodWeightReading.setText(FormulaUtility.convertOldToNewUnit(selectedUnit, Constant.UNIT_OZ, tvAddFoodWeightReading.getText().toString()));
                        break;
                    case R.id.menu_weight_reading_ml:
                        tv_add_food_unit.setText(Constant.UNIT_ML);
                        AddFoodActivity.tvAddFoodWeightReading.setText(FormulaUtility.convertOldToNewUnit(selectedUnit, Constant.UNIT_ML, tvAddFoodWeightReading.getText().toString()));
                        break;
                }
                return true;
            }
        });
        popupMenu.show();

    }

    @Click
    public void btnAddTo() {
        if (isValidAddOrEditFoodList()) {

            if (selectedFoodAdapter.checkEditTxtValue() && NetworkUtility.checkIsInternetConnectionAvailable(AddFoodActivity.this)) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("user_id", getSession().getUserId());
                map.put("type", foodtype);
                if (selectedDate == null)
                    map.put("date", DateUtility.getCurrentDateIn_yyyy_mm_dd()/*DateUtility.getCurrentDateIn_yyyy_mm_dd()*/);
                else
                    map.put("date", selectedDate/*DateUtility.getCurrentDateIn_yyyy_mm_dd()*/);

                /* handle food add and update mode */
                if (isFoodModeEdit)
                    map.put("mode", "edit");
                else
                    map.put("mode", "add");

                if (isFoodModeEdit) {
                    debugLog("selectedFoodAdapter.getAddedFoodList().size() >> " + selectedFoodAdapter.getAddedFoodList().size());
                    map.put("data", createJsonFormat(selectedFoodAdapter.getAddedFoodList()));
                } else {
                    debugLog("selectedFoodList >> " + selectedFoodList.size());
                    map.put("data", createJsonFormat(selectedFoodList));
                }

                FoodService.addFood(AddFoodActivity.this, map, new APIService.Success<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        debugLog("Add Food response >> " + response.toString());
                        try {
                            String status = response.optString("status");
                            String msg = response.optString("message");

                            if (status.equalsIgnoreCase("1")) {
                                toastMessage(msg);
                                Intent intent = new Intent(AddFoodActivity.this, MainActivity_.class);
                                intent.putExtra("OPEN_DIARY_FM", true);
                                setTabPosition(foodtype);
                                startActivity(intent);
                                finish();
                            } else {
                                toastMessage(msg);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        } else {
            toastMessage("Please, Add at least one food");
        }
    }

    /* Device reset : Send reset command to device */
    @Click
    public void btnAddEditFoodReset() {
        BTDeviceConnectThread tempThread = new BluetoothDevicesActivity().getDeviceConnectThread();
        debugLog("tempThread >> " + tempThread);
        if (tempThread == null) {
            if (BluetoothDevicesActivity.forResetDeviceSocket != null)
                sendCommand(0xFA, BluetoothDevicesActivity.forResetDeviceSocket); // FA
            else {
                //toastMessage(getString(R.string.toast_msg_reconnect_device));
            }
        } else
            tempThread.write(0xFA); // FA
    }

    /* check add or edit food list is empty or not,
    * empty list allow for "EDIT" &
    * at least one food is required for "ADD"
    * */
    private boolean isValidAddOrEditFoodList() {
        if (selectedFoodList != null) {
            if (isFoodModeEdit)
                return true;
            else {
                if (selectedFoodList.size() <= 0) {
                    toastMessage(getString(R.string.toast_msg_add_at_least_one_food));
                    return false;
                } else return true;
            }
        }
        return false;
    }

    /* Create json format string for each selected food item */
    private String createJsonFormat(ArrayList<Food> selectedFoodList) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray();
            for (int i = 0; i < selectedFoodList.size(); i++) {
                Food foodItem = selectedFoodList.get(i);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("food_id", foodItem.getId());
                jsonObject.put("qty", foodItem.getQuantity());

                // When user select "lb" than pass "pound" as parameter
                if (foodItem.getWeightReadingUnit().equalsIgnoreCase("lb"))
                    jsonObject.put("qty_unit", "pound");
                else
                    jsonObject.put("qty_unit", foodItem.getWeightReadingUnit());

                jsonArray.put(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        debugLog("jsonArray.toString() >> " + jsonArray.toString());
        return jsonArray.toString();
    }

    @Click
    public void iv_add_food_back() {
        Intent intent = new Intent(AddFoodActivity.this, MainActivity_.class);
        intent.putExtra("OPEN_DIARY_FM", true);
        startActivity(intent);
        finish();
    }

    /***
     * Food Item Adapter
     */
    class FoodItemAdapter extends ArrayAdapter<String> implements Filterable {
        private static final int MAX_RESULTS = 10;
        private Context mContext;

        public FoodItemAdapter(Context context, int resource) {
            super(context, resource);
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return foodList.size();
        }

        @Override
        public String getItem(int i) {
            return foodList.get(i).getFoodName();
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.drop_down_item, viewGroup, false);
            }
            TextView tvFoodName  = (TextView) convertView.findViewById(R.id.tvFoodName);
            FontUtility.condBold(tvFoodName,AddFoodActivity.this);
            tvFoodName.setText(getItem(position).toString());
            return convertView;
        }

        @Override
        public Filter getFilter() {

            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    FilterResults filterResults = new FilterResults();

                    if (charSequence != null && NetworkUtility.checkIsInternetConnectionAvailable(AddFoodActivity.this)) {
                        foodList = searchFood(charSequence.toString());/*mPlaceAPI.autocomplete(constraint.toString());*/
                        if (foodList != null && foodList.size() > 0) {
                            debugLog("foodList.size() >> " + foodList.size());
                            filterResults.values = foodList;
                            filterResults.count = foodList.size();
                        }
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                }
            };
            return filter;
        }
    }

    private ArrayList<Food> getFoodFooListFromFoodHistory(String foodType) {
        BreakfastFoodHistoryFragment breakfastFoodHistoryFragment = new BreakfastFoodHistoryFragment();
        ArrayList<FoodHistory> list = null;
        ArrayList<Food> tempFoodList = null;

        if (foodType.equalsIgnoreCase(Constant.BREAKFAST))
            list = breakfastList;
        else if (foodType.equalsIgnoreCase(Constant.LUNCH))
            list = LunchFoodHistoryFragment.lunchList;
        else if (foodType.equalsIgnoreCase(Constant.DINNER))
            list = DinnerFoodHistoryFragment.dinnerList;
        else if (foodType.equalsIgnoreCase(Constant.SNACKS))
            list = SnacksFoodHistoryFragment.snacksList;

        if (list != null) {
            debugLog("edit food list.size() >> " + list.size());
            tempFoodList = new ArrayList<>();
            if (list.size() > 0) {
                Food food;
                for (int i = 0; i < list.size(); i++) {
                    food = new Food();
                    FoodHistory item = list.get(i);
                    food.setId(item.getFoodId());
                    food.setFoodName(item.getFoodName());
                    food.setDescription(item.getDescription());
                    food.setQuantity(item.getQty());
                    food.setCalorie(item.getKcal());
                    food.setFoodUnit(item.getQtyUnit());
                    food.setFoodKcal(item.getFoodKcal());
                    food.setFoodUnit(item.getFoodUnit());
                    food.setFoodQty(item.getFoodQty());

                    tempFoodList.add(food);
                }
            }
        }
        debugLog("foodList.size() >> " + tempFoodList.size());
        return tempFoodList;
    }

    private ArrayList<Food> getFoodFooListFromFoodHistory(ArrayList<FoodHistory> list) {
        BreakfastFoodHistoryFragment breakfastFoodHistoryFragment = new BreakfastFoodHistoryFragment();
        //ArrayList<FoodHistory> list = null;
        ArrayList<Food> tempFoodList = null;

        if (list != null) {
            debugLog("list.size() >> " + list.size());
            tempFoodList = new ArrayList<>();
            if (list.size() > 0) {
                Food food;
                for (int i = 0; i < list.size(); i++) {
                    food = new Food();
                    FoodHistory item = list.get(i);
                    food.setId(item.getFoodId());
                    food.setFoodName(item.getFoodName());
                    food.setDescription(item.getDescription());
                    food.setQuantity(item.getQty());
                    tempFoodList.add(food);
                }
            }
        }
        debugLog("foodList.size() >> " + tempFoodList.size());
        return tempFoodList;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(AddFoodActivity.this, MainActivity_.class);
        intent.putExtra("OPEN_DIARY_FM", true);
        startActivity(intent);
        finish();
    }

    /* Select tab position after Add / Edit food ,
    * Directly redirect on particular tab
    * */
    private void setTabPosition(String foodType) {
        if (foodType.equals(Constant.BREAKFAST))
            DiaryFragment.selectedTabPosition = 0;
        else if (foodType.equals(Constant.LUNCH))
            DiaryFragment.selectedTabPosition = 1;
        else if (foodType.equals(Constant.SNACKS))
            DiaryFragment.selectedTabPosition = 2;
        else if (foodType.equals(Constant.DINNER))
            DiaryFragment.selectedTabPosition = 3;
    }

    private void setFontStyle(){
        FontUtility.condLight(tv_add_food_food_type,AddFoodActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BluetoothDevicesActivity.isAddEditFoodActivityCreated = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        BluetoothDevicesActivity.isAddEditFoodActivityCreated = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        BluetoothDevicesActivity.isAddEditFoodActivityCreated = true;
    }

    private BluetoothDevice getPairedWeightPlateDevice() {
        BluetoothDevice bluetoothDevice = null;

        Set<BluetoothDevice> pairedDevices
                = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                String deviceBTName = device.getName();
                if (deviceBTName.equalsIgnoreCase("Weight Plate")) {
                    return device;
                }
            }
        }
        return bluetoothDevice;
    }

    public void startAsServer() {
        debugLog("Listening for online Bluetooth devices...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                serverConnectThread = new BTServerConnectThread();
                curBTSocket = serverConnectThread.acceptConnection(AddFoodActivity.this, mBluetoothAdapter, BTConstant.MY_UUID, mHandler);
                Log.d(TAG, "curBTSocket >> " + curBTSocket);
            }
        }).start();
    }

    public void killServerThread() {
        if (serverConnectThread != null) {
            serverConnectThread.closeConnection();
            serverConnectThread = null;
        }
    }

    public void connectAsClient() {
        debugLog("Connecting for online Bluetooth devices...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (deviceToConnect != null) {
                    if (connectThread != null) {
                        connectThread.cancel();
                        connectThread = null;
                        Log.d("TAG", "deviceToConnect.getName() >> " + deviceToConnect.getName());
                        if (deviceToConnect.getUuids() != null)
                            Log.d("TAG", "deviceToConnect.getUuids()[0].getUuid() >> " + deviceToConnect.getUuids()[0].getUuid());
                    }
                    connectThread = new BTClientThread();
                    curBTSocket = connectThread.connect(AddFoodActivity.this, mBluetoothAdapter, deviceToConnect, BTConstant.MY_UUID, mHandler);
                    connectThread.start();
                }
            }
        }).start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "Handler message >> " + msg);
            byte[] buf = (byte[]) msg.obj;

            switch (msg.what) {
                case BTConstant.MESSAGE_READ:
                    final String deviceName = new String(buf);
                    //msg.arg2 is positive(1) & negative(0) value, if value is (-) then multiply (-1).
                    if (msg.arg2 == 0)
                        BluetoothDevicesActivity.weightReadingInGram = msg.arg1 * (-1);
                    else
                        BluetoothDevicesActivity.weightReadingInGram = msg.arg1;

                    debugLog("BluetoothDevicesActivity.weightReadingInGram >> " + BluetoothDevicesActivity.weightReadingInGram);
                    String selectedUnit = AddFoodActivity.tv_add_food_unit.getText().toString();
                    AddFoodActivity.tvAddFoodWeightReading.setText(FormulaUtility.convertOldToNewUnit(Constant.UNIT_GM, selectedUnit, String.valueOf(BluetoothDevicesActivity.weightReadingInGram)));
                    break;
                case BTConstant.MESSAGE_DEVICE_NAME:
                    // Connected device's name
                    String mConnectedDeviceName = new String(buf);
                    toastMessage("Connected to " + mConnectedDeviceName);
                    // Send "0x0" hex command to device for device always on
                    sendCommand(0x0);
                    BluetoothDevicesActivity.isSocketConnected = true;
                    tv_add_food_bt_status.setText(getString(R.string.lbl_Connected));
                    tv_add_food_bt_status.setTextColor(ContextCompat.getColor(AddFoodActivity.this, R.color.white));
                    iv_add_food_bt_image.setBackgroundResource(R.drawable.ic_bluetooth);
                    resetButtonVisibleInvisible();
                case BTConstant.MESSAGE_SOCKET_DISCONNECTED:
                    // socket connection lost
                    tv_add_food_bt_status.setText(getString(R.string.lbl_disconnected));
                    BluetoothDevicesActivity.isSocketConnected = false;
                    tv_add_food_bt_status.setTextColor(ContextCompat.getColor(AddFoodActivity.this, R.color.colorFontGry));
                    iv_add_food_bt_image.setBackgroundResource(R.drawable.ic_bluetooth_gry);
                    resetButtonVisibleInvisible();
            }
        }
    };

    public static void resetButtonVisibleInvisible() {
        if (BluetoothDevicesActivity.isSocketConnected)
            btnAddEditFoodReset.setVisibility(View.VISIBLE);
        else
            btnAddEditFoodReset.setVisibility(View.INVISIBLE);
    }

    /* Send command to device in HEX format string */
    public void sendCommand(int command) {
        deviceConnectThread = new BTDeviceConnectThread(AddFoodActivity.this, curBTSocket, mHandler);
        deviceConnectThread.start();
        Log.d(TAG, "command >> " + command);
        //if (command.length() > 0) {
        //byte[] send = /*message*/"0xFE".getBytes();
        //Log.d(TAG,"Send >> "+ 0x30);
        deviceConnectThread.write(command);
        //}

    }

    public void sendCommand(int command, BluetoothSocket socket) {
        deviceConnectThread = new BTDeviceConnectThread(AddFoodActivity.this, socket, mHandler);
        deviceConnectThread.start();
        Log.d(TAG, "command >> " + command);
        //if (command.length() > 0) {
        //byte[] send = /*message*/"0xFE".getBytes();
        //Log.d(TAG,"Send >> "+ 0x30);
        deviceConnectThread.write(command);
        //}

    }
}
