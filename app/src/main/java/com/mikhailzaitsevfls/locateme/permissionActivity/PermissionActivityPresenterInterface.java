package com.mikhailzaitsevfls.locateme.permissionActivity;


import com.mikhailzaitsevfls.locateme.interfaces.PresenterInterface;

public interface PermissionActivityPresenterInterface extends PresenterInterface {

    void attachView(PermissionActivityInterface activity);

    void detachView();

    void onNextButtonClicked();

    void isReady();
}
