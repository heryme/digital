package com.digitalscale.activity;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalscale.R;
import com.digitalscale.constant.Constant;
import com.digitalscale.services.UserService;
import com.digitalscale.tools.Session;
import com.digitalscale.utility.AndroidUtility;
import com.digitalscale.utility.DialogUtility;
import com.digitalscale.utility.FontUtility;
import com.digitalscale.utility.NetworkUtility;
import com.digitalscale.vollyrest.APIService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

@Fullscreen
@EActivity(R.layout.activity_basic_detail)
@WindowFeature(Window.FEATURE_NO_TITLE)
public class BasicDetailActivity extends MasterActivity {

    @ViewById(R.id.btnBasicDetailBack)
    Button btnBasicDetailBack;

    @ViewById(R.id.btnBasicDetailStart)
    Button btnBasicDetailStart;

    @ViewById(R.id.tvWeightUnit)
    public static
    TextView tvWeightUnit;

    @ViewById(R.id.ivBasicDetailSettingWeight)
    ImageView ivBasicDetailSettingWeight;

    @ViewById(R.id.edtWeight)
    EditText edtWeight;

    @ViewById(R.id.tvAge)
    TextView tvage;

    @ViewById(R.id.radioSexGroup)
    RadioGroup radioSexGroup;

    @ViewById(R.id.rbmale)
    RadioButton rbmale;

    @ViewById(R.id.rbfemale)
    RadioButton rbfemale;

    @ViewById(R.id.tvWeight)
    TextView tvWeight;

    @ViewById(R.id.tvGender)
    TextView tvGender;

    @ViewById(R.id.tvBasicDetailBirthDate)
    TextView tvBasicDetailBirthDate;

    @ViewById(R.id.tvAge)
    TextView tvAge;

    @ViewById(R.id.sp_basic_activity_ft)
    Spinner sp_basic_activity_ft;

    @ViewById(R.id.sp_basic_activity_inch)
    Spinner sp_basic_activity_inch;

    @ViewById(R.id.tv_basic_activity_unit)
    TextView tv_basic_activity_unit;

    @ViewById(R.id.tv_basic_detail_height_lbl)
    TextView tv_basic_detail_height_lbl;

    @ViewById(R.id.iv_basic_activity_height_unit)
    ImageView iv_basic_activity_height_unit;

    @ViewById(R.id.et_basic_activity_cm)
    EditText et_basic_activity_cm;

    @ViewById(R.id.lv_basic_activity_ft)
    LinearLayout lv_basic_activity_ft;

    @ViewById(R.id.ll_basic_activity_height_cm)
    LinearLayout ll_basic_activity_height_cm;


    /**
     * Radio Button
     */
    private RadioButton radioSexButton;

    /***
     * Date Picker
     */
    private DatePickerDialog birthDatePickerDialog;

    /**
     * SimpleDateFormat
     */
    private SimpleDateFormat dateFormatter;

    /**
     * Calendar
     */
    private Calendar currentDate, newDate;

    Session session;

    private String questionTwoUnit;
    private String basicDetailsActUnit;

    /**
     * Set Spinner Item
     */
    private ArrayAdapter<String> spinnerFeetAdapter, spinnerInchAdapter;

    @AfterViews
    public void init() {
        //Set up view and set value
        basicInitialization();

        listener();

        setFontStyle();

        et_basic_activity_cm.requestFocus();
        et_basic_activity_cm.setCursorVisible(true);
    }

    @Click
    public void btnBasicDetailBack() {
        //Set Session Value On Back Of The Basic Detail Activity
        setSessionValueOnBack();
        startActivity(new Intent(BasicDetailActivity.this, Question2Activity_.class));
        finish();
    }

    @Click
    public void btnBasicDetailStart() {

        debugLog(TAG, "question1OptionID-->" + Question1Activity.question1OptionID);
        debugLog(TAG, "question2OptionID-->" + Question2Activity.question2OptionID);
        debugLog(TAG, "question2 Weight-->" + Question2Activity.question2weight);
        //debugLog(TAG, "question2 GainWeight-->" + Question2Activity.edtQuestion2GainWeight.getText());
        debugLog(TAG, "Basic Details Weight" + edtWeight.getText());
        debugLog(TAG, "Basic Details Age" + tvage.getText());
        debugLog(TAG, "kg + pound-->" + tvWeightUnit.getText().toString());

        //If select "lb" then pass value pound
        if (questionTwoUnit.equalsIgnoreCase("lb")) {
            questionTwoUnit = "pound";
        }

        if (tvWeightUnit.getText().toString().equalsIgnoreCase("lb")) {
            basicDetailsActUnit = "pound";
        }

        //Check Validation
        validation();
    }

    @Click
    public void ivBasicDetailSettingWeight() {
        DialogUtility.weightUnitSettingPopUp(BasicDetailActivity.this, tvWeightUnit, ivBasicDetailSettingWeight);
    }

    @Click
    public void tvAge() {
        if (tvAge.getText().toString().trim().equals("<select date>"))
            birthDatePickerDialog.updateDate(1985, 0, 1);

        birthDatePickerDialog.show();
    }

    @Click
    public void iv_basic_activity_height_unit() {
        heightUnitSettingPopUp(tv_basic_activity_unit,
                iv_basic_activity_height_unit,
                lv_basic_activity_ft,
                ll_basic_activity_height_cm);
    }

    /**
     * BirthDate Date Picker
     */
    private void birthDatePicker() {

        currentDate = Calendar.getInstance();
        //dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        birthDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                if (!newDate.after(currentDate)) {
                    tvage.setText(dateFormatter.format(newDate.getTime()));
                } else {
                    //Toast.makeText(BasicDetailActivity.this,getString(R.string.toast_error_valid_birth_date_msg), Toast.LENGTH_SHORT).show();
                    toastMessage(getString(R.string.toast_error_valid_birth_date_msg));
                }
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    /**
     * Selection Of The Gender
     */
    private int selectionGender() {
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) findViewById(selectedId);
        session.setBasicDetailGender((radioSexButton.getText().toString()));
        return selectedId;
    }

    /**
     *
     */
    private void validation() {
        if (NetworkUtility.checkIsInternetConnectionAvailable(BasicDetailActivity.this)
                && isValidEditText()
                && validationUtils.isValidEditText(edtWeight)
                && !validationUtils.checkInputIsZero(edtWeight)) {

            if (!tvage.getText().toString().equals("<select date>")) {

                if (selectionGender() == -1) {
                    Toast.makeText(BasicDetailActivity.this, getApplicationContext().getString(R.string.toast_error_gender_msg), Toast.LENGTH_SHORT).show();
                } else {
                    if (!Question2Activity.question2OptionID.equals("opt1")) {
                        //Call For If Select Option2 Or Option3  Of The Question2
                        callUpdateInitialSettingAPI();
                    } else {
                        //Call For If Select Option1 Of The Question2
                        callUpdateInitialSettingWithoutUnit();
                    }
                }

            } else {
                toastMessage(getString(R.string.toast_error_birth_date_msg));
            }
        }
    }

    /**
     * Check Validation Only et_basic_activity_cm Edittext
     *
     * @return
     */
    private boolean isValidEditText() {

        if (tv_basic_activity_unit.getText().toString().equalsIgnoreCase("cm")) {
            if (validationUtils.isValidEditText(et_basic_activity_cm)) {
                if (!validationUtils.checkInputIsZero(et_basic_activity_cm)) {
                    if (validationUtils.checkInputValidHeights(et_basic_activity_cm)) {
                        return true;
                    } else
                        return false;
                }
            } else {
                return false;
            }
        } else {
            return true;
        }
        return true;
    }


    /**
     * Call Update Initial Setting API
     * For Select Option2 Or Option3 Then Pass Units
     */
    private void callUpdateInitialSettingAPI() {

        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", session.getUserId());
        param.put("question_one", Question1Activity.question1OptionID);
        param.put("question_two", Question2Activity.question2OptionID);

        //Split Value Of The Wight Of Question2 Option 2 Or 3
        String[] parts = Question2Activity.question2weight.split("g");
        String weight = parts[0].trim();

        param.put("question_two_weight",weight);
        param.put("question_two_unit", "g");
        if (tv_basic_activity_unit.getText().toString().equalsIgnoreCase("ft")) {
            param.put("height", sp_basic_activity_ft.getSelectedItem().toString() + "." + sp_basic_activity_inch.getSelectedItem().toString());
        } else {
            param.put("height", et_basic_activity_cm.getText().toString());
        }
        param.put("height_unit", tv_basic_activity_unit.getText().toString());
        param.put("weight", edtWeight.getText().toString());
        param.put("weight_unit", basicDetailsActUnit);
        param.put("dob", tvage.getText().toString());
        //param.put("gender", radioSexButton.getText().toString());
        if(Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("Svenska")){
            if(radioSexButton.getText().toString().equalsIgnoreCase("Kvinna")){
                param.put("gender","Female");
            }else if(radioSexButton.getText().toString().equalsIgnoreCase("Man")) {
                param.put("gender","Male");
            }

        }else {
            param.put("gender", radioSexButton.getText().toString());
        }

        param.put("mode", "add");

        UserService.updateInitialSetting(BasicDetailActivity.this, param, new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                String status = response.optString("status");
                String message = response.optString("message");

                if (status.equals("1")) {
                    getSession().setIsUserFillBasicDetails(true);
                    Intent intent = new Intent(BasicDetailActivity.this, BluetoothDevicesActivity_.class);
                    intent.putExtra("from_basic_setting",true);
                    startActivity(intent);
                    finish();
                } else {
                    toastMessage(message);
                }
            }
        });
    }

    /**
     * Call callUpdateInitialSettingWithoutUnit
     * For Only Option1 Of The Question2 Without Pass Units
     */
    private void callUpdateInitialSettingWithoutUnit() {

        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", session.getUserId());
        param.put("question_one", Question1Activity.question1OptionID);
        param.put("question_two", Question2Activity.question2OptionID);
        if (tv_basic_activity_unit.getText().toString().equalsIgnoreCase("ft")) {
            param.put("height", sp_basic_activity_ft.getSelectedItem().toString() + "." + sp_basic_activity_inch.getSelectedItem().toString());
        } else {
            param.put("height", et_basic_activity_cm.getText().toString());
        }
        param.put("height_unit", tv_basic_activity_unit.getText().toString());
        param.put("weight", edtWeight.getText().toString());
        param.put("weight_unit", basicDetailsActUnit);
        param.put("dob", tvage.getText().toString());
        if(Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("Svenska")){
            if(radioSexButton.getText().toString().equalsIgnoreCase("Kvinna")){
                param.put("gender","Female");
            }else if(radioSexButton.getText().toString().equalsIgnoreCase("Man")) {
                param.put("gender","Male");
            }

        }else {
            param.put("gender", radioSexButton.getText().toString());
        }

        Log.d(TAG,"Gender" + radioSexButton.getText().toString());
        Log.d(TAG,"Language" + Locale.getDefault().getDisplayLanguage());
        param.put("mode", "add");

        UserService.updateInitialSetting(BasicDetailActivity.this, param, new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                String status = response.optString("status");
                String message = response.optString("message");

                if (status.equals("1")) {
                    getSession().setIsUserFillBasicDetails(true);
                    // After basic setting completion redirect  to BT Screen for pair device.
                    Intent intent = new Intent(BasicDetailActivity.this, BluetoothDevicesActivity_.class);
                    intent.putExtra("from_basic_setting",true);
                    startActivity(intent);
                } else {
                    toastMessage(message);
                }
            }
        });
    }

    /**
     * Set up view and set value
     */
    private void basicInitialization() {

        session = getSession();

        //Set Spinner
        fillGroupBySpinner();

        //BirthDate Date Picker
        birthDatePicker();

        //Get unit of the question2 options for pass API
        questionTwoUnit = session.getUnit();

        //Set data basic detail height,weight according to previous entered height and weight
        et_basic_activity_cm.setText(session.getBasicDetailHeight());
        edtWeight.setText(session.getBasicDetailWeight());

        //Set data of  basic detail height,weight unit according to previous selection
        tv_basic_activity_unit.setText(session.getBasicDetailHeightUnit());
        tvWeightUnit.setText(session.getBasicDetailWeightUnit());

        if (session.getBasicDetailHeightUnit().equalsIgnoreCase("cm")) {
            ll_basic_activity_height_cm.setVisibility(View.VISIBLE);
            lv_basic_activity_ft.setVisibility(View.GONE);
            tv_basic_activity_unit.setText(tv_basic_activity_unit.getText().toString());
        } else {
            ll_basic_activity_height_cm.setVisibility(View.GONE);
            lv_basic_activity_ft.setVisibility(View.VISIBLE);

            //Set Spinner Height Ft and Inch Value according Session
            sp_basic_activity_ft.setSelection(spinnerFeetAdapter.getPosition(session.getBasicDetailSpinnerHeightFt()));
            sp_basic_activity_inch.setSelection(spinnerInchAdapter.getPosition(session.getBasicDetailSpinnerHeightInch()));
            tv_basic_activity_unit.setText(tv_basic_activity_unit.getText().toString());
        }

        //Set birth date  according to entered
        tvage.setText(session.getBasicDetailBirthDate());

        //Set Gender Value According previous selected
        if(session.getBasicDetailGender() != null && session.getBasicDetailGender().length() > 0){
                if (session.getBasicDetailGender().equals("Male")) {
                    rbmale.setChecked(true);
                } else {
                    rbfemale.setChecked(true);
                }
            }

        //get basic detail unit value
        basicDetailsActUnit = tvWeightUnit.getText().toString();
    }

    /**
     * Set Session Value On Back
     */
    private void setSessionValueOnBack() {

        //Store data in session on back click for basic height,weight,birth date,gender and its unit
        session.setBasicDetailHeight(et_basic_activity_cm.getText().toString());
        session.setBasicDetailWeight(edtWeight.getText().toString());
        session.setBasicDetailHeightUnit(tv_basic_activity_unit.getText().toString());
        session.setBasicDetailWeightUnit(tvWeightUnit.getText().toString());
        session.setBasicDetailBirthDate(tvage.getText().toString());

        //Store Data In Session Spinner Ft And Inch Value
        session.setBasicDetailSpinnerHeightFt(sp_basic_activity_ft.getSelectedItem().toString());
        session.setBasicDetailSpinnerHeightInch(sp_basic_activity_inch.getSelectedItem().toString());

        //Set Gender Value
        selectionGender();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        btnBasicDetailBack.performClick();
    }

    /**
     * Set Font Style Textview And Radio Button
     */
    private void setFontStyle() {
        FontUtility.condLight(tv_basic_detail_height_lbl, BasicDetailActivity.this);
        FontUtility.condLight(tvWeight, BasicDetailActivity.this);
        FontUtility.condLight(tvAge, BasicDetailActivity.this);
        FontUtility.condLight(tvGender, BasicDetailActivity.this);
        FontUtility.condLight(tvBasicDetailBirthDate,BasicDetailActivity.this);
        FontUtility.condBold(tv_basic_activity_unit, BasicDetailActivity.this);
        FontUtility.condBold(tvWeightUnit, BasicDetailActivity.this);
        FontUtility.condLight(tvage, BasicDetailActivity.this);
        FontUtility.condLight(rbmale, BasicDetailActivity.this);
        FontUtility.condLight(rbfemale, BasicDetailActivity.this);
    }

    /**
     * Show Pop up for height centimeter and foot
     *
     * @param textView
     * @param imageView
     */
    private void heightUnitSettingPopUp(
            final TextView textView,
            ImageView imageView,
            final LinearLayout lv_frg_update_profile_ft1,
            final LinearLayout ll_frg_update_profile_height_cm1) {

        PopupMenu popupMenu = new PopupMenu(BasicDetailActivity.this, imageView);

        //Inflating the Popup using xml file
        popupMenu.getMenuInflater()
                .inflate(R.menu.menu_basic_setting1_height, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_basic_setting1_cm:
                        textView.setText("cm");
                        lv_frg_update_profile_ft1.setVisibility(View.GONE);
                        ll_frg_update_profile_height_cm1.setVisibility(View.VISIBLE);
                        et_basic_activity_cm.setText(AndroidUtility.feetToCentimeter(sp_basic_activity_ft.getSelectedItem().toString() + "."+
                        sp_basic_activity_inch.getSelectedItem().toString()));
                        break;
                    case R.id.menu_basic_setting1_ft:
                        textView.setText("ft");
                        ll_frg_update_profile_height_cm1.setVisibility(View.GONE);
                        lv_frg_update_profile_ft1.setVisibility(View.VISIBLE);
                        convertCmToFt();
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    /**
     * Group By Spinner
     * Create the adapter and set
     */
    private void fillGroupBySpinner() {
        //Set Feet Adapter
        if (spinnerFeetAdapter == null) {
            spinnerFeetAdapter = new ArrayAdapter<String>(
                    BasicDetailActivity.this,
                    R.layout.row_spinner,
                    R.id.tv_row_spinner_title,
                    Constant.spinnerItemFtArray);
            sp_basic_activity_ft.setAdapter(spinnerFeetAdapter);
            sp_basic_activity_ft.setSelection(spinnerFeetAdapter.getPosition("5"));

        }

        //Set Inch Adapter
        if (spinnerInchAdapter == null) {
            spinnerInchAdapter = new ArrayAdapter<String>(
                    BasicDetailActivity.this,
                    R.layout.row_spinner,
                    R.id.tv_row_spinner_title,
                    Constant.spinnerItemInArray);
            sp_basic_activity_inch.setAdapter(spinnerInchAdapter);
            sp_basic_activity_inch.setSelection(spinnerInchAdapter.getPosition("0"));
        }
    }

    /**
     * Convert Centimeter Value to Feet And Inch spinner
     */

    private void convertCmToFt() {
        if (et_basic_activity_cm.getText().toString() != null &&
                et_basic_activity_cm.getText().toString().length()> 0 ) {
            //Split Value Of The Height
            String[] parts = AndroidUtility.convertCmTofeetInches(et_basic_activity_cm.getText().toString()).split("\\.");
            String Feet = parts[0];
            String Inch = parts[1];
            sp_basic_activity_ft.setSelection(spinnerFeetAdapter.getPosition(Feet));
            sp_basic_activity_inch.setSelection(spinnerInchAdapter.getPosition(Inch));
        }
    }


    /***
     * On TextChange Fire With Height Validation et_basic_activity_cm EditText
     */
    private void listener() {
        et_basic_activity_cm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                debugLog(TAG,"On Text Change-->" + charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                validationUtils.checkInputValidHeights(et_basic_activity_cm);
            }
        });
    }
}
