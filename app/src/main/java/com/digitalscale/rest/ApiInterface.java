package com.digitalscale.rest;

import com.digitalscale.model.ForgotPassword;
import com.digitalscale.model.LoginModel;
import com.digitalscale.model.SignUpModel;
import com.digitalscale.model.UpadateInitialSetting;
import com.digitalscale.model.VerifyOtp;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Vishal Gadhiya on 3/8/2017.
 */

public interface ApiInterface {

    /* Sign up */

    @FormUrlEncoded
    @POST("register")
    Call<SignUpModel> signUp(@Field("email") String email,
                             @Field("first_name") String firstName,
                             @Field("last_name") String lastName,
                             @Field("password") String password,
                             @Field("register_by") String registerBy,
                             @Field("ref_id") String refId);


    /* Login */

    @FormUrlEncoded
    @POST("login")
    Call<LoginModel> login(@Field("email") String email,
                           @Field("password") String password);


    /* Verify OTP */
    @FormUrlEncoded
    @POST("verify_email")
    Call<VerifyOtp> verifyOtp(@Field("email") String email,
                              @Field("otp") String otp);

    /* Update Initial Setting */
    @FormUrlEncoded
    @POST("update_initial_setting")
    Call<UpadateInitialSetting> updateInitialSetting(@Field("user_id") String user_id,
                                                     @Field("question_one") String question_one,
                                                     @Field("question_two") String question_two,
                                                     @Field("question_two_weight") String question_two_weight,
                                                     @Field("question_two_unit") String question_two_unit,
                                                     @Field("height") String height,
                                                     @Field("height_unit") String height_unit,
                                                     @Field("weight") String weight,
                                                     @Field("weight_unit") String weight_unit,
                                                     @Field("dob") String dob,
                                                     @Field("gender")String gender);


    /**
     * Update Initial Setting For When Select First Option Of The Question2
     * @param user_id
     * @param question_one
     * @param question_two
     * @param height
     * @param height_unit
     * @param weight
     * @param weight_unit
     * @param dob
     * @param gender
     * @return
     */

    @FormUrlEncoded
    @POST("update_initial_setting")
    Call<UpadateInitialSetting> updateInitialSettingWithoutUnit(@Field("user_id") String user_id,
                                                                @Field("question_one") String question_one,
                                                                @Field("question_two") String question_two,
                                                                @Field("height") String height,
                                                                @Field("height_unit") String height_unit,
                                                                @Field("weight") String weight,
                                                                @Field("weight_unit") String weight_unit,
                                                                @Field("dob") String dob,
                                                                @Field("gender")String gender);


    /*****************
     * Food API
     *****************/

    /* getFood */
    /*@FormUrlEncoded
    @POST("get_food")
    void searchFood(@Field("search_text") String search_text, Callback<Response> response);*/



    /* Forgot Password */
    @FormUrlEncoded
    @POST("forgot_password")
    Call<ForgotPassword> forgot_pwd(@Field("email") String email);

}
