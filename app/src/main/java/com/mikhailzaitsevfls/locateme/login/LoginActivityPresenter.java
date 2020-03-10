package com.mikhailzaitsevfls.locateme.login;

import android.view.View;

import com.mikhailzaitsevfls.locateme.interfaces.PresenterInterfaceWithSecondDetach;
import com.mikhailzaitsevfls.locateme.model.Db;
import com.mikhailzaitsevfls.locateme.checking.internet.CheckInternetConnection;
import com.mikhailzaitsevfls.locateme.R;
import com.mikhailzaitsevfls.locateme.mainActivity.MainActivity;
import com.mikhailzaitsevfls.locateme.noInternetActivity.NoInternetActivity;
import com.mikhailzaitsevfls.locateme.registrationActivity.RegistrationActivity;

public class LoginActivityPresenter implements LoginActivityPresenterInterface, PresenterInterfaceWithSecondDetach {
    private LoginActivityInterface activity;


    public void attachView(LoginActivityInterface activity){
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
    public void startCheckingInternet() {
        CheckInternetConnection.startChecking(this, true);
    }

    @Override
    public void stopCheckingInternet(){
        CheckInternetConnection.cancelChecking(this);
    }

    @Override
    public boolean isDetached() {
        return activity == null;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_activity_next_button:
                //////
                activity.startNewActivity(MainActivity.class);
                break;
                //////
                /*String respMessage = Validate.validateEmailAndPassword(activity.getInputEmail(),activity.getInputPassword());
                if (!TextUtils.isEmpty(respMessage)){
                    activity.changeMessage(respMessage);
                    activity.showMessage(true);
                    return;//there're errors in input views so we don't react on User clicking
                }else {

                    activity.showMessage(false);
                    Db.newInstance().checkForAccountExisting(activity.getInputEmail(),activity.getInputPassword(), this);
                    //presenter(this) for kind of callback by inputConfirmed method
                }
                break;*/
            case R.id.login_activity_go_to_registration_button:
                activity.startNewActivity(RegistrationActivity.class);
                break;
        }
    }


    @Override
    public void inputConfirmed(boolean isConfirmed) {
        activity.setProgressBarVisible(false);
        if (isConfirmed){
            activity.startNewActivity(MainActivity.class);
        }else {
            activity.changeMessage("wrong email or password");
            activity.showMessage(true);
        }
    }


    @Override
    public void detachViewNotFromParent() {
        activity.startNewActivity(NoInternetActivity.class);
        //////////////////////////////////////////////////////////////////////
        Db.newInstance().deleteDb();
    }
}