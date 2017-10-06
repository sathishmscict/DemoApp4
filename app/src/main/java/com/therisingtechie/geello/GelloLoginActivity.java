package com.therisingtechie.geello;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.therisingtechie.geello.api.ApiClient;
import com.therisingtechie.geello.api.ApiInterface;
import com.therisingtechie.geello.helper.CommonMethods;
import com.therisingtechie.geello.model.UserDataResponse;
import com.therisingtechie.geello.request.LoginRequest;
import com.therisingtechie.geello.request.UserSimpleRegistration;
import com.therisingtechie.geello.session.SessionManager;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GelloLoginActivity extends AppCompatActivity {

    @BindView(R.id.btnRegister)
    Button btnRegister;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.edtEmail)
    EditText edtEmail;

    @BindView(R.id.edtPassword)
    EditText edtPassword;

    private Context context = this;
    private String TAG = GelloLoginActivity.class.getSimpleName();
    private SpotsDialog spotsDialog;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gello_login);

        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(getString(R.string.app_name) + " Login ");

        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // getSupportActionBar().setHomeAsUpIndicator(AllKeys.back_button);
            getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }


        spotsDialog = new SpotsDialog(context);
        spotsDialog.setCancelable(false);

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, SimpleRegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (edtEmail.getText().toString().equals("") || edtPassword.getText().toString().equals("")) {
                    CommonMethods.showAlertDialog(context, "Login Info", "Please enter email and Password");
                } else if (edtEmail.getText().toString().equals("")) {
                    CommonMethods.showAlertDialog(context, "Login Info", "Please enter email");

                } else if (edtPassword.getText().toString().equals("")) {
                    CommonMethods.showAlertDialog(context, "Login Info", "Please enter password");

                } else {
                    getLoginDetailsFromServer();

                }

                Toast.makeText(context, "Clicked on login", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getLoginDetailsFromServer() {
        ApiInterface apicInterface = ApiClient.getClient().create(ApiInterface.class);

        LoginRequest loginRequest = new LoginRequest();

        loginRequest.email = edtEmail.getText().toString();

        loginRequest.password = edtPassword.getText().toString();
        // loginRequest.login_type  = CommonMethods.LOGIN_TYPE_DIRECT;

        Log.d(TAG, "URL api/login : " + CommonMethods.WEBSITE + "api/login?body=");

        apicInterface.sendLoginDetails(loginRequest).enqueue(new Callback<UserDataResponse>() {


            @Override
            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {

                Log.d(TAG, "getLoginDetails Response Code : " + response.code());

                if (response.code() == 200) {

                    String str_error = response.body().getMessage();
                    boolean error_status = response.body().getFlag();

                    Log.d(TAG , "Errror : "+ str_error+" Stataus : "+error_status);


                    if (error_status == true) {


                        UserDataResponse.Data userData = response.body().getData();

                        String userid = userData.getId();
                        String token = userData.getToken();
                        boolean isNewUser = userData.getIsNewUser();
                        String firstname = userData.getFirstName();

                        sessionManager.setUserDetails(userData.getFirstName(), userData.getLastName(), userData.getEmail(), userData.getMobile(), userData.getImage(), userData.getIsActive(), userData.getSmsCode(), userData.getToken(), userData.getId(), userData.getIsNewUser());
                        CommonMethods.hideDialog(spotsDialog);
                        if (userData.getMobile().length() == 10) {
                            Intent intent = new Intent(context, DashBoardActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Intent intent = new Intent(context, SimpleRegistrationActivity.class);
                            startActivity(intent);
                            finish();


                        }

                    } else {
                        CommonMethods.showAlertDialog(context, "Login Info", response.body().getMessage());
                    }
                } else {
                    CommonMethods.showErrorMessageWhenStatusNot200(context, response.code());
                }


            }

            @Override
            public void onFailure(Call<UserDataResponse> call, Throwable t) {

                CommonMethods.onFailure(context, TAG, t);
                CommonMethods.hideDialog(spotsDialog);

            }
        });

    }

}
