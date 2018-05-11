package com.digitalscale.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.digitalscale.R;
import com.digitalscale.activity.MainActivity;
import com.digitalscale.constant.Constant;
import com.digitalscale.google_place_api.AddressService;
import com.digitalscale.google_place_api.GooglePlaceApi;
import com.digitalscale.google_place_api.GooglePlaceParser;
import com.digitalscale.google_place_api.PlacesAutoCompleteAdapter;
import com.digitalscale.parser.GetProfileParser;
import com.digitalscale.services.ChangeProfileService;
import com.digitalscale.services.GetProfileService;
import com.digitalscale.services.SendImageService;
import com.digitalscale.tools.Session;
import com.digitalscale.utility.AndroidUtility;
import com.digitalscale.utility.DialogUtility;
import com.digitalscale.utility.FontUtility;
import com.digitalscale.utility.ImageUtility;
import com.digitalscale.utility.MarshMallowPermissionUtility;
import com.digitalscale.utility.NetworkUtility;
import com.digitalscale.utility.ValidationUtils;
import com.digitalscale.vollyrest.APIService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Vishal Gadhiya on 3/15/2017.
 */

@EFragment(R.layout.fragment_update_profile)
public class UpdateProfileFragment extends MasterFragment {

    private final String TAG = UpdateProfileFragment.class.getName();

    @ViewById(R.id.tv_frg_first_name)
    TextView tv_frg_first_name;

    @ViewById(R.id.tv_frg_height)
    TextView tv_frg_height;

    @ViewById(R.id.tv_frg_gender)
    TextView tv_frg_gender;

    @ViewById(R.id.tv_frg_dob)
    TextView tv_frg_dob;

    @ViewById(R.id.tv_frg_location)
    TextView tv_frg_location;

    @ViewById(R.id.tv_frg_zip_code)
    TextView tv_frg_zip_code;

    @ViewById(R.id.tv_frg_last_name)
    TextView tv_frg_last_name;

    @ViewById(R.id.tv_frg_weight)
    TextView tv_frg_weight;

    @ViewById(R.id.et_frg_first_name)
    EditText et_frg_first_name;

    @ViewById(R.id.rd_frg_group)
    RadioGroup rd_frg_group;

    @ViewById(R.id.rb_frg_male)
    RadioButton rb_frg_male;

    @ViewById(R.id.rb_frg_female)
    RadioButton rb_frg_female;

    @ViewById(R.id.tv_frg_birth_date)
    TextView tv_frg_birth_date;

    @ViewById(R.id.et_frg_location)
    EditText et_frg_location;

    @ViewById(R.id.et_frg_zip_code)
    EditText et_frg_zip_code;

    @ViewById(R.id.et_frg_weight)
    EditText et_frg_weight;

    @ViewById(R.id.et_frg_last_name)
    EditText et_frg_last_name;

    @ViewById(R.id.btn_frg_update_profile)
    Button btn_frg_update_profile;

    @ViewById(R.id.spn_frg_profile_inch)
    Spinner spn_frg_profile_inch;

    @ViewById(R.id.sp_frg_profile_ft)
    Spinner sp_frg_profile_ft;

    @ViewById(R.id.iv_frg_change_pic)
    ImageView iv_frg_change_pic;

    @ViewById(R.id.iv_frg_profile)
    ImageView iv_frg_profile;

    @ViewById(R.id.iv_frg_profile_height_unit)
    ImageView iv_frg_profile_height_unit;

    @ViewById(R.id.tv_frg_profile_unit)
    TextView tv_frg_profile_unit;

    @ViewById(R.id.iv_frg_profile_weight)
    ImageView iv_frg_profile_weight;

    @ViewById(R.id.tv_frg_profile_weight_unit)
    TextView tv_frg_profile_weight_unit;

    @ViewById(R.id.tv_frg_profile_ft)
    TextView tv_frg_profile_ft;

    @ViewById(R.id.tv_frg_profile_in)
    TextView tv_frg_profile_in;

    @ViewById(R.id.ll_frg_update_profile_height_cm)
    LinearLayout ll_frg_update_profile_height_cm;

    @ViewById(R.id.lv_frg_update_profile_ft)
    LinearLayout lv_frg_update_profile_ft;

    @ViewById(R.id.autoCompTvUpdatePro)
    AutoCompleteTextView autoCompTvUpdatePro;

    @ViewById(R.id.et_frg_update_profile_cm)
    EditText et_frg_update_profile_cm;

    @ViewById(R.id.tv_update_profile_email)
    TextView tv_update_profile_email;

    @ViewById(R.id.tv_update_profile_email_label)
    TextView tv_update_profile_email_label;


    private DatePickerDialog birthDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Calendar currentDate, newDate;
    ValidationUtils validationUtils;

    /**
     * Set Spinner Item
     */
    private ArrayAdapter<String> spinnerFeetAdapter, spinnerInchAdapter;

    private boolean isNewImageSelect = false;

    private File file;


    //Choose Photos
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;

    //Google Place APi Parser
    private GooglePlaceParser googlePlaceParser;

    Session session;

    @AfterViews
    public void init() {
        basicInitialization();
        callGooglePlaceAPI(getContext(), autoCompTvUpdatePro);
        tv_update_profile_email.setText(session.getEmail());
    }


    @Click
    public void autoCompTvUpdatePro() {
       /* Utils.openChangeCityStateCountryDialog(getContext(),
                autoCompTvUpdatePro);*/
    }

    @Click
    public void btn_frg_update_profile() {
        if (NetworkUtility.checkIsInternetConnectionAvailable(getContext())
                && isValidInputForUpdateProfile()) {
            if (isNewImageSelect) {
                changeProfilePictureAPI();
            }
            changeProfileData();
            AndroidUtility.hideSoftKeyboard(getActivity());
        }
    }

    @Click
    public void tv_frg_birth_date() {
        if (tv_frg_birth_date.getText().toString().trim().equals("<select date>"))
            birthDatePickerDialog.updateDate(1985, 0, 1);

        birthDatePickerDialog.show();
    }

    @Click
    public void iv_frg_change_pic() {
        if (!MarshMallowPermissionUtility.checkPermissionForCamera(getActivity())) {
            MarshMallowPermissionUtility.requestPermissionForCamera(getActivity());
        } else if (!MarshMallowPermissionUtility.checkPermissionForExternalStorage(getActivity())) {
            MarshMallowPermissionUtility.requestPermissionForExternalStorage(getActivity());
        } else if (!MarshMallowPermissionUtility.checkPermissionForReadExternalStorage(getActivity())) {
            MarshMallowPermissionUtility.requestPermissionForReadExternalStorage(getActivity());
        }else {
            selectImage();
        }
    }

    @Click
    public void iv_frg_profile_height_unit() {
        heightUnitSettingPopUp(getContext(), tv_frg_profile_unit,
                iv_frg_profile_height_unit,
                lv_frg_update_profile_ft,
                ll_frg_update_profile_height_cm);
    }

    @Click
    public void iv_frg_profile_weight() {
        DialogUtility.weightUnitSettingPopUp(getActivity(), tv_frg_profile_weight_unit, iv_frg_profile_weight);
    }

    /**
     * Call Change Profile Data API
     */
    private void changeProfileData() {

        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", getSession().getUserId());
        param.put("first_name", et_frg_first_name.getText().toString());
        param.put("last_name", et_frg_last_name.getText().toString());
        param.put("dob", tv_frg_birth_date.getText().toString());
        if (tv_frg_profile_unit.getText().toString().equalsIgnoreCase("ft")) {
            param.put("height", sp_frg_profile_ft.getSelectedItem().toString() + "." + spn_frg_profile_inch.getSelectedItem().toString());
        } else {
            param.put("height", et_frg_update_profile_cm.getText().toString());
        }
        param.put("height_unit", tv_frg_profile_unit.getText().toString());
        param.put("weight", et_frg_weight.getText().toString());

        //Pass Value According  To Weight Unit Selection
        if (tv_frg_profile_weight_unit.getText().toString().equalsIgnoreCase("lb")) {
            param.put("weight_unit", "pound");
        } else {
            param.put("weight_unit", "kg");
        }

        if (rb_frg_male.isChecked()) {
            param.put("gender", "Male");
            getSession().setBasicDetailGender("Male");
        } else {
            param.put("gender", "Female");
            getSession().setBasicDetailGender("Female");
        }

        param.put("location", autoCompTvUpdatePro.getText().toString());
        param.put("zipcode", et_frg_zip_code.getText().toString());
        if (googlePlaceParser != null) {
            param.put("city", googlePlaceParser.getCity());
            param.put("state", googlePlaceParser.getState());
            param.put("country", googlePlaceParser.getCountry());
        }
        ChangeProfileService.changeProfile(getActivity(), param, new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                debugLog(TAG, "Update Profile Data-->" + response.toString());

                String status = response.optString("status");
                String message = response.optString("message");

                if(status.equals("1")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(message);
                    builder.setCancelable(false);
                    String positiveText = "OK";
                    builder.setPositiveButton(positiveText,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // positive button logic
                                    if (dialog != null)
                                        dialog.cancel();

                                    ((MainActivity)getContext()).loadFragment(HomeFragment.newInstance(), "fm_home",true,getString(R.string.nav_menu_home));
                                }
                            });

                    AlertDialog dialog = builder.create();
                    // display dialog
                    dialog.show();
                }else {
                    DialogUtility.dialogWithPositiveButton(message,getContext());
                }
            }
        });
    }


    /**
     * BirthDate Date Picker
     */
    private void birthDatePicker() {

        currentDate = Calendar.getInstance();
        //dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        final Calendar newCalendar = Calendar.getInstance();
        birthDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                Log.d(TAG,"Year--->" + year + "monthOfYear-->" + monthOfYear + "dayOfMonth-->" + dayOfMonth);

                if (!newDate.after(currentDate)) {
                    tv_frg_birth_date.setText(dateFormatter.format(newDate.getTime()));
                } else {
                    toastMessage(getString(R.string.toast_error_valid_birth_date_msg));
                }
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        birthDatePickerDialog.updateDate(1985, 1, 1);

    }

    /**
     * Change Textview Font Style
     */
    private void changeFontStyle() {
        //TextView
        FontUtility.condLight(tv_frg_first_name, getContext());
        FontUtility.condLight(tv_frg_height, getContext());
        FontUtility.condLight(tv_frg_gender, getContext());
        FontUtility.condLight(tv_frg_dob, getContext());
        FontUtility.condLight(tv_frg_location, getContext());
        FontUtility.condLight(tv_frg_zip_code, getContext());
        FontUtility.condLight(tv_frg_weight, getContext());
        FontUtility.condLight(tv_frg_last_name, getContext());
        FontUtility.condLight(tv_update_profile_email, getContext());
        FontUtility.condLight(tv_update_profile_email_label, getContext());
        FontUtility.condBold(tv_frg_profile_unit,getContext());
        FontUtility.condBold(tv_frg_profile_weight_unit,getContext());

        //EditText
        FontUtility.condLight(et_frg_first_name, getContext());
        //FontUtility.condLight(et_frg_height,getContext());
        FontUtility.condLight(tv_frg_birth_date, getContext());
        FontUtility.condLight(et_frg_location, getContext());
        //FontUtility.condLight(et_frg_gender,getContext());
        FontUtility.condLight(et_frg_zip_code, getContext());
        FontUtility.condLight(et_frg_weight, getContext());
        FontUtility.condLight(et_frg_last_name, getContext());
        FontUtility.condLight(et_frg_update_profile_cm, getContext());

        FontUtility.condLight(btn_frg_update_profile, getContext());

        //Radio Button
        FontUtility.condLight(rb_frg_male, getContext());
        FontUtility.condLight(rb_frg_female, getContext());

        //AutoCompleteTextView
        FontUtility.condLight(autoCompTvUpdatePro,getContext());
    }

    /**
     * Check Update Profile validation
     *
     * @return
     */
    public boolean isValidInputForUpdateProfile() {
        if (validationUtils.isValidEditText(et_frg_first_name)
                && validationUtils.isValidEditText(et_frg_last_name)
                && validationUtils.isValidEditText(et_frg_weight)
                && !validationUtils.checkInputIsZero(et_frg_weight)
                //&& validationUtils.isValidEditText(tv_frg_birth_date)
                && validationUtils.isValidAutoCompleteTextView(autoCompTvUpdatePro)
                && validationUtils.isValidEditText(et_frg_zip_code)
                ) {

            if (tv_frg_profile_unit.getText().toString().equalsIgnoreCase("cm")) {
                if (validationUtils.isValidEditText(et_frg_update_profile_cm)) {
                    if (!validationUtils.checkInputIsZero(et_frg_update_profile_cm)) {
                        if (validationUtils.checkInputValidHeights(et_frg_update_profile_cm)) {
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
            return false;
        }
        return false;
    }


    /**
     * Set Up Basic Componets
     */
    private void basicInitialization() {
        onTextChange();
        birthDatePicker();
        changeFontStyle();
        validationUtils = new ValidationUtils(getContext());
        session = new Session(getActivity());
        getProfileAPI();
        fillGroupBySpinner();
    }

    /**
     * Call Get Profile API
     */
    private void getProfileAPI() {
        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", getSession().getUserId());
        GetProfileService.getProfile(getActivity(), param, new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    //Get Parser response
                    GetProfileParser getProfileParser = GetProfileParser.parseGetProfileData(response);
                    et_frg_first_name.setText(getProfileParser.getFirstName());
                    et_frg_last_name.setText(getProfileParser.getLastName());
                    //Zip code and Location  Getting "Null" First Time User Login Then Set Empty Value
                    if(!getProfileParser.getZipcode().equalsIgnoreCase("null")) {
                        et_frg_zip_code.setText(getProfileParser.getZipcode());
                    }else{
                        et_frg_zip_code.setText("");
                    }

                    if(!getProfileParser.getLocation().equalsIgnoreCase("null")){
                        autoCompTvUpdatePro.setText(getProfileParser.getLocation());
                    }else {
                        autoCompTvUpdatePro.setText("");
                    }

                    if( getProfileParser.getDob() != null &&  getProfileParser.getDob().length() > 0){
                        //Split Value Of The Height
                        String[] dateArray = getProfileParser.getDob().split(" ");
                        String date = dateArray[0];
                        tv_frg_birth_date.setText(date);

                        //Date Picker Dialog Open With Entered Birth Date Not Current Date
                        updateDialogPickerDate(date);
                    }

                    et_frg_weight.setText(getProfileParser.getWeight());

                    //Set Weight Unit Value
                    if (getProfileParser.getWeight_unit().equalsIgnoreCase("pound")) {
                        tv_frg_profile_weight_unit.setText("lb");
                    } else {
                        tv_frg_profile_weight_unit.setText("kg");
                    }

                    //Set Height Unit Value
                    if (getProfileParser.getHeight_unit().equalsIgnoreCase("ft")) {
                        lv_frg_update_profile_ft.setVisibility(View.VISIBLE);
                        ll_frg_update_profile_height_cm.setVisibility(View.GONE);
                        tv_frg_profile_unit.setText(getProfileParser.getHeight_unit());

                        //Split Value Of The Height
                        String[] parts = getProfileParser.getHeight().split("\\.");
                        String Feet = parts[0];
                        String Inch = parts[1];

                        //Set Spinner Value
                        sp_frg_profile_ft.setSelection(spinnerFeetAdapter.getPosition(Feet));
                        spn_frg_profile_inch.setSelection(spinnerInchAdapter.getPosition(Inch));
                    } else {
                        lv_frg_update_profile_ft.setVisibility(View.GONE);
                        ll_frg_update_profile_height_cm.setVisibility(View.VISIBLE);
                        tv_frg_profile_unit.setText(getProfileParser.getHeight_unit());
                        et_frg_update_profile_cm.setText(getProfileParser.getHeight());
                    }

                    //Set Gender
                    if (getProfileParser.getGender().equalsIgnoreCase("Male")) {
                        rb_frg_male.setChecked(true);
                    } else {
                        rb_frg_female.setChecked(true);
                    }

                    //Set Profile Picture
                    if (getProfileParser.getProfilePic() != null
                            && getProfileParser.getProfilePic().length() > 0) {
                        Glide.with(getActivity()).load(getProfileParser.getProfilePic())
                                .into(iv_frg_profile);
                    }else{
                        if(getProfileParser.getGender().equalsIgnoreCase("Male")){
                            Glide.with(getActivity()).load(R.drawable.ic_male)
                                    .into(iv_frg_profile);
                        }else{
                            Glide.with(getActivity()).load(R.drawable.ic_female)
                                    .into(iv_frg_profile);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Dialog For Image Selection
     */
    private void selectImage() {
        final CharSequence[] items = {getString(R.string.lbl_take_photo), getString(R.string.lbl_choose_from_gallery),
                getString(R.string.lbl_cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getString(R.string.lbl_take_photo))) {
                    userChoosenTask = "Take Photo";
                        cameraIntent();

                } else if (items[item].equals(getString(R.string.lbl_choose_from_gallery))) {
                    userChoosenTask = "Choose from Gallery";
                        galleryIntent();
                } else if (items[item].equals(getString(R.string.lbl_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /**
     * Start Camera
     */
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    /**
     * Select Image From The Gallery
     */
    private void galleryIntent() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_FILE);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                isNewImageSelect = true;
                if(ImageUtility.getPath(getActivity(),selectedImageUri) != null
                        && !ImageUtility.getPath(getActivity(), selectedImageUri).endsWith(".mp4")) {

                        file = new File(ImageUtility.getPath(getActivity(), selectedImageUri));
                        System.out.println("Image Path---->" + ImageUtility.getPath(getActivity(), selectedImageUri));
                }else{
                    DialogUtility.dialogWithPositiveButton(getString(R.string.dialog_some_thing_wrong),getContext());
                }

                if (selectedImageUri == null && data.getExtras() != null) {
                    selectedImageUri = ImageUtility.getImageUri(getActivity(), (Bitmap) data.getExtras().get("data"));
                }

                if(ImageUtility.getPath(getActivity(),selectedImageUri) != null) {
                        Glide.with(getActivity()).load(selectedImageUri)
                            .error(R.mipmap.ic_launcher)
                            .into(iv_frg_profile);
                }

            } else if (requestCode == REQUEST_CAMERA) {

                Uri photosUri = data.getData();
                if (photosUri == null && data.getExtras() != null) {
                    photosUri = ImageUtility.getImageUri(getActivity(), (Bitmap) data.getExtras().get("data"));/*AndroidUtility.getLastCapturedImg(getContext());*/
                }

                Glide.with(getActivity()).load(photosUri)
                        .error(R.mipmap.ic_launcher)
                        .into(iv_frg_profile);

                isNewImageSelect = true;
                file = new File(ImageUtility.getRealPathFromURI(getContext(), photosUri));
                System.out.println("Camera Photo Path---->" + file);
            }
        }
    }


    /**
     * Change Profile Picture API
     */
    private void changeProfilePictureAPI() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("user_id", getSession().getUserId());
        param.put("profile_pic", file);

        SendImageService.sendImage(getContext(), param, new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                debugLog(TAG, "Change Profile Picture Response-->" + response.toString());
                String status = null,message;

                try {
                    status = response.optString("status");
                    message = response.optString("message");
                    JSONObject data = response.getJSONObject("data");
                    getSession().setProfilePic(data.getString("newProfilePic"));
                    Log.d(TAG,"newProfilePic-->" + data.getString("newProfilePic"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (status.equals("1")) {
                    //DialogUtility.dialogWithPositiveButton(message, getContext());
                }
            }
        });
    }

    /**
     * Show Pop up for height centimeter and foot
     *
     * @param context
     * @param textView
     * @param imageView
     */
    private void heightUnitSettingPopUp(Context context,
                                        final TextView textView,
                                        ImageView imageView,
                                        final LinearLayout lv_frg_update_profile_ft1,
                                        final LinearLayout ll_frg_update_profile_height_cm1) {
        PopupMenu popupMenu = new PopupMenu(context, imageView);

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
                        et_frg_update_profile_cm.setText(AndroidUtility.feetToCentimeter(sp_frg_profile_ft.getSelectedItem().toString() + "." +
                                spn_frg_profile_inch.getSelectedItem().toString()));
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
     * City,State and Country Change dialog
     */
    private void callGooglePlaceAPI(final Context context,
                                    final AutoCompleteTextView act_dialogChangeCityStateCountry) {

        act_dialogChangeCityStateCountry.setAdapter(new PlacesAutoCompleteAdapter(context,
                R.layout.row_google_autocomplete_text));
        act_dialogChangeCityStateCountry.setCursorVisible(false);

		/* AutoComplete on click */
        act_dialogChangeCityStateCountry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                act_dialogChangeCityStateCountry.setFocusableInTouchMode(true);
                act_dialogChangeCityStateCountry.requestFocus();
                act_dialogChangeCityStateCountry.setCursorVisible(true);
            }
        });

		/* AutoComplete on item click */
        act_dialogChangeCityStateCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*
                 * Get data associated with the specified position in the list
				 * (AdapterView)
				 */

                act_dialogChangeCityStateCountry.setFocusableInTouchMode(false);
                act_dialogChangeCityStateCountry.setCursorVisible(false);
                HashMap<String, Object> mapPlaceIdAndDesc = GooglePlaceApi.getPlaceIdAndDesc(position);

                act_dialogChangeCityStateCountry.setText(mapPlaceIdAndDesc.get("DESCRIPTION").toString());

				/* set user selected city,country,state */
                String userSelectedCityStateCountry = mapPlaceIdAndDesc.get("DESCRIPTION").toString();
                String place_id = mapPlaceIdAndDesc.get("PLACE_ID").toString();
                act_dialogChangeCityStateCountry.setText(userSelectedCityStateCountry);

                //Call API For Get The Address
                AddressService.getAddress(context, getPlaceInfo(place_id), new APIService.Success<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        Log.d("TAG", "Place Response-->" + response.toString());
                        googlePlaceParser = GooglePlaceParser.parseResponse(response);
                    }
                });
            }
        });
    }

    /**
     * Pass Place Id  And Return Full API
     *
     * @param input
     * @return
     */
    public static String getPlaceInfo(String input) {
        String place = "https://maps.googleapis.com/maps/api/place/details/json?" +
                "placeid=" + input + "&key=" + Constant.API_KEY;
        Log.d("FINAL URL:::   ", "----------->" + place);
        //return urlString.toString();
        return place;
    }

    /**
     * Group By Spinner
     * Create the adapter and set
     */
    private void fillGroupBySpinner() {
        //Set Feet Adapter
        if (spinnerFeetAdapter == null) {
            spinnerFeetAdapter = new ArrayAdapter<String>(
                    getContext(),
                    R.layout.row_spinner,
                    R.id.tv_row_spinner_title,
                    Constant.spinnerItemFtArray);
            sp_frg_profile_ft.setAdapter(spinnerFeetAdapter);
        }

        //Set Inch Adapter
        if (spinnerInchAdapter == null) {
            spinnerInchAdapter = new ArrayAdapter<String>(
                    getContext(),
                    R.layout.row_spinner,
                    R.id.tv_row_spinner_title,
                    Constant.spinnerItemInArray);
            spn_frg_profile_inch.setAdapter(spinnerInchAdapter);
        }

    }

    /**
     * Convert Centimeter Value to Feet And Inch spinner
     */

    private void convertCmToFt() {
        if (et_frg_update_profile_cm.getText().toString() != null &&
                et_frg_update_profile_cm.getText().toString().length() > 0 ) {
            //Split Value Of The Height
            String[] parts = AndroidUtility.convertCmTofeetInches(et_frg_update_profile_cm.getText().toString()).split("\\.");
            Log.d(TAG,"parts >> "+parts);
            String feet = parts[0];
            String inch = parts[1];
            Log.d(TAG,"Feet >> "+feet);
            Log.d(TAG,"Inch >> "+inch);


            sp_frg_profile_ft.setSelection(spinnerFeetAdapter.getPosition(feet));
            spn_frg_profile_inch.setSelection(spinnerInchAdapter.getPosition(inch));
        }
    }

    /***
     * On TextChange Fire With Height Validation et_basic_activity_cm EditText
     */
    private void onTextChange() {
        et_frg_update_profile_cm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                debugLog(TAG,"On Text Change-->" + charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                validationUtils.checkInputValidHeights(et_frg_update_profile_cm);
            }
        });
    }

    /**
     * Show DatePicker Dialog With Previous Entered Birth Date
     * @param date
     */
    private void updateDialogPickerDate(String date){
        //Date Picker Dialog Open With Entered Birth Date Not Current Date
        String[] datePickerArray = date.split("-");
        String datePickerYear = datePickerArray[0];
        String datePickerMonth = datePickerArray[1];
        String datePickerDay = datePickerArray[2];
        birthDatePickerDialog.updateDate(Integer.parseInt(datePickerYear),
                Integer.parseInt(datePickerMonth) - 1,
                Integer.parseInt(datePickerDay));
    }
}
