package com.therisingtechie.geello.Verification;

import android.util.Log;


import com.therisingtechie.geello.api.ApiClient;
import com.therisingtechie.geello.api.ApiInterface;
import com.therisingtechie.geello.base.onFinishAPIListener;
import com.therisingtechie.geello.helper.CommonMethods;
import com.therisingtechie.geello.model.CommonReponse;
import com.therisingtechie.geello.session.SessionManager;

import java.util.HashMap;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SATHISH on 12-Sep-17.
 */

public class VerificationInteractorImpl implements VerificationInteractor {

    private String TAG = VerificationInteractorImpl.class.getSimpleName();

    @Override
    public void getVerificationFromServer(SessionManager sessionManager, HashMap<String, String> userDetails, final onFinishAPIListener listener) {


        String sendsms = "";

        if (userDetails.get(SessionManager.KEY_CODE).equals("0"))
        {
            Random r = new Random();
            int code = r.nextInt(9999 - 1000) + 1000;
            Log.d(TAG, "Verification Code : " + code);

            Log.d(TAG, "URL sendSMS : " + sendsms);

            sessionManager.createUserSendSmsUrl("" + code);
            userDetails = sessionManager.getSessionDetails();
        } else {

            userDetails = sessionManager.getSessionDetails();
            Log.d(TAG, "URL sendSMS : " + sendsms);

        }


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Log.d(TAG, "URL getVerificatonCode  : " + CommonMethods.WEBSITE + "getVerificationCode?type=verification&code=" + userDetails.get(SessionManager.KEY_CODE) + "&mobile=" + userDetails.get(SessionManager.KEY_USER_MOBILE) + "");


        apiInterface.getVerificatonCode("verification", userDetails.get(SessionManager.KEY_CODE), userDetails.get(SessionManager.KEY_USER_MOBILE)).enqueue(new Callback<CommonReponse>() {
            @Override
            public void onResponse(Call<CommonReponse> call, Response<CommonReponse> response) {

                Log.d(TAG, "getVerificationCode Response Code : " + response.code());

                if (response.code() == 200)
                {
                    if (response.body().getERRORSTATUS() == false) {

                        listener.onSuccess();


                    } else {
                        listener.onServiceError(response.code(), "There was an error while sending the OTP to your mobile number. \n" + response.body().getORIGINALERROR());

                    }


                } else {
                    listener.onServiceError(response.code(), "There was an error while sending the OTP to your mobile number. ");



                }


            }

            @Override
            public void onFailure(Call<CommonReponse> call, Throwable t) {


                listener.onFailureServiceCalled(TAG, t);

            }
        });

    }

    @Override
    public void checkVerificationCode(String verificationCode, String receivedVerificationCode, final onFinishVerificationCheckListener listener) {

        if (verificationCode.equals(receivedVerificationCode)) {


            listener.onSuccessMatchVerificationCode();


        } else {

            listener.onFailedMatchVerificationCode("Invalid verification code");



        }


    }


}
