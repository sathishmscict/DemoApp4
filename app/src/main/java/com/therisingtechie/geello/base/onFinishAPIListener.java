package com.therisingtechie.geello.base;



/**
 * Created by SATHISH on 12-Sep-17.
 */

public interface onFinishAPIListener {

    void onSuccess();

    void onServiceError(int errorCode, String errorMessage);

    //void serviceCallFailed(String TAG,Throwable T);
    void onFailureServiceCalled(String TAG, Throwable T);
     void noDataFound();

    void onServiceErrorMessage(String message);





}
