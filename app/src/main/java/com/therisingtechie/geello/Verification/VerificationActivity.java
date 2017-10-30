package com.therisingtechie.geello.Verification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.therisingtechie.geello.DashBoardActivity;

import com.therisingtechie.geello.NewDashboardActivity;
import com.therisingtechie.geello.R;
import com.therisingtechie.geello.helper.CommonMethods;
import com.therisingtechie.geello.helper.NetConnectivity;
import com.therisingtechie.geello.session.SessionManager;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class VerificationActivity extends AppCompatActivity implements  VerificationView,View.OnClickListener{

    private Context context = this;

    @BindView(R.id.edt1)
    EditText edt1;

    @BindView(R.id.edt2)
     EditText edt2;

    @BindView(R.id.edt3)
    EditText edt3;

    @BindView(R.id.edt4)
    EditText edt4;

    @BindView(R.id.fabNext)
    FloatingActionButton fabNext;

    @BindView(R.id.tvResend)
    TextView tvResend;

    @BindView(R.id.tvDescr2)
    TextView tvDescr2;

    private Timer timer;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private String TAG = VerificationActivity.class.getSimpleName();
    private SpotsDialog spotsDialog;
    private VerificationPresenterImpl presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();

        spotsDialog = new SpotsDialog(context);
        spotsDialog.setCancelable(false);


        setTitle(getString(R.string.title_activity_verification));

        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        } catch (Exception e) {
            e.printStackTrace();
        }



        presenter = new VerificationPresenterImpl(this,context);


        //tvDescr2 = (TextView)findViewById(R.id.tvDescr2);
        tvDescr2.setText(getString(R.string.str_verification_descr2)+" "+ userDetails.get(SessionManager.KEY_USER_MOBILE) +"");

        //fabNext = (FloatingActionButton) findViewById(R.id.fabNext);
        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //presenter.validationCerificationCode(edt1.getText().toString()+edt2.getText().toString()+edt3.getText().toString()+edt4.getText().toString());

                navigateToDashboard();





            }
        });




        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dexter.withActivity(VerificationActivity.this)
                        .withPermission(android.Manifest.permission.READ_SMS)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {

                                presenter.sendVerificationCode();


                                //sendSMSToUser();


                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest
                                                                                   permission, PermissionToken token) {/* ... */}
                        }).check();
            }
        });


        setEditextSequesnce();

        if (NetConnectivity.isOnline(context))
        {

            timer = new Timer();
            TimerTask hourlyTask = new TimerTask() {
                @Override
                public void run() {
                    // your code here...

                    userDetails = sessionManager.getSessionDetails();

                    try {
                        userDetails.get(SessionManager.KEY_CODE);

                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    if (userDetails.get(SessionManager.KEY_RECEIVECODE)
                            .length() == 4) {
                        if (userDetails.get(SessionManager.KEY_RECEIVECODE)
                                .equals(userDetails
                                        .get(SessionManager.KEY_CODE))) {


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                    /*    txtcode.setText(""
                                                + userDetails
                                                .get(SessionManager.KEY_RECEIVECODE));*/
                                        String code  = userDetails
                                                .get(SessionManager.KEY_RECEIVECODE);

                                        edt1.setText(code.substring(0,1));
                                        edt2.setText(code.substring(1,2));
                                        edt3.setText(code.substring(2,3));
                                        edt4.setText(code.substring(3,4));

                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();

                                    }
                                    // serviceP.asmx/SetStudentVerificationStatusUpdate?type=varemp&empid=string&mobileno=string&status=string&clientid=string&branch=string


                                    try {
                                        timer.cancel();
                                        timer.purge();

                                        timer = null;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }
                            });


                        }
                    }

                }
            };

            // schedule the task to run starting now and then every hour...
            timer.schedule(hourlyTask, 0l, 1000 * 5); // 1000*10*60 every 10
            // minut
        }




        //Commeented code for stop sending sms
       /* Dexter.withActivity(VerificationActivity.this)
                .withPermission(android.Manifest.permission.READ_SMS)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                      //  sendSMSToUser();

                        presenter.sendVerificationCode();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {*//* ... *//*}

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest
                                                                           permission, PermissionToken token) {*//* ... *//*}
                }).check();*/


    }
    //onCreate Completed

    private void VerificationStatusUpdate()
    {
        sessionManager.CheckSMSVerificationActivity("",
                "1");


        Intent intent = new Intent(context, DashBoardActivity.class);
        startActivity(intent);
        finish();





       /* String url_update_status = AllKeys.WEBSITE + "UpdateVerificationStatus?type=status&mobile=" + userDetails.get(SessionManager.KEY_USER_MOBILE);
        Log.d(TAG, "URL UpdateVerificationStatus : " + url_update_status);
        StringRequest str_sendsms = new StringRequest(Request.Method.GET, url_update_status, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.d(TAG, "Response UpdateVerificationStatus : " + response);


                sessionManager.CheckSMSVerificationActivity("",
                        "1");


                if (userDetails.get(SessionManager.KEY_USER_IS_FIRST_BILL).equals("0") || userDetails.get(SessionManager.KEY_USER_IS_REFERRED).equals("1")) {
                    Intent intent = new Intent(context, SecondaryDashboard.class);
                    startActivity(intent);
                    finish();

                } else {
                    Intent intent = new Intent(context, DashBoardActivity.class);
                    startActivity(intent);
                    finish();

                }


                hideDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                if (error instanceof ServerError || error instanceof NetworkError) {
                    hideDialog();
                } else {
                    VerificationStatusUpdate();
                }


                Log.d(TAG, "Error Response :" + error.getMessage());
            }
        });
        MyApplication.getInstance().addToRequestQueue(str_sendsms);*/

    }


    private void setEditextSequesnce() {
        edt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 1) {
                    edt2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        edt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 1) {
                    edt3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        edt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 1) {
                    edt4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        edt4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 1) {
                    fabNext.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

    }




    @Override
    public void showProgress() {



        CommonMethods.showDialog(spotsDialog);

    }

    @Override
    public void hideProgress() {

        CommonMethods.hideDialog(spotsDialog);

    }

    @Override
    public void navigateToDashboard() {




        Intent intent = new Intent(context, NewDashboardActivity.class);
        startActivity(intent);
        finish();


    }
    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void serviceErrorCode(int errorCode) {
        CommonMethods.showErrorMessageWhenStatusNot200(context, errorCode);
    }

    @Override
    public void serviceCalledFailed(String tag, Throwable t) {

        CommonMethods.onFailure(context, TAG, t);

        CommonMethods.hideDialog(spotsDialog);
    }

    @Override
    public void checkValidationOfCode() {

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        hideProgress();

    }

    @Override
    public void onClick(View view) {


    }

 /*   private void showDialog() {
        if (!spotsDialog.isShowing()) {
            spotsDialog.show();

        }
    }

    private void hideDialog() {
        if (spotsDialog.isShowing()) {
            spotsDialog.dismiss();


        }
    }*/


}
