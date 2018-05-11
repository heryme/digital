package com.digitalscale.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.digitalscale.R;
import com.digitalscale.tools.Session;
import com.digitalscale.utility.FontUtility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

@Fullscreen
@EActivity(R.layout.activity_question2)
@WindowFeature(Window.FEATURE_NO_TITLE)
public class Question2Activity extends MasterActivity {

    @ViewById(R.id.btnQuestion2Back)
    Button btnQuestion2Back;

    @ViewById(R.id.btnQuestion2Next)
    Button btnQuestion2Next;

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

    //Set Value Base On Selection Of The Option
    public static String question2OptionID = "",question2weight = "";

    /**
     * User Session
     */
    Session session;

    @AfterViews
    public void init() {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        ll_activity_que2_child_lose_weight.setLayoutParams(params);
        ll_activity_que2_child_gain_weight.setLayoutParams(params);

        //Set Question Option Previous Selected State
        setQuestionOptionsState();

        //Set Font Style
        setFontStyle();
    }

    @Click
    public void btnQuestion2Back() {
        selectionLoseWeightRadioButton();
        selectionGainWeightRadioButton();
        startActivity(new Intent(Question2Activity.this, Question1Activity_.class));
        finish();
    }

    @Click
    public void btnQuestion2Next() {

        selectionLoseWeightRadioButton();
        selectionGainWeightRadioButton();

        //Check Validation
        checkValidation();
    }

    @Click
    public void ll_act_question2_que1() {

        question2OptionID = "opt1";

        //Set selected option value to the session
        session.setSelectionQuestion2(question2OptionID);

        //Set Option2 And Option3 Layout Height Match Parent
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        ll_activity_que2_child_lose_weight.setLayoutParams(params);
        ll_activity_que2_child_gain_weight.setLayoutParams(params);

        ll_act_question2_que2_inner_view.setVisibility(View.GONE);
        ll_act_question2_que3_inner_view.setVisibility(View.GONE);

        iv_act_question2_que1.setImageResource(R.drawable.ic_checked);
        tv_act_question2_que1.setTextColor(ContextCompat.getColor(Question2Activity.this, R.color.colorOrange));

        iv_act_question2_que2.setImageResource(R.drawable.ic_unchecked);
        tv_act_question2_que2.setTextColor(ContextCompat.getColor(Question2Activity.this, R.color.colorFontGry));
        iv_act_question2_que3.setImageResource(R.drawable.ic_unchecked);
        tv_act_question2_que3.setTextColor(ContextCompat.getColor(Question2Activity.this, R.color.colorFontGry));
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

        //Set selected option value to the session
        session.setSelectionQuestion2(question2OptionID);
        ll_act_question2_que2_inner_view.setVisibility(View.VISIBLE);
        ll_act_question2_que3_inner_view.setVisibility(View.GONE);

        iv_act_question2_que2.setImageResource(R.drawable.ic_checked);
        tv_act_question2_que2.setTextColor(ContextCompat.getColor(Question2Activity.this, R.color.colorOrange));

        iv_act_question2_que1.setImageResource(R.drawable.ic_unchecked);
        tv_act_question2_que1.setTextColor(ContextCompat.getColor(Question2Activity.this, R.color.colorFontGry));
        iv_act_question2_que3.setImageResource(R.drawable.ic_unchecked);
        tv_act_question2_que3.setTextColor(ContextCompat.getColor(Question2Activity.this, R.color.colorFontGry));

    }

    @Click
    public void ll_act_question2_que3() {

        question2OptionID = "opt3";

        //Set selected option value the session
        session.setSelectionQuestion2(question2OptionID);

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
        tv_act_question2_que3.setTextColor(ContextCompat.getColor(Question2Activity.this, R.color.colorOrange));

        iv_act_question2_que1.setImageResource(R.drawable.ic_unchecked);
        tv_act_question2_que1.setTextColor(ContextCompat.getColor(Question2Activity.this, R.color.colorFontGry));

        iv_act_question2_que2.setImageResource(R.drawable.ic_unchecked);
        tv_act_question2_que2.setTextColor(ContextCompat.getColor(Question2Activity.this, R.color.colorFontGry));
    }

    /**
     * Check Validation
     */
    private void checkValidation() {
        debugLog(TAG,"---><<---" + session.getUnit());
        debugLog(TAG,"question2OptionID >> " + question2OptionID);
        Log.d(TAG,"question2OptionID >> " + question2OptionID);

        if (question2OptionID.equals("opt1")) {
            startActivity(new Intent(Question2Activity.this, BasicDetailActivity_.class));
        } else {
            if (question2OptionID.equals("opt2")) {
                    if(selectionLoseWeightRadioButton() == -1) {
                        toastMessage(getString(R.string.toast_lose_weight));
                    }else{
                        startActivity(new Intent(this, BasicDetailActivity_.class));
                        finish();
                    }
            } else if(question2OptionID.equals("opt3")) {
                if(selectionGainWeightRadioButton() == -1) {
                    toastMessage(getString(R.string.toast_gain_option));
                }else{
                    startActivity(new Intent(this, BasicDetailActivity_.class));
                    finish();
                }
            }else {
                toastMessage(getString(R.string.toast_select_option));
            }
        }
    }

    /**
     * Set Question Option According To Previous Selected State
     */
    private void setQuestionOptionsState() {

        session = new Session(Question2Activity.this);

        if(!session.getSelectionQuestion2().equals("")){
            if(session.getSelectionQuestion2().equals("opt1")){
                //Select first option
                ll_act_question2_que1.performClick();
            }else if(session.getSelectionQuestion2().equals("opt2")) {
                //select second option
                ll_act_question2_que2.performClick();
                if(getSession().getQuestion2LoseWeight() != null &&
                        getSession().getQuestion2LoseWeight().length() > 0) {
                    if(getSession().getQuestion2LoseWeight().equals("250 g")) {
                        rb_activity_question2_lose_weight_250g.setChecked(true);
                    }else if(getSession().getQuestion2LoseWeight().equals("500 g")) {
                        rb_activity_question2_lose_weight_500g.setChecked(true);
                    }else if(getSession().getQuestion2LoseWeight().equals("750 g")) {
                        rb_activity_question2_lose_weight_750g.setChecked(true);
                    }else if(getSession().getQuestion2LoseWeight().equals("1000 g")){
                        rb_activity_question2_lose_weight_1000g.setChecked(true);
                    }
                }
            }else {
                //Select three option
                ll_act_question2_que3.performClick();
                if(getSession().getQuestion2gainWeight() != null &&
                        getSession().getQuestion2gainWeight().length() > 0) {
                    if(getSession().getQuestion2gainWeight().equals("250 g")) {
                        rb_activity_question2_gain_weight_250g.setChecked(true);
                    }else if(getSession().getQuestion2gainWeight().equals("500 g")) {
                        rb_activity_question2_gain_weight_500g.setChecked(true);
                    }else if(getSession().getQuestion2gainWeight().equals("750 g")) {
                        rb_activity_question2_gain_weight_750g.setChecked(true);
                    }else if(getSession().getQuestion2gainWeight().equals("1000 g")){
                        rb_activity_question2_gain_weight_1000g.setChecked(true);
                    }
                }
            }
        }
    }

    /**
     * Set Font Style Of The Textview
     */
    private void setFontStyle() {
        FontUtility.condLight(tv_act_question2_que1, Question2Activity.this);
        FontUtility.condLight(tv_act_question2_que2, Question2Activity.this);
        FontUtility.condLight(tv_act_question2_que3, Question2Activity.this);

        FontUtility.condLight(rb_activity_question2_lose_weight_250g,Question2Activity.this);
        FontUtility.condLight(rb_activity_question2_lose_weight_500g,Question2Activity.this);
        FontUtility.condLight(rb_activity_question2_lose_weight_750g,Question2Activity.this);
        FontUtility.condLight(rb_activity_question2_lose_weight_1000g,Question2Activity.this);

        FontUtility.condLight(rb_activity_question2_gain_weight_250g,Question2Activity.this);
        FontUtility.condLight(rb_activity_question2_gain_weight_500g,Question2Activity.this);
        FontUtility.condLight(rb_activity_question2_gain_weight_750g,Question2Activity.this);
        FontUtility.condLight(rb_activity_question2_gain_weight_1000g,Question2Activity.this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        btnQuestion2Back.performClick();
        selectionLoseWeightRadioButton();
        selectionGainWeightRadioButton();
    }

    /**
     * Radio Button Selection Of The Lose Weight
     */
    private int selectionLoseWeightRadioButton() {
        int selectedId = rg_activity_question2_lose_weight.getCheckedRadioButtonId();
        radioButtonLoseWeight = (RadioButton) findViewById(selectedId);
        if(radioButtonLoseWeight != null) {
            session.setQuestion2LoseWeight((radioButtonLoseWeight.getText().toString()));
            question2weight = radioButtonLoseWeight.getText().toString();
        }
        return selectedId;
    }


    /**
     *  Radio Button Selection Of The Gain Weight
     */
    private int selectionGainWeightRadioButton() {
        int selectedId = rg_activity_question2_gain_weight.getCheckedRadioButtonId();
        radioButtonGainWeight = (RadioButton) findViewById(selectedId);
        if(radioButtonGainWeight != null) {
            session.setQuestion2gainWeight((radioButtonGainWeight.getText().toString()));
            question2weight = radioButtonGainWeight.getText().toString();
        }
        return selectedId;
    }
}
