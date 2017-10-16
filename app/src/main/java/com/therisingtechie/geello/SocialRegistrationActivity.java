package com.therisingtechie.geello;

import android.Manifest;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.therisingtechie.geello.Verification.VerificationActivity;
import com.therisingtechie.geello.api.ApiClient;
import com.therisingtechie.geello.api.ApiInterface;
import com.therisingtechie.geello.helper.CommonMethods;
import com.therisingtechie.geello.model.UserDataResponse;
import com.therisingtechie.geello.request.UserSimpleRegistration;
import com.therisingtechie.geello.request.UserSocialRegistration;
import com.therisingtechie.geello.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SocialRegistrationActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    @BindView(R.id.tvAlready)
    TextView tvAlready;

    // @BindView(R.id.btnFacebookLogin)
    Button login_fb_button;

    @BindView(R.id.btnMenu)
    Button btnMenu;


    //@BindView(R.id.btnGmailLogin)
    Button login_google_button;

    private Context context = this;
    private CallbackManager callbackManager;

    GoogleApiClient google_api_client;
    GoogleApiAvailability google_api_availability;


    String FIRST_NAME = "", LAST_NAME = "";

    /*@BindView(R.id.login_button)
    LoginButton login;
    @BindView(R.id.sign_in_button)
    SignInButton signIn_btn;*/

    LoginButton login;
    SignInButton signIn_btn;

    private String PROVIDER_USERID = "0";

    private int LOGIN_TYPE;
    private String USER_EMAIL = "";

    private static final int SIGN_IN_CODE = 0;
    private static final int PROFILE_PIC_SIZE = 120;


    private ConnectionResult connection_result;
    private boolean is_intent_inprogress;
    private boolean is_signInBtn_clicked;
    private int request_code;
    private SpotsDialog spotsDialog;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private String TAG = SocialRegistrationActivity.class.getSimpleName();

    @BindView(R.id.btnGelloLogin)
    Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppEventsLogger.activateApp(this);


        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_social_registration);
        callbackManager = CallbackManager.Factory.create();
        ButterKnife.bind(this);


        AppEventsLogger.activateApp(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        spotsDialog = new SpotsDialog(context);
        spotsDialog.setCancelable(false);

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();
        sessionManager.setSocialLoginProviderIdDetails("");


        getSupportActionBar().hide();


        login = (LoginButton) findViewById(R.id.login_button);
        signIn_btn = (SignInButton) findViewById(R.id.sign_in_button);


        tvAlready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SimpleRegistrationActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sessionManager.setLoginType(CommonMethods.LOGIN_TYPE_DIRECT);
                Intent intent = new Intent(context, GelloLoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

            }
        });


        ArrayList<String> permissions = new ArrayList<>();
        permissions.add("public_profile");
        permissions.add("email");
        //  permissions.add("user_birthday");
        // permissions.add("user_friends");
        login_fb_button = (Button) findViewById(R.id.btnFacebookLogin);
        login_google_button = (Button) findViewById(R.id.btnGmailLogin);

        login.setReadPermissions(permissions);



        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                sessionManager.setGuestUserDetails();
                Intent intent = new Intent(context, DashBoardActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

            }
        });

        login_fb_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LOGIN_TYPE = CommonMethods.LOGIN_TYPE_FACEBOOK;


                sessionManager.setLoginType(CommonMethods.LOGIN_TYPE_FACEBOOK);

                login.performClick();
            }
        });

        login_google_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LOGIN_TYPE = CommonMethods.LOGIN_TYPE_GOOGLE;
                //LOGIN_TYPE = "gmail";
                //passwordEditText.setText("");
                //userNameEditText.setText("");


                Dexter.withActivity(SocialRegistrationActivity.this)
                        .withPermission(android.Manifest.permission.GET_ACCOUNTS)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                /* ... */
                                gPlusSignIn();

                                sessionManager.setLoginType(CommonMethods.LOGIN_TYPE_GOOGLE);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                /* ... */
                                token.continuePermissionRequest();
                            }
                        }).check();


            }
        });


        login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                //passwordEditText.setText("");
                //userNameEditText.setText("");


                AccessToken accessToken = loginResult.getAccessToken();
                // AccessToken.getCurrentAccessToken().getToken();
                // App code
                GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        if (response.getError() != null) {
                            Log.e(TAG, "Error in Response " + response);
                        } else {
                            String fbUserId = object.optString("id");
                            PROVIDER_USERID = fbUserId;

                            try {
                                USER_EMAIL = object.optString("email");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            FIRST_NAME = object.optString("name");
                            try {
                                LAST_NAME = object.optString("last_name");
                                FIRST_NAME = object.optString("first_name");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            String fbUserProfilePics = "http://graph.facebook.com/" + fbUserId + "/picture?type=large";
                            Log.e(TAG, "Json Object Data " + object + " Email id " + USER_EMAIL + " Name :" + FIRST_NAME);


                            //http://graph.facebook.com//picture?type=large

                            //http://graph.facebook.com/800089893478559/picture?type=large

                            //http://graph.facebook.com/799197813567767/picture?type=large

                            sendLoginDetailsToServer();

                            sessionManager.setUserImageUrl(fbUserProfilePics);
                            LoginManager.getInstance().logOut();


                        }


                    }
                });
                Bundle bundle = new Bundle();
                bundle.putString("fields", "id,name,email,gender,birthday,first_name,last_name");
                graphRequest.setParameters(bundle);
                graphRequest.executeAsync();


                //Intent in = new Intent(context, NewDashBoardActivity.class);
                /*sessionmanager.createstatusKEy("1");*/
                   /* in.putExtra("Name",text);
                    in.putExtra("Email",email);
                    in.putExtra("profile",prifle);*/
                // startActivity(in);
                // finish();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


        buidNewGoogleApiClient();

    }
    //onCreate completed


    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }

    void deleteFacebookApplication() {
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions", null, HttpMethod.DELETE, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                boolean isSuccess = false;
                try {
                    isSuccess = response.getJSONObject().getBoolean("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (isSuccess && response.getError() == null) {
                    // Application deleted from Facebook account
                }

            }
        }).executeAsync();
    }


    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
//64206,-1,dalvik.system.PathClassLoader[DexPathList[[zip file "/data/app/com.yelona-2/base.apk", zip file "/data/app/com.yelona-2/split_lib_dependencies_apk.apk", zip file "/data/app/com.yelona-2/split_lib_slice_0_apk.apk", zip file "/data/app/com.yelona-2/split_lib_slice_1_apk.apk", zip file "/data/app/com.yelona-2/split_lib_slice_2_apk.apk", zip file "/data/app/com.yelona-2/split_lib_slice_3_apk.apk", zip file "/data/app/com.yelona-2/split_lib_slice_4_apk.apk", zip file "/data/app/com.yelona-2/split_lib_slice_5_apk.apk", zip file "/data/app/com.yelona-2/split_lib_slice_6_apk.apk", zip file "/data/app/com.yelona-2/split_lib_slice_7_apk.apk", zip file "/data/app/com.yelona-2/split_lib_slice_8_apk.apk", zip file "/data/app/com.yelona-2/split_lib_slice_9_apk.apk"],nativeLibraryDirectories=[/data/app/com.yelona-2/lib/arm, /system/lib, /vendor/lib]]]
        if (login_fb_button.getText().toString().toLowerCase().contains("facebook")) {
            callbackManager.onActivityResult(requestCode, responseCode, data);
        }
        if (requestCode == SIGN_IN_CODE) {
            request_code = requestCode;
            if (responseCode != RESULT_OK) {
                is_signInBtn_clicked = false;
                CommonMethods.hideDialog(spotsDialog);

            }

            is_intent_inprogress = false;

            if (!google_api_client.isConnecting()) {
                google_api_client.connect();
            }
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Log.d("GOOGLE SIGN IN", "handleSignInResult:" + result.isSuccess());
            // Signed in successfully, show authenticated UI.
           /* GoogleSignInAccount acct = result.getSignInAccount();
            UserUtil util = new UserUtil();
            SmartGoogleUser googleUser = util.populateGoogleUser(acct);*/


        }

    }


  /*  public void onClick(View v) {
        if (v == login_fb_button) {

            fackbook = login_fb_button.getText().toString();
            login.performClick();
        }
        if (v == login_google_button) {
            //signIn_btn.performClick();

            gPlusSignIn();
        }

    }*/

    //gmail here
    private void buidNewGoogleApiClient() {

        google_api_client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

    }

    protected void onStart() {
        super.onStart();
        google_api_client.connect();
    }

    protected void onStop() {
        super.onStop();
        if (google_api_client.isConnected()) {
            google_api_client.disconnect();
        }
    }

    protected void onResume() {
        super.onResume();
        if (google_api_client.isConnected()) {
            google_api_client.connect();
        }
    }

    public void onConnectionFailed(ConnectionResult result) {
        try {
            if (!result.hasResolution()) {
                google_api_availability.getErrorDialog(this, result.getErrorCode(), request_code).show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!is_intent_inprogress) {

            connection_result = result;

            if (is_signInBtn_clicked) {

                resolveSignInError();
            }
        }

    }

    @Override
    public void onConnected(Bundle arg0) {
        is_signInBtn_clicked = false;
        // Get user's information and set it into the layout
        getProfileInfo();
        //Intent in = new Intent(context, DashBoardActivity.class);
        /*sessionmanager.createstatusKEy("0");*/
        //startActivity(in);
        // finish();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        google_api_client.connect();
    }

    private void gPlusSignIn() {
        if (!google_api_client.isConnecting()) {
            Log.d("user connected", "connected");
            is_signInBtn_clicked = true;
            CommonMethods.showDialog(spotsDialog);
            resolveSignInError();

        }
    }

    private void getProfileInfo() {

        try {

            if (Plus.PeopleApi.getCurrentPerson(google_api_client) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(google_api_client);
                setPersonalInfo(currentPerson);
                currentPerson.getDisplayName();


                FIRST_NAME = currentPerson.getName().getGivenName();
                LAST_NAME = currentPerson.getName().getMiddleName();
                FIRST_NAME = currentPerson.getDisplayName();
                String email = currentPerson.getId();
                Log.d("Emailid", email);


                sessionManager.setUserImageUrl(currentPerson.getImage().getUrl().toString());


            } else {
                Toast.makeText(getApplicationContext(),
                        "No Personal info mention", Toast.LENGTH_LONG).show();

                CommonMethods.hideDialog(spotsDialog);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setPersonalInfo(Person currentPerson) {

        try {
            String personName = currentPerson.getDisplayName();
            String personPhotoUrl = currentPerson.getImage().getUrl();
            PROVIDER_USERID = currentPerson.getId();
            FIRST_NAME = currentPerson.getName().getFamilyName();
            LAST_NAME = currentPerson.getName().getGivenName();

            sessionManager.setUserImageUrl(personPhotoUrl);
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {


                }
            }
            USER_EMAIL = Plus.AccountApi.getAccountName(google_api_client);

            Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
            android.accounts.Account[] accounts = AccountManager.get(context).getAccountsByType(
                    "com.google");
            for (android.accounts.Account account : accounts) {
                if (emailPattern.matcher(account.name).matches()) {
                    String email12 = account.name;
                    Log.i("MY_EMAIL_count", "" + email12);
                }
            }

            //email12 = currentPerson.getId();

            Log.d("nameuser", personName);
            Log.d("emailuser", USER_EMAIL);
            ///gss

            sendLoginDetailsToServer();
            //progress_dialog.dismiss();
            CommonMethods.hideDialog(spotsDialog);

            // sessionmanager.createsavegmaildetails(personName,personPhotoUrl,email12);
            gPlusSignOut();
            gPlusRevokeAccess();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resolveSignInError() {
        if (connection_result.hasResolution()) {
            try {
                is_intent_inprogress = true;
                connection_result.startResolutionForResult(this, SIGN_IN_CODE);
                Log.d("resolve error", "sign in error resolved");
            } catch (IntentSender.SendIntentException e) {
                is_intent_inprogress = false;
                google_api_client.connect();
            }
        }
    }

    private void gPlusSignOut() {
        if (google_api_client.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(google_api_client);
            google_api_client.disconnect();
            google_api_client.connect();
        }
    }

    private void gPlusRevokeAccess() {
        if (google_api_client.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(google_api_client);
            Plus.AccountApi.revokeAccessAndDisconnect(google_api_client)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.d("MainActivity", "User access revoked!");
                            buidNewGoogleApiClient();
                            google_api_client.connect();
                        }

                    });
        }
    }


    private void sendLoginDetailsToServer() {


        ApiInterface apicInterface = ApiClient.getClient().create(ApiInterface.class);

        UserSocialRegistration usr = new UserSocialRegistration();
        usr.first_name = FIRST_NAME;
        usr.last_name = LAST_NAME;
        usr.provider_id = PROVIDER_USERID;
        usr.login_type = LOGIN_TYPE;
        usr.email = USER_EMAIL;

        Log.d(TAG, "URL api/register : " + CommonMethods.WEBSITE + "api/register?body=" + usr.toString());

        apicInterface.sendSocialRegistrationDetails(usr).enqueue(new Callback<UserDataResponse>() {


            @Override
            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {

                Log.d(TAG, "GetComplainData Response Code : " + response.code());

                String mobiel = "";
                if (response.code() == 200) {

                    sessionManager.setSocialLoginProviderIdDetails(PROVIDER_USERID);
                    String str_error = response.body().getMessage();
                    boolean error_status = response.body().getFlag();


                    if (error_status == true) {


                        UserDataResponse.Data userData = response.body().getData();

                        String userid = userData.getId();
                        String token = userData.getToken();
                        boolean isNewUser = userData.getIsNewUser();
                        String firstname = userData.getFirstName();

                        mobiel = userData.getMobile();
                        sessionManager.setUserDetails(userData.getFirstName(), userData.getLastName(), USER_EMAIL, userData.getMobile(), userData.getImage(), userData.getIsActive(), userData.getSmsCode(), userData.getToken(), userData.getId(),false);


                    }
                } else {
                    CommonMethods.showErrorMessageWhenStatusNot200(context, response.code());
                }
                CommonMethods.hideDialog(spotsDialog);

                if (mobiel.length() == 10) {

                    //Replace as Verification screen
                    Intent intent = new Intent(context, VerificationActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                } else {
                    Intent intent = new Intent(context, SimpleRegistrationActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                }


            }

            @Override
            public void onFailure(Call<UserDataResponse> call, Throwable t) {

                CommonMethods.onFailure(context, TAG, t);
                CommonMethods.hideDialog(spotsDialog);

            }
        });


        CommonMethods.showDialog(spotsDialog);

        String url_check_credentials = null;
        String fcm_tokenid = "";
        try {
            MyFirebaseInstanceIDService mid = new MyFirebaseInstanceIDService();
            fcm_tokenid = String.valueOf(mid.onTokenRefreshNew(context));
        } catch (Exception e) {
            fcm_tokenid = "";
            e.printStackTrace();
        }


    }


}
