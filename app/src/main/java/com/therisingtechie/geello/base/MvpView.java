package com.therisingtechie.geello.base;

/**
 * Created by SATHISH on 13-Sep-17.
 */

public interface MvpView {

    void showProgress();

    void hideProgress();

    void showMessage(String message);

    void serviceErrorCode(int errorCode);

    void serviceCalledFailed(String tag, Throwable t);
}
