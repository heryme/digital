package com.digitalscale.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalscale.R;
import com.digitalscale.instalogin.InstagramApp;
import com.digitalscale.parser.UserParser;
import com.digitalscale.services.UserService;
import com.digitalscale.tools.Constant;
import com.digitalscale.utility.DialogUtility;
import com.digitalscale.utility.FontUtility;
import com.digitalscale.utility.NetworkUtility;
import com.digitalscale.utility.LoginWithGmailManager;
import com.digitalscale.utility.ValidationUtils;
import com.digitalscale.vollyrest.APIService;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import io.fabric.sdk.android.Fabric;

@Fullscreen
@EActivity(R.layout.activity_login)
@WindowFeature(Window.FEATURE_NO_TITLE)
public class LoginActivity extends MasterActivity {

    private static final int RC_GOOGLE_SIGN_IN = 9001;
    private static final int RC_FB_SIGN_IN = 64206;


    @ViewById(R.id.ivLoginFb)
    ImageView ivLoginFb;

    @ViewById(R.id.ivLoginGooglePlush)
    ImageView ivLoginGooglePlush;

    @ViewById(R.id.ivLoginInstagram)
    ImageView ivLoginInstagram;

    @ViewById(R.id.ivLoginTwitter)
    ImageView ivLoginTwitter;

    @ViewById(R.id.txtLoginCreateAccount)
    TextView txtLoginCreateAccount;

    @ViewById(R.id.twitter_login_button)
    com.twitter.sdk.android.core.identity.TwitterLoginButton twitterLoginButton;

    @ViewById(R.id.facebook_login_button)
    LoginButton facebookLoginButton;

    @ViewById(R.id.btnLoginLogin)
    Button btnLogin;

    @ViewById(R.id.edtEmail)
    EditText edtEmail;

    @ViewById(R.id.edtLoginPassword)
    EditText edtPassword;

    @ViewById(R.id.tv_login)
    TextView tv_login;

    @ViewById(R.id.tv_login_forgot_pwd)
    TextView tv_login_forgot_pwd;

    @ViewById(R.id.ll_login)
    LinearLayout ll_login;

    //Sign up components
    @ViewById(R.id.ll_sign_up)
    LinearLayout ll_sign_up;

    @ViewById(R.id.tv_login_sign_up)
    TextView tv_login_sign_up;

    @ViewById(R.id.edtSignUpFirstName)
    EditText edtSignUpFirstName;

    @ViewById(R.id.edtSignUpLastName)
    EditText edtSignUpLastName;

    @ViewById(R.id.edtSignUpEmail)
    EditText edtSignUpEmail;

    @ViewById(R.id.edtSignUpPassword)
    EditText edtSignUpPassword;

    @ViewById(R.id.btnSignUpSignUp)
    Button btnSignUpSignUp;

    InstagramApp mApp;
    CallbackManager fbCallbackManager;
    ValidationUtils validationUtils;
    LoginWithGmailManager loginWithGmailManager;

    Boolean isSignUp = false;


    boolean isBack = false;

    @AfterViews
    public void init() {

        loginWithGmailManager = new LoginWithGmailManager(LoginActivity.this);

        configureFacebook();

        //configureGoogleSignIn();

        configureInstagram();

        configureTwitter();

        objectCreator();

        //Set Font Style
        setFontStyle();

        debugLog("isFBLoggedIn() >> " + isFBLoggedIn());

        //Get Intent Data From The EmailVerification Activity
        Intent intent = getIntent();
        isSignUp = intent.getBooleanExtra("showSighUP",false);
        backFromEmailVerificationActivity();


    }

    @Click
    public void tv_login_forgot_pwd() {
        Intent intent = new Intent(LoginActivity.this,ForgotPwdActivity_.class);
        startActivity(intent);

    }

    /**
     * Set Up Facebook
     */
    private void configureFacebook() {
        fbCallbackManager = CallbackManager.Factory.create();
        facebookLoginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday"));

        facebookLoginButton.registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                debugLog(TAG,"FB login result >> " + loginResult);

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Get facebook data from login
                        //Bundle bFacebookData = getFacebookData(object);
                        debugLog(TAG,"fb login response >> " + object);
                        handleFbSignInResult(object);
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                debugLog(TAG,"FB login in cancel>> ");
            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
            }
        });

    }

    /**
     * Create Object Of The ValidationUtils
     */
    private void objectCreator() {
        validationUtils = new com.digitalscale.utility.ValidationUtils(LoginActivity.this);
    }

    /**
     * Set Up Twitter
     */
    private void configureTwitter() {

        if (!Fabric.isInitialized()) {
            TwitterAuthConfig authConfig = new TwitterAuthConfig(Constant.TWITTER_API_KEY, Constant.TWITTER_API_SECRET);
            Fabric.with(this, new Twitter(authConfig));
        }

        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                debugLog("result.data >> " + new String(result.data.toString()));
                debugLog("session >> " + new String(session.toString()));

                String email = "";
                String id = String.valueOf(session.getUserId());
                String fName = session.getUserName();
                String lName = "";
                String password = "";
                String profilePic = "";

                signUp(email, fName, lName, password, profilePic, Constant.TWITTER, id);

            }

            @Override
            public void failure(TwitterException exception) {
                debugLog(TAG, "Login with Twitter failure" + exception);
            }
        });

    }


    /**
     * Set Up Instagram
     */
    public void configureInstagram() {
        mApp = new InstagramApp(LoginActivity.this,
                Constant.INSTA_CLIENT_ID,
                Constant.INSTA_CLIENT_SECRET,
                Constant.INSTA_CALLBACK_URL);
        InstagramApp.OAuthAuthenticationListener listener = new InstagramApp.OAuthAuthenticationListener() {

            @Override
            public void onSuccess() {
                debugLog(TAG,"mApp " + mApp);
                debugLog(TAG,"Insta Connected as " + mApp.getUserName());

                String email = "";
                String fName = mApp.getName();
                String lName = "";
                String id = mApp.getId();
                String password = "";
                String profilePic = "";

                signUp(email, fName, lName, password, profilePic, Constant.INSTAGRAM, id);
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        };

        mApp.setListener(listener);

    }

    @Click
    public void txtLoginCreateAccount() {
        isBack = true;
        //Hide Login Field
        tv_login.setVisibility(View.GONE);
        txtLoginCreateAccount.setVisibility(View.GONE);
        ll_login.setVisibility(View.GONE);
        tv_login_forgot_pwd.setVisibility(View.GONE);

        //Visible sign up field
        tv_login_sign_up.setVisibility(View.VISIBLE);
        ll_sign_up.setVisibility(View.VISIBLE);

        edtSignUpEmail.setText("");
        edtSignUpFirstName.setText("");
        edtSignUpLastName.setText("");

    }

    // Login with FB
    @Click
    public void ivLoginFb() {
        if (NetworkUtility.checkIsInternetConnectionAvailable(LoginActivity.this)){
            facebookLoginButton.performClick();
        }
    }

    // Login with Twitter
    @Click
    public void ivLoginTwitter() {
        if (NetworkUtility.checkIsInternetConnectionAvailable(LoginActivity.this)){
            twitterLoginButton.performClick();
        }
    }

    // Login with Instagram
    @Click
    public void ivLoginInstagram() {
        if (NetworkUtility.checkIsInternetConnectionAvailable(LoginActivity.this)){
            mApp.authorize();
        }
    }

    // Login with Google Plush
    @Click
    public void ivLoginGooglePlush() {
        if (NetworkUtility.checkIsInternetConnectionAvailable(LoginActivity.this)){
            loginWithGoogle();
        }
    }

    // Normal login
    @Click
    public void btnLoginLogin() {
        if (NetworkUtility.checkIsInternetConnectionAvailable(LoginActivity.this) && isValidInputForLogin()) {
            // TODO: Call login api
            String email = edtEmail.getText().toString().trim();
            String psw = edtPassword.getText().toString().trim();

            HashMap<String, String> param = new HashMap<>();
            param.put("email", email);
            param.put("password", psw);
            getSession().setEmail(email);
            UserService.login(LoginActivity.this, param, new APIService.Success<JSONObject>() {
                @Override
                public void onSuccess(JSONObject response) {
                    UserParser.Login login = UserParser.Login.parseLoginResponse(response);
                    if (login.getStatus().equals("1")) {
                        debugLog("Login : success");

                        getSession().setUserId(login.getUserId());
                        getSession().setEmail(login.getEmail());
                        getSession().setFirstName(login.getFirstName());
                        getSession().setLastName(login.getLastName());
                        debugLog(TAG,"login.getProfilePic() >> " + login.getProfilePic());
                        getSession().setProfilePic(login.getProfilePic());
                        //startActivity(new Intent(LoginActivity.this, MainActivity_.class));
                        redirectTo(login.getInitialSetting());
                    } else if (login.getStatus().equals("2")) {
                        // Till user not verify
                        startActivity(new Intent(LoginActivity.this, EmailVerificationActivity_.class));
                    } else {
                        DialogUtility.dialogWithPositiveButton(login.getMessage(),LoginActivity.this);
                    }
                }
            });
        }
    }

    /**
     * Login With Gmail
     */
    private void loginWithGoogle() {
        //Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(loginWithGmailManager.mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    /**
     * Get Fb Login Response
     * @param object
     */
    private void handleFbSignInResult(JSONObject object) {
        //TODO :Handle sign result here
        String mEmail = "", mUid = "", mName = "", mFirstName = "", mLastName = "", mGender = "", mPhoto = "";
        try {
            if (object.has("email"))
                mEmail = object.getString("email");
            if (object.has("id"))
                mUid = object.getString("id");
            if (object.has("name"))
                mName = object.getString("name");
            if (object.has("first_name"))
                mFirstName = object.getString("first_name");
            if (object.has("last_name"))
                mLastName = object.getString("last_name");
            if (object.has("gender"))
                mGender = object.getString("gender");

            mPhoto = "https://graph.facebook.com/" + mUid + "/picture?type=large";
            Log.d(TAG, "mPhoto >> " + mPhoto);
            debugLog("Fb user id : " + mUid);
            signUp(mEmail, mFirstName, mLastName, "", mPhoto, Constant.FACEBOOK, mUid);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Get Response Of The Sign In With Gmail
     * @param googleSignInResult
     */
    private void handleGoogleSignInResult(GoogleSignInResult googleSignInResult) {
        //Handle google sign result here
        //Log.d(TAG, "googleSignInResult:" + googleSignInResult.isSuccess());
        if (googleSignInResult.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = googleSignInResult.getSignInAccount();

            String email = "";
            if (acct.getEmail() != null && acct.getEmail().length() > 0)
                email = acct.getEmail();

            String id = "";
            if (acct.getId() != null && acct.getId().length() > 0)
                id = acct.getId();

            String fName = "";
            if (acct.getDisplayName() != null && acct.getDisplayName().length() > 0)
                fName = acct.getDisplayName();

            String lName = "";
            String password = "";
            String idToken = "";
            if (acct.getIdToken() != null && acct.getIdToken().length() > 0)
                idToken = acct.getIdToken();

            String photoUrl = "";
            if (acct.getPhotoUrl() != null && acct.getPhotoUrl().toString().length() > 0)
                photoUrl = acct.getPhotoUrl().toString();

            signUp(email, fName, lName, password, photoUrl, Constant.GOOGLE, id);

        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    /**
     * When user login with social media at that time we must do user registration
     * @param email
     * @param fName
     * @param lName
     * @param password
     * @param profilePic
     * @param socialMediaName
     * @param id
     */
    private void signUp(final String email, final String fName, final String lName, String password, String profilePic, String socialMediaName, String id) {

        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("first_name", fName);
        params.put("last_name", lName);
        params.put("password", password);
        params.put("register_by", socialMediaName);
        params.put("ref_id", id);
        params.put("profile_pic", profilePic);
        params.put("language","en");


        UserService.signUp(LoginActivity.this, params, new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                UserParser.SignUp signUp = UserParser.SignUp.parseSignUpResponse(response);

                if (signUp.getStatus().equalsIgnoreCase("1")) {

                    getSession().setUserId(signUp.getUserId());
                    getSession().setEmail(signUp.getEmail());
                    getSession().setFirstName(signUp.getFirstName());
                    getSession().setLastName(signUp.getLastName());
                    getSession().setProfilePic(signUp.getProfilePic());
                    redirectTo(signUp.getInitialSetting());

                } else if (signUp.getStatus().equalsIgnoreCase("2")) {
                    /* Email not verified so, we move user to verification screen */
                    getSession().setEmail(email);
                    getSession().setFirstName(fName);
                    getSession().setLastName(lName);
                    getSession().setIsUserVerifyEmailId(true);
                    //toastMessage(signUp.getMessage());

                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage(signUp.getMessage());
                    builder.setCancelable(false);
                    String positiveText = "OK";/*getString(android.R.string.yes);*/
                    builder.setPositiveButton(positiveText,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // positive button logic
                                    if (dialog != null)
                                        dialog.cancel();

                                    startActivity(new Intent(LoginActivity.this, EmailVerificationActivity_.class).putExtra("email", email));
                                    finish();
                                }
                            });

                    AlertDialog dialog = builder.create();
                    // display dialog
                    dialog.show();
                } else {
                    DialogUtility.dialogWithPositiveButton(signUp.getMessage(),LoginActivity.this);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        } else if (requestCode == RC_FB_SIGN_IN) {
            fbCallbackManager.onActivityResult(requestCode, resultCode, data);
        } else {
            twitterLoginButton.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean isFBLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public void googleLogout() {
        if(mGoogleApiClient != null){
        // do your stuff with Google Api Client
            Log.d(TAG,"Google Client--> "+Auth.GoogleSignInApi);
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if(status.isSuccess()){
                            Log.d(TAG,"Succesfully Log Out");
                        }

                    }
                });
        }
    }


    private void FbLogout() {
        LoginManager.getInstance().logOut();

    }

    /**
     * Check login validation
     * @return
     */
    public boolean isValidInputForLogin() {
        if (validationUtils.isValidEditText(edtEmail)
                && validationUtils.isValidEditText(edtPassword)
                && validationUtils.isValidEditTextEmail(edtEmail)
                && validationUtils.isValidEditTextPassword(edtPassword)) {
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginWithGmailManager.getClient().disconnect();
         //mGoogleApiClient.stopAutoManage(LoginActivity.this);
        //mGoogleApiClient.disconnect();

    }

    @Override
    protected void onStop() {
        super.onStop();
        loginWithGmailManager.getClient().disconnect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginWithGmailManager.getClient().connect();
    }

     void redirectTo(String initialSetting) {
        if (initialSetting.equalsIgnoreCase("0")) {
            startActivity(new Intent(LoginActivity.this, Question1Activity_.class));
        } else {
            getSession().setIsUserFillBasicDetails(true);
            startActivity(new Intent(LoginActivity.this, MainActivity_.class));
        }
    }

    //--------------------  Sign Up Module ------------------------

    @Click
    public void btnSignUpSignUp() {
        if (NetworkUtility.checkIsInternetConnectionAvailable(LoginActivity.this))
        signUp();
    }

    /**
     * User Do Simple SighUp
     */
    private void signUp() {
        if (isValidInputForSignUp()) {
            //Todo: Call sign up api
            String firstName = edtSignUpFirstName.getText().toString().trim();
            String latsName = edtSignUpLastName.getText().toString().trim();
            String email = edtSignUpEmail.getText().toString().trim();
            String password = edtSignUpPassword.getText().toString().trim();
            String profilePic = "";

            signUp(email, firstName, latsName, password, profilePic, "", "");
        }
    }

    /**
     * Check sign up validation
     * @return
     */
    private boolean isValidInputForSignUp() {
        if (validationUtils.isValidEditText(edtSignUpFirstName) &&
                validationUtils.isValidEditText(edtSignUpLastName) &&
                validationUtils.isValidEditText(edtSignUpEmail) &&
                validationUtils.isValidEditText(edtSignUpPassword) &&
                validationUtils.isValidEditTextEmail(edtSignUpEmail) &&
                validationUtils.isValidEditTextPassword(edtSignUpPassword)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        //Hide sign up field
        tv_login_sign_up.setVisibility(View.GONE);
        ll_sign_up.setVisibility(View.GONE);

        if(isBack) {
            debugLog("in if");
            //Visible login field
            tv_login.setVisibility(View.VISIBLE);
            txtLoginCreateAccount.setVisibility(View.VISIBLE);
            ll_login.setVisibility(View.VISIBLE);
            tv_login_forgot_pwd.setVisibility(View.VISIBLE);
            isBack = false;
        }else {
            //super.onBackPressed();
            debugLog("in else");
            Intent intent = new Intent(getApplicationContext(), SplashActivity_.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Set Font Style Textview,Edittext And Button
     */
    private void setFontStyle(){
        FontUtility.condLight(tv_login,LoginActivity.this);
        FontUtility.condLight(tv_login_sign_up,LoginActivity.this);
        FontUtility.condLight(tv_login_forgot_pwd,LoginActivity.this);
        FontUtility.condLight(txtLoginCreateAccount,LoginActivity.this);

        FontUtility.condLight(edtEmail,LoginActivity.this);
        FontUtility.condLight(edtSignUpFirstName,LoginActivity.this);
        FontUtility.condLight(edtSignUpLastName,LoginActivity.this);
        FontUtility.condLight(edtSignUpEmail,LoginActivity.this);

        FontUtility.condLight(btnLogin,LoginActivity.this);
        FontUtility.condLight(btnSignUpSignUp,LoginActivity.this);

        FontUtility.condLight(edtPassword,LoginActivity.this);
        FontUtility.condLight(edtSignUpPassword,LoginActivity.this);
    }

    /**
     * Show Or Hide Login Or Sign Up View
     * Also Set Sigh Up Details First Name,Last Name,Email ID
     */
    private void backFromEmailVerificationActivity() {
        if(isSignUp) {
            isBack = true;
            tv_login.setVisibility(View.GONE);
            txtLoginCreateAccount.setVisibility(View.GONE);
            ll_login.setVisibility(View.GONE);
            tv_login_forgot_pwd.setVisibility(View.GONE);

            //Visible sign up field
            tv_login_sign_up.setVisibility(View.VISIBLE);
            ll_sign_up.setVisibility(View.VISIBLE);

            edtSignUpEmail.setText(getSession().getEmail());
            edtSignUpFirstName.setText(getSession().getFirstName());
            edtSignUpLastName.setText(getSession().getLastName());
        }
    }
}
