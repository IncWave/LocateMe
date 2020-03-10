package com.mikhailzaitsevfls.locateme.registrationActivity;

import android.view.View;

import com.mikhailzaitsevfls.locateme.interfaces.PresenterInterface;

public interface RegistrationActivityPresenterInterface extends PresenterInterface {

    void attachView(RegistrationActivityInterface activity);

    void detachView();

    void isReady();

    void startCheckingInternet();

    void stopCheckingInternet();

    void onClick(View view);

    void inputConfirmed(boolean isConfirmed);
}
