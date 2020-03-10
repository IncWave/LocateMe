package com.mikhailzaitsevfls.locateme.login;

import android.view.View;

import com.mikhailzaitsevfls.locateme.interfaces.PresenterInterface;
import com.mikhailzaitsevfls.locateme.login.LoginActivityInterface;

public interface LoginActivityPresenterInterface extends PresenterInterface {

    void attachView(LoginActivityInterface activity);

    void detachView();

    void isReady();

    void startCheckingInternet();

    void stopCheckingInternet();

    void onClick(View clickedView);

    void inputConfirmed(boolean isConfirmed);
}
