package com.therisingtechie.geello.Verification;

/**
 * Created by SATHISH on 12-Sep-17.
 */

public interface VerificationPresenter {



    void onDestroy();

    void sendVerificationCode();

    void validationCerificationCode(String verificationCode);



}
