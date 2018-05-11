package com.digitalscale.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digitalscale.R;
import com.digitalscale.utility.FontUtility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

@Fullscreen
@EActivity(R.layout.activity_question1)
@WindowFeature(Window.FEATURE_NO_TITLE)
public class Question1Activity extends MasterActivity {

    @ViewById(R.id.llQuestion1Answer)
    LinearLayout llQuestion1Answer;

    @ViewById(R.id.btnQuestion1Next)
    Button btnQuestion1Next;

    /* private String arrAnswer[] = {"Little or no exercise",
             "Exercise 1-3 days per week",
             "Exercise 3-5 days per week",
             "Exercise 6-7 days per week",
             "Exercise daily"};*/
   /* private String arrAnswer[] = {getString(R.string.lbl_llitel_or_no_execise),
            getString(R.string.lbl_exercise_1_3_daysperweek),
            getString(R.string.lbl_exercise_3_5_daysperweek),
            getString(R.string.lbl_exercise_6_7_daysperweek),
            getString(R.string.lbl_exercise_daily)};*/

    public static String question1OptionID = "";

    @AfterViews
    public void init() {
        setupAnswer();
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
        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(llParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        linearLayout.setId(id);

        // Check mark
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final ImageView imgCheckMark = new ImageView(this);
        imgCheckMark.setLayoutParams(imgParams);
        imgCheckMark.setImageResource(R.drawable.ic_unchecked);
        linearLayout.addView(imgCheckMark);

        // Answer text
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvParams.setMargins(20, 0, 0, 0);
        final TextView tvAnswer = new TextView(this);

        //Set Font Style
        FontUtility.condLight(tvAnswer, Question1Activity.this);
        tvAnswer.setLayoutParams(tvParams);

        linearLayout.setGravity(Gravity.LEFT | Gravity.CENTER);
        tvAnswer.setTextColor(ContextCompat.getColor(this, R.color.colorFontGry));
        tvAnswer.setText(ansText);
        tvAnswer.setTextSize(20f);
        linearLayout.addView(tvAnswer);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetViews(linearLayout);
                imgCheckMark.setImageResource(R.drawable.ic_checked);
                tvAnswer.setTextColor(ContextCompat.getColor(Question1Activity.this, R.color.colorOrange));
                Log.d(TAG, "id-->" + id);
                question1OptionID = "opt" + String.valueOf(id + 1);
                getSession().setSelectionQuestion1(question1OptionID);

            }
        });

        llQuestion1Answer.addView(linearLayout);

        //Set Previous Selected Option State
        setQuestionOptionsState(id,imgCheckMark,tvAnswer);
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
            tv.setTextColor(ContextCompat.getColor(this, R.color.colorFontGry));
        }
    }

    @Click
    public void btnQuestion1Next() {
        debugLog("question1OptionID >> " + question1OptionID);
        if (!question1OptionID.equals("")) {
            startActivity(new Intent(Question1Activity.this, Question2Activity_.class));
        } else {
            toastMessage(getString(R.string.toast_select_option));
        }
    }

    @Override
    public void onBackPressed() {
        // Stop device back key working
        //super.onBackPressed();
    }

    /**
     * Set Option Selection According To The sharedpreferences
     */
    private void setQuestionOptionsState(int id, ImageView imgCheckMark, TextView tvAnswer) {
        if ((getSession().getSelectionQuestion1() != null)) {
            debugLog(TAG,"Id--> " + id);
            debugLog(TAG,"Shareprefarance id --> " + getSession().getSelectionQuestion1());
            if (getSession().getSelectionQuestion1().equals("opt1")) {
                if(id == 0){
                    question1OptionID = getSession().getSelectionQuestion1();
                    imgCheckMark.setImageResource(R.drawable.ic_checked);
                    tvAnswer.setTextColor(ContextCompat.getColor(Question1Activity.this, R.color.colorOrange));
                }
            }else if(getSession().getSelectionQuestion1().equals("opt2")){
                if(id == 1){
                    question1OptionID = getSession().getSelectionQuestion1();
                    imgCheckMark.setImageResource(R.drawable.ic_checked);
                    tvAnswer.setTextColor(ContextCompat.getColor(Question1Activity.this, R.color.colorOrange));
                }
            }else if(getSession().getSelectionQuestion1().equals("opt3")){
                if(id == 2){
                    question1OptionID = getSession().getSelectionQuestion1();
                    imgCheckMark.setImageResource(R.drawable.ic_checked);
                    tvAnswer.setTextColor(ContextCompat.getColor(Question1Activity.this, R.color.colorOrange));
                }
            }else if(getSession().getSelectionQuestion1().equals("opt4")){
                if(id == 3){
                    question1OptionID = getSession().getSelectionQuestion1();
                    imgCheckMark.setImageResource(R.drawable.ic_checked);
                    tvAnswer.setTextColor(ContextCompat.getColor(Question1Activity.this, R.color.colorOrange));
                }
            }else if(getSession().getSelectionQuestion1().equals("opt5")){
                if(id == 4){
                    question1OptionID = getSession().getSelectionQuestion1();
                    imgCheckMark.setImageResource(R.drawable.ic_checked);
                    tvAnswer.setTextColor(ContextCompat.getColor(Question1Activity.this, R.color.colorOrange));
                }
            }
        }
    }
}
