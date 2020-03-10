package com.mikhailzaitsevfls.locateme.permissionActivity;

import com.mikhailzaitsevfls.locateme.interfaces.PresenterInterfaceWithCallback;
import com.mikhailzaitsevfls.locateme.login.LoginActivity;

public class PermissionActivityPresenter implements PermissionActivityPresenterInterface, PresenterInterfaceWithCallback {

    private PermissionActivityInterface activity;


    @Override
    public void attachView(PermissionActivityInterface activity) {
        this.activity = activity;
    }


    @Override
    public void detachView(){
        this.activity = null;
    }


    @Override
    public void isReady(){
        if (activity.checkPermissions()){
            activity.startActivity(LoginActivity.class);
        }
    }

    @Override
    public boolean isDetached() {
        return activity == null;
    }


    @Override
    public void onNextButtonClicked() {
        activity.requirePermissions();
    }


    @Override
    public void callBack(Boolean bool) {
        if (bool){
            activity.startActivity(LoginActivity.class);
        }else {
            activity.finishActivity();
        }
    }
}