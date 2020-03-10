package com.mikhailzaitsevfls.locateme.startActivity;

import com.mikhailzaitsevfls.locateme.login.LoginActivity;
import com.mikhailzaitsevfls.locateme.permissionActivity.PermissionActivity;

public class StartActivityPresenter implements StartActivityPresenterInterface {
    private StartActivityInterface activity;

    @Override
    public void attachView(StartActivityInterface activity) {
        this.activity = activity;
    }

    @Override
    public void detachView() {
        this.activity = null;
    }

    @Override
    public void isReady() {
        if (activity.checkPermission()){
            activity.startActivity(LoginActivity.class);
        }
    }

    @Override
    public boolean isDetached() {
        return activity == null;
    }

    @Override
    public void onNextButtonClicked() {
        activity.startActivity(PermissionActivity.class);
    }
}
