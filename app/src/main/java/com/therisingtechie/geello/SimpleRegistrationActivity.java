package com.therisingtechie.geello;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.therisingtechie.geello.Verification.VerificationActivity;
import com.therisingtechie.geello.api.ApiClient;
import com.therisingtechie.geello.api.ApiInterface;
import com.therisingtechie.geello.helper.CommonMethods;
import com.therisingtechie.geello.model.UserDataResponse;
import com.therisingtechie.geello.request.UserSimpleRegistration;
import com.therisingtechie.geello.session.SessionManager;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimpleRegistrationActivity extends AppCompatActivity {

    private Context context = this;

    @BindView(R.id.btnRegister)
    Button btnRegister;


    @BindView(R.id.edtFirstName)
    EditText edtFirstName;

    @BindView(R.id.edtFirstNameWrapper)
    TextInputLayout edtFirstNameWrapper;

    @BindView(R.id.edtLastName)
    EditText edtLastName;

    @BindView(R.id.edtLastNameWrapper)
    TextInputLayout edtLastNameWrapper;

    @BindView(R.id.edtEmail)
    EditText edtEmail;

    @BindView(R.id.edtEmailWrapper)
    TextInputLayout edtEmailWrapper;


    @BindView(R.id.edtMobile)
    EditText edtMobile;

    @BindView(R.id.tvMobileError)
    TextView tvMobileError;


    @BindView(R.id.edtPassword)
    EditText edtPassword;

    @BindView(R.id.edtPasswordWrapper)
    TextInputLayout edtPasswordWrapper;
    private String TAG = SimpleRegistrationActivity.class.getSimpleName();
    private SpotsDialog spotsDialog;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails= new HashMap<String, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_registration);

        ButterKnife.bind(this);

        setTitle(getString(R.string.app_name) + " App Registration");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().show();
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }


        spotsDialog = new SpotsDialog(context);
        spotsDialog.setCancelable(false);

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();


        edtEmail.setText(userDetails.get(SessionManager.KEY_USER_EMAIL));
        edtFirstName.setText(userDetails.get(SessionManager.KEY_FIRST_NAME));
        edtLastName.setText(userDetails.get(SessionManager.KEY_LAST_NAME));
        edtMobile.setText(userDetails.get(SessionManager.KEY_USER_MOBILE));

        if(userDetails.get(SessionManager.KEY_USER_EMAIL).equals(""))
        {
            edtEmail.setFocusableInTouchMode(true);
        }






        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent intent = new Intent(context , SocialRegistrationActivity.class);
                startActivity(intent);
                finish();*/

                boolean isError = false;

                if (edtFirstName.getText().toString().equals("")) {

                    isError = true;
                    edtFirstNameWrapper.setErrorEnabled(true);
                    edtFirstNameWrapper.setError("Enter First name");


                }
                else
                {

                    edtFirstNameWrapper.setErrorEnabled(false);
                }

                if (edtLastName.getText().toString().equals("")) {

                    isError = true;
                    edtLastNameWrapper.setErrorEnabled(true);
                    edtLastNameWrapper.setError("Enter Last name");
                }
                else
                {
                    edtLastNameWrapper.setErrorEnabled(false);
                }

                if (edtEmail.getText().toString().equals("")) {

                    isError = true;
                    edtEmailWrapper.setErrorEnabled(true);
                    edtEmailWrapper.setError("Enter Email");
                }
                else
                {
                    edtEmailWrapper.setErrorEnabled(false);
                }

                if (edtMobile.getText().toString().equals("")) {

                    isError = true;
                    tvMobileError.setText("Enter Mobile no");
                }
                else
                {
                    if(edtMobile.getText().toString().length() != 10 )
                    {
                        tvMobileError.setText("Invalid Mobile no");

                    }
                    else
                    {

                    tvMobileError.setText("");
                    }
                }


                if (edtPassword.getText().toString().equals("")) {

                    isError = true;
                    edtPasswordWrapper.setErrorEnabled(true);
                    edtPasswordWrapper.setError("Enter Password");
                }
                else
                {
                    edtPasswordWrapper.setErrorEnabled(false);
                }



                if(isError == false)
                {

                    sendRegistrationDetaislToServer();
                }
            }
        });

    }

    private void sendRegistrationDetaislToServer() {

        ApiInterface apicInterface = ApiClient.getClient().create(ApiInterface.class);

        UserSimpleRegistration usr= new UserSimpleRegistration();
        usr.first_name = edtFirstName.getText().toString();
        usr.last_name = edtLastName.getText().toString();
        usr.email = edtEmail.getText().toString();
        usr.mobile = edtMobile.getText().toString();
        usr.password = edtPassword.getText().toString();
        usr.login_type  = CommonMethods.LOGIN_TYPE_DIRECT;

        Log.d(TAG, "URL api/register : " + CommonMethods.WEBSITE + "api/register?body=");

        apicInterface.sendRegistrationDetails(usr).enqueue(new Callback<UserDataResponse>() {


            @Override
            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {

                Log.d(TAG, "GetComplainData Response Code : " + response.code());

                if (response.code() == 200) {

                    String str_error = response.body().getMessage();
                    boolean error_status = response.body().getFlag();


                    if (error_status == true) {


                        UserDataResponse.Data userData =  response.body().getData();

                        String  userid = userData.getId();
                        String token = userData.getToken();
                        boolean isNewUser  = userData.getIsNewUser();
                        String firstname = userData.getFirstName();

                        sessionManager.setUserDetails(userData.getFirstName() , userData.getLastName(),userData.getEmail() , userData.getMobile(),userData.getImage(),userData.getIsActive(),userData.getSmsCode(),userData.getToken(),userData.getId(),userData.getIsNewUser());


                        CommonMethods.hideDialog(spotsDialog);
                        Intent intent = new Intent(context, VerificationActivity.class);
                        startActivity(intent);
                        finish();



                    }
                } else {
                    CommonMethods.showErrorMessageWhenStatusNot200(context, response.code());
                }
                CommonMethods.hideDialog(spotsDialog);


                Intent intent = new Intent(context , DashBoardActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onFailure(Call<UserDataResponse> call, Throwable t) {

                CommonMethods.onFailure(context, TAG, t);
                CommonMethods.hideDialog(spotsDialog);

            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(context, GelloLoginActivity.class);
            startActivity(intent);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, GelloLoginActivity.class);
        startActivity(intent);
        finish();
    }
}
