package com.mikhailzaitsevfls.locateme.noInternetActivity;

import com.mikhailzaitsevfls.locateme.interfaces.PresenterInterface;
import com.mikhailzaitsevfls.locateme.noInternetActivity.NoInternetActivityInterface;

public interface NoInternetActivityPresenterInterface extends PresenterInterface {
    void attachView(NoInternetActivityInterface activity);

    void detachView();

    void isReady(String prev_activity_name);

    void startCheckingInternet();

    void stopCheckingInternet();
}
