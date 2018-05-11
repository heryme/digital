package com.digitalscale.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.digitalscale.R;
import com.digitalscale.activity.MainActivity_;
import com.digitalscale.parser.GetUpdateInitialSettingParser;
import com.digitalscale.services.GoalTabService;
import com.digitalscale.utility.DialogUtility;
import com.digitalscale.utility.FontUtility;
import com.digitalscale.utility.NetworkUtility;
import com.digitalscale.vollyrest.APIService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Vishal Gadhiya on 3/15/2017.
 */

@EFragment(R.layout.fragment_tab_goal)
public class TabGoalFragment extends MasterFragment {

    public static final String TAG = TabGoalFragment.class.getSimpleName();

    @ViewById(R.id.ll_frg_tab_goal_not_internet_conn)
    LinearLayout ll_frg_tab_goal_not_internet_conn;

    @ViewById(R.id.ll_frg_tab_goal_question_view)
    LinearLayout ll_frg_tab_goal_question_view;

    @ViewById(R.id.btn_frg_tab_goal_try_again)
    Button btn_frg_tab_goal_try_again;

    @ViewById(R.id.llQuestion1Answer)
    LinearLayout llQuestion1Answer;

    @ViewById(R.id.btnQuestion1Next)
    Button btnQuestion1Next;

    //Question 2
    @ViewById(R.id.ll_act_question2_que1)
    LinearLayout ll_act_question2_que1;

    @ViewById(R.id.ll_act_question2_que2)
    LinearLayout ll_act_question2_que2;

    @ViewById(R.id.ll_act_question2_que3)
    LinearLayout ll_act_question2_que3;

    @ViewById(R.id.ll_act_question2_que2_inner_view)
    LinearLayout ll_act_question2_que2_inner_view;

    @ViewById(R.id.ll_act_question2_que3_inner_view)
    LinearLayout ll_act_question2_que3_inner_view;

    @ViewById(R.id.iv_act_question2_que1)
    ImageView iv_act_question2_que1;

    @ViewById(R.id.iv_act_question2_que2)
    ImageView iv_act_question2_que2;

    @ViewById(R.id.iv_act_question2_que3)
    ImageView iv_act_question2_que3;

    @ViewById(R.id.tv_act_question2_que1)
    TextView tv_act_question2_que1;

    @ViewById(R.id.tv_act_question2_que2)
    TextView tv_act_question2_que2;

    @ViewById(R.id.tv_act_question2_que3)
    TextView tv_act_question2_que3;

    @ViewById(R.id.btn_frg_tab_goal_submit)
    Button btn_frg_tab_goal_submit;

    @ViewById(R.id.rg_activity_question2_lose_weight)
    RadioGroup rg_activity_question2_lose_weight;

    @ViewById(R.id.rg_activity_question2_gain_weight)
    RadioGroup rg_activity_question2_gain_weight;

    @ViewById(R.id.rb_activity_question2_lose_weight_250g)
    RadioButton rb_activity_question2_lose_weight_250g;

    @ViewById(R.id.rb_activity_question2_lose_weight_500g)
    RadioButton rb_activity_question2_lose_weight_500g;

    @ViewById(R.id.rb_activity_question2_lose_weight_750g)
    RadioButton rb_activity_question2_lose_weight_750g;

    @ViewById(R.id.rb_activity_question2_lose_weight_1000g)
    RadioButton rb_activity_question2_lose_weight_1000g;

    @ViewById(R.id.rb_activity_question2_gain_weight_250g)
    RadioButton rb_activity_question2_gain_weight_250g;

    @ViewById(R.id.rb_activity_question2_gain_weight_500g)
    RadioButton rb_activity_question2_gain_weight_500g;

    @ViewById(R.id.rb_activity_question2_gain_weight_750g)
    RadioButton rb_activity_question2_gain_weight_750g;

    @ViewById(R.id.rb_activity_question2_gain_weight_1000g)
    RadioButton rb_activity_question2_gain_weight_1000g;

    @ViewById(R.id.ll_activity_que2_child_lose_weight)
    LinearLayout ll_activity_que2_child_lose_weight;

    @ViewById(R.id.ll_activity_que2_child_gain_weight)
    LinearLayout ll_activity_que2_child_gain_weight;

    /**
     * Radio Button
     */
    private RadioButton radioButtonLoseWeight,radioButtonGainWeight;

    GetUpdateInitialSettingParser getUpdateInitialSettingParser;

    public String question1OptionID,
            question2OptionID,
            prevSelectedOptionQue1,
            prevSelectedOptionQue2;
    public static String tabGoalQuestion2Weight;

    @AfterViews
    public void init() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        ll_activity_que2_child_lose_weight.setLayoutParams(params);
        ll_activity_que2_child_gain_weight.setLayoutParams(params);

        if(NetworkUtility.checkIsInternetConnectionAvailable(getContext())) {
            callGetInitialSettingsAPI();
        }else {
            ll_frg_tab_goal_question_view.setVisibility(View.GONE);
            ll_frg_tab_goal_not_internet_conn.setVisibility(View.VISIBLE);
        }

        setFontStyle();
    }

    @Click
    public void btn_frg_tab_goal_try_again(){
        if(NetworkUtility.checkIsInternetConnectionAvailable(getContext())){
            ll_frg_tab_goal_question_view.setVisibility(View.VISIBLE);
            ll_frg_tab_goal_not_internet_conn.setVisibility(View.GONE);
            callGetInitialSettingsAPI();
        }else {
            ll_frg_tab_goal_not_internet_conn.setVisibility(View.VISIBLE);
        }
    }

    @Click
    public void btn_frg_tab_goal_submit(){
        //debugLog(TAG,"Weight--->" + edtQuestion2LoseWeight.getText().toString() + edtQuestion2GainWeight.getText().toString());
        debugLog(TAG,"Question2 Unit-->" + tabGoalQuestion2Weight);
        debugLog("question1OptionID >> " + question1OptionID);
        debugLog("question2OptionID >> " + question2OptionID);
        if (question1OptionID.equals("")) {
            toastMessage(getString(R.string.toast_select_option));
        }else {
            checkValidation();
        }
    }

    @Click
    public void ll_act_question2_que1() {

        tabGoalQuestion2Weight = "";
        question2OptionID = "opt1";

        //Set Option2 And Option3 Layout Height Match Parent
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        ll_activity_que2_child_lose_weight.setLayoutParams(params);
        ll_activity_que2_child_gain_weight.setLayoutParams(params);

        ll_act_question2_que2_inner_view.setVisibility(View.GONE);
        ll_act_question2_que3_inner_view.setVisibility(View.GONE);

        iv_act_question2_que1.setImageResource(R.drawable.ic_checked);
        tv_act_question2_que1.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorOrange));

        iv_act_question2_que2.setImageResource(R.drawable.ic_unchecked);
        tv_act_question2_que2.setTextColor(ContextCompat.getColor(getContext(), R.color.colorFontGry));
        iv_act_question2_que3.setImageResource(R.drawable.ic_unchecked);
        tv_act_question2_que3.setTextColor(ContextCompat.getColor(getContext(), R.color.colorFontGry));
    }

    @Click
    public void ll_act_question2_que2() {

        question2OptionID = "opt2";

        //Set Option2 Height Wrap content
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        //Set Option3 Height Match Parent
        LinearLayout.LayoutParams gain = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        ll_activity_que2_child_lose_weight.setLayoutParams(params);
        ll_activity_que2_child_gain_weight.setLayoutParams(gain);


        //edtQuestion2GainWeight.setText("");

        ll_act_question2_que2_inner_view.setVisibility(View.VISIBLE);
        ll_act_question2_que3_inner_view.setVisibility(View.GONE);

        iv_act_question2_que2.setImageResource(R.drawable.ic_checked);
        tv_act_question2_que2.setTextColor(ContextCompat.getColor(getContext(), R.color.colorOrange));

        iv_act_question2_que1.setImageResource(R.drawable.ic_unchecked);
        tv_act_question2_que1.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorFontGry));
        iv_act_question2_que3.setImageResource(R.drawable.ic_unchecked);
        tv_act_question2_que3.setTextColor(ContextCompat.getColor(getContext(), R.color.colorFontGry));

    }

    @Click
    public void ll_act_question2_que3() {

        question2OptionID = "opt3";

        //Set Option2 Layout Height Match Parent
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        ll_activity_que2_child_lose_weight.setLayoutParams(params);

        //Set Option3 Layout Height Wrap Content
        LinearLayout.LayoutParams gain = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        ll_activity_que2_child_gain_weight.setLayoutParams(gain);

        ll_act_question2_que2_inner_view.setVisibility(View.GONE);
        ll_act_question2_que3_inner_view.setVisibility(View.VISIBLE);

        iv_act_question2_que3.setImageResource(R.drawable.ic_checked);
        tv_act_question2_que3.setTextColor(ContextCompat.getColor(getContext(), R.color.colorOrange));

        iv_act_question2_que1.setImageResource(R.drawable.ic_unchecked);
        tv_act_question2_que1.setTextColor(ContextCompat.getColor(getContext(), R.color.colorFontGry));

        iv_act_question2_que2.setImageResource(R.drawable.ic_unchecked);
        tv_act_question2_que2.setTextColor(ContextCompat.getColor(getContext(), R.color.colorFontGry));
    }

    /**
     * Set Up Answer
     */
    private void setupAnswer() {
        String arrAnswer[] = {getString(R.string.lbl_llitel_or_no_execise),
                getString(R.string.lbl_exercise_1_3_daysperweek),
                getString(R.string.lbl_exercise_3_5_daysperweek),
                getString(R.string.lbl_exercise_6_7_daysperweek),
                getString(R.string.lbl_exercise_daily)};

        //Set Up Question 2 Answer
        setQuestionOptionsStateQuestion2();
        for (int i = 0; i < arrAnswer.length; i++) {
            answerDynamicView(arrAnswer[i], i);
        }
    }

    /**
     * Create Dynamic View Of The Answer
     * @param ansText
     * @param id
     */
    private void answerDynamicView(String ansText, final int id) {
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llParams.setMargins(0, 0, 0, 2);
        final LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setLayoutParams(llParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        linearLayout.setId(id);

        // Check mark
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final ImageView imgCheckMark = new ImageView(getContext());
        imgCheckMark.setLayoutParams(imgParams);
        imgCheckMark.setImageResource(R.drawable.ic_unchecked);
        linearLayout.addView(imgCheckMark);

        // Answer text
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvParams.setMargins(20, 0, 0, 0);
        final TextView tvAnswer = new TextView(getContext());

        //Set Font Style
        FontUtility.condLight(tvAnswer,getContext());
        tvAnswer.setLayoutParams(tvParams);

        linearLayout.setGravity(Gravity.LEFT | Gravity.CENTER);
        tvAnswer.setTextColor(ContextCompat.getColor(getContext(), R.color.colorFontGry));
        tvAnswer.setText(ansText);
        tvAnswer.setTextSize(20f);
        FontUtility.condLight(tvAnswer,getContext());
        linearLayout.addView(tvAnswer);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetViews(linearLayout);
                imgCheckMark.setImageResource(R.drawable.ic_checked);
                tvAnswer.setTextColor(ContextCompat.getColor(getContext(), R.color.colorOrange));
                question1OptionID = "opt" + String.valueOf(id + 1);
                Log.d(TAG, "opt id-->" + question1OptionID);
            }
        });

        llQuestion1Answer.addView(linearLayout);

        //Set Question2 Answer According To API
        setQuestionOptionsStateQuestion1(id,imgCheckMark,tvAnswer);
    }

    /**
     * Reset View Base On Selection Of The Option
     * @param linearLayout
     */
    private void resetViews(LinearLayout linearLayout) {
        for (int i = 0; i < llQuestion1Answer.getChildCount(); i++) {
            LinearLayout ll = (LinearLayout) llQuestion1Answer.getChildAt(i);
            ImageView iv = (ImageView) ll.getChildAt(0);
            iv.setImageResource(R.drawable.ic_unchecked);
            TextView tv = (TextView) ll.getChildAt(1);
            tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorFontGry));
        }
    }

    /**
     * Check Validation Question2
     */
    private void checkValidation() {
          Log.d(TAG,"selectionLoseWeightRadioButton-->" + selectionLoseWeightRadioButton());
        if (question2OptionID.equals("opt1")) {
            if(NetworkUtility.checkIsInternetConnectionAvailable(getContext())){
                //callUpdateSettingAPI();
                 alertDialog();
            }
        } else {
            if (question2OptionID.equals("opt2")) {
                //Log.d(TAG,"selectionLoseWeightRadioButton-->" + selectionLoseWeightRadioButton());
                if(selectionLoseWeightRadioButton() == -1) {
                    toastMessage(getString(R.string.toast_lose_weight));
                }else {
                    if(NetworkUtility.checkIsInternetConnectionAvailable(getContext())){
                         alertDialog();
                    }
                }
            } else if(question2OptionID.equals("opt3")) {
                if(selectionGainWeightRadioButton() == -1) {
                    toastMessage(getString(R.string.toast_gain_option));
                }else{
                    alertDialog();
                }
            }else {
                toastMessage(getString(R.string.toast_select_option_tab_goal_fragment));
            }
        }
    }

    /**
     * Set Font Style Of The Textview
     */
    private void setFontStyle() {
        FontUtility.condLight(tv_act_question2_que1,getContext());
        FontUtility.condLight(tv_act_question2_que2,getContext());
        FontUtility.condLight(tv_act_question2_que3,getContext());

        FontUtility.condLight(rb_activity_question2_lose_weight_250g,getContext());
        FontUtility.condLight(rb_activity_question2_lose_weight_500g,getContext());
        FontUtility.condLight(rb_activity_question2_lose_weight_750g,getContext());
        FontUtility.condLight(rb_activity_question2_lose_weight_1000g,getContext());

        FontUtility.condLight(rb_activity_question2_gain_weight_250g,getContext());
        FontUtility.condLight(rb_activity_question2_gain_weight_500g,getContext());
        FontUtility.condLight(rb_activity_question2_gain_weight_750g,getContext());
        FontUtility.condLight(rb_activity_question2_gain_weight_1000g,getContext());
    }

    /**
     * Set Question2 Option According To API
     */
    private void setQuestionOptionsStateQuestion1(int id, ImageView imgCheckMark, TextView tvAnswer) {
        if (prevSelectedOptionQue1 != null && prevSelectedOptionQue1.length() > 0) {
            debugLog(TAG,"Id--> " + id);
            debugLog(TAG,"prevSelectedOptionQue1 id --> " + prevSelectedOptionQue1);
            if (prevSelectedOptionQue1.equals("opt1")) {
                if(id == 0){
                    question1OptionID = prevSelectedOptionQue1;
                    imgCheckMark.setImageResource(R.drawable.ic_checked);
                    tvAnswer.setTextColor(ContextCompat.getColor(getContext(), R.color.colorOrange));
                }
            }else if(prevSelectedOptionQue1.equals("opt2")){
                if(id == 1){
                    question1OptionID = prevSelectedOptionQue1;
                    imgCheckMark.setImageResource(R.drawable.ic_checked);
                    tvAnswer.setTextColor(ContextCompat.getColor(getContext(), R.color.colorOrange));
                }
            }else if(prevSelectedOptionQue1.equals("opt3")){
                if(id == 2){
                    question1OptionID = prevSelectedOptionQue1;
                    imgCheckMark.setImageResource(R.drawable.ic_checked);
                    tvAnswer.setTextColor(ContextCompat.getColor(getContext(), R.color.colorOrange));
                }
            }else if(prevSelectedOptionQue1.equals("opt4")){
                if(id == 3){
                    question1OptionID = prevSelectedOptionQue1;
                    imgCheckMark.setImageResource(R.drawable.ic_checked);
                    tvAnswer.setTextColor(ContextCompat.getColor(getContext(), R.color.colorOrange));
                }
            }else if(prevSelectedOptionQue1.equals("opt5")){
                if(id == 4){
                    question1OptionID = prevSelectedOptionQue1;
                    imgCheckMark.setImageResource(R.drawable.ic_checked);
                    tvAnswer.setTextColor(ContextCompat.getColor(getContext(), R.color.colorOrange));
                }
            }
        }
    }

    /**
     * Set Question2 Option According To API
     */
    private void setQuestionOptionsStateQuestion2() {

        if(!prevSelectedOptionQue2.equals("")){
            if(prevSelectedOptionQue2.equals("opt1")){
                //Select first option
                ll_act_question2_que1.performClick();
            }else if(prevSelectedOptionQue2.equals("opt2")) {
                //select second option
                ll_act_question2_que2.performClick();

                String weightLose = getUpdateInitialSettingParser.getQuestionTwoWeight();
                if (weightLose != null && weightLose.length() > 0) {
                    if (weightLose.equals("250")) {
                        rb_activity_question2_lose_weight_250g.setChecked(true);
                    } else if (weightLose.equals("500")) {
                        rb_activity_question2_lose_weight_500g.setChecked(true);
                    } else if (weightLose.equals("750")) {
                        rb_activity_question2_lose_weight_750g.setChecked(true);
                    } else if (weightLose.equals("1000")) {
                        rb_activity_question2_lose_weight_1000g.setChecked(true);
                    }
                }
            }else {
                //Select three option
                ll_act_question2_que3.performClick();
                String weightGain = getUpdateInitialSettingParser.getQuestionTwoWeight();
                if (weightGain != null && weightGain.length() > 0) {
                    if (weightGain.equals("250")) {
                        rb_activity_question2_gain_weight_250g.setChecked(true);
                    } else if (weightGain.equals("500")) {
                        rb_activity_question2_gain_weight_500g.setChecked(true);
                    } else if (weightGain.equals("750")) {
                        rb_activity_question2_gain_weight_750g.setChecked(true);
                    } else if (weightGain.equals("1000")) {
                        rb_activity_question2_gain_weight_1000g.setChecked(true);
                    }
                }
            }
        }
    }

    /**
     * Call Basic Initial Setting
     */
    private void callUpdateSettingAPI(){
        HashMap<String,String> param = new HashMap<>();
        param.put("user_id",getSession().getUserId());
        param.put("question_one",question1OptionID);
        param.put("question_two",question2OptionID);

        if(!question2OptionID.equals("opt1")){
            //Split Value Of The Wight Of Question2 Option 2 Or 3
            String[] parts = tabGoalQuestion2Weight.split("g");
            String weight = parts[0].trim();
            param.put("question_two_weight",weight);
            param.put("question_two_unit","g");
        }else {
            param.put("question_two_weight","");
            param.put("question_two_unit","");
        }
        param.put("mode","edit");

        GoalTabService.updateInitialSetting(getContext(), param, new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                debugLog(TAG,response.toString());
                String status = response.optString("status");
                String message = response.optString("message");

                if(status.equals("1")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(message);
                    builder.setCancelable(false);
                    String positiveText = "OK";/*getString(android.R.string.yes);*/
                    builder.setPositiveButton(positiveText,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // positive button logic
                                    if (dialog != null)
                                        dialog.cancel();

                                    Intent intent = new Intent(getContext(), MainActivity_.class);
                                    getActivity().startActivity(intent);
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
     * Call GetInitialSettingsAPI
     */
    private void callGetInitialSettingsAPI(){
        HashMap<String,String> params = new HashMap<>();
        params.put("user_id",getSession().getUserId());
        GoalTabService.getInitialSettings(getContext(), params, new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                debugLog(TAG,response.toString());
                getUpdateInitialSettingParser = GetUpdateInitialSettingParser.parseGetUpdateInitialSetting(response);
                //Set Value For Selection Question1 And Question2 Option
                prevSelectedOptionQue1 = getUpdateInitialSettingParser.getQuestionOne();
                prevSelectedOptionQue2 =  getUpdateInitialSettingParser.getQuestionTwo();
                //Set Up Answer Of Question1 And Question2
                setupAnswer();
            }
        });
    }

    /**
     * Radio button Selection Of The Lose Weight
     */
    private int selectionLoseWeightRadioButton() {
        int selectedId = rg_activity_question2_lose_weight.getCheckedRadioButtonId();
        radioButtonLoseWeight = (RadioButton) getActivity().findViewById(selectedId);
        if(radioButtonLoseWeight != null) {
            tabGoalQuestion2Weight = radioButtonLoseWeight.getText().toString();
        }
        return selectedId;
    }

    /**
     * Radio button Selection Of The Gain Weight
     */
    private int selectionGainWeightRadioButton() {
        int selectedId = rg_activity_question2_gain_weight.getCheckedRadioButtonId();
        radioButtonGainWeight = (RadioButton)getActivity().findViewById(selectedId);
        if(radioButtonGainWeight != null) {
            tabGoalQuestion2Weight = radioButtonGainWeight.getText().toString();
        }
        return selectedId;
    }

    /**
     * Conform Dialog For Update Goal API
     */
    private void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getActivity().getString(R.string.dialog_tab_goal_fragment_msg));
        builder.setCancelable(false);
        String positiveText = "Yes";/*getString(android.R.string.yes);*/
        builder.setPositiveButton(positiveText,
        new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callUpdateSettingAPI();
                // positive button logic
                if (dialog != null)
                    dialog.cancel();
        }});
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int i) {
            dialog.cancel();
                                  }
        });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }
}