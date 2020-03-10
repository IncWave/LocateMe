package com.mikhailzaitsevfls.locateme.noInternetActivity;

import com.mikhailzaitsevfls.locateme.noInternetActivity.NoInternetActivityInterface;
import com.mikhailzaitsevfls.locateme.noInternetActivity.NoInternetActivityPresenterInterface;
import com.mikhailzaitsevfls.locateme.interfaces.PresenterInterfaceWithSecondDetach;
import com.mikhailzaitsevfls.locateme.checking.internet.CheckInternetConnection;
import com.mikhailzaitsevfls.locateme.login.LoginActivity;
import com.mikhailzaitsevfls.locateme.mainActivity.MainActivity;
import com.mikhailzaitsevfls.locateme.noInternetActivity.NoInternetActivity;
import com.mikhailzaitsevfls.locateme.permissionActivity.PermissionActivity;
import com.mikhailzaitsevfls.locateme.registrationActivity.RegistrationActivity;
import com.mikhailzaitsevfls.locateme.startActivity.StartActivity;

public class NoInternetActivityPresenter implements NoInternetActivityPresenterInterface, PresenterInterfaceWithSecondDetach {

    private NoInternetActivityInterface activity;
    private String prev_activity_name;

    @Override
    public void attachView(NoInternetActivityInterface activity) {
        this.activity = activity;
    }


    @Override
    public void detachView() {
        CheckInternetConnection.cancelChecking(this);
        this.activity = null;
    }

    @Override
    public void isReady() {}


    @Override
    public void isReady(String prev_activity_name){
        this.prev_activity_name = prev_activity_name;
    }

    @Override
    public void startCheckingInternet() {
        CheckInternetConnection.startChecking(this, false);
    }

    @Override
    public void stopCheckingInternet() {
        CheckInternetConnection.cancelChecking(this);
    }


    @Override
    public boolean isDetached() {
        return activity == null;
    }


    @Override
    public void detachViewNotFromParent() {
        switch (prev_activity_name){
            case NoInternetActivity.LOGIN_ACTIVITY:
                activity.startActivity(LoginActivity.class);
                break;
            case NoInternetActivity.MAIN_ACTIVITY:
                activity.startActivity(MainActivity.class);
                break;
            case NoInternetActivity.PERMISSION_ACTIVITY:
                activity.startActivity(PermissionActivity.class);
                break;
            case NoInternetActivity.REGISTRATION_ACTIVITY:
                activity.startActivity(RegistrationActivity.class);
                break;
            case NoInternetActivity.START_ACTIVITY:
                activity.startActivity(StartActivity.class);
                break;
        }
    }
}