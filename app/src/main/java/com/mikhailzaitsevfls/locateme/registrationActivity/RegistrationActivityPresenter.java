package com.mikhailzaitsevfls.locateme.registrationActivity;

import android.text.TextUtils;
import android.view.View;

import com.mikhailzaitsevfls.locateme.interfaces.PresenterInterfaceWithSecondDetach;
import com.mikhailzaitsevfls.locateme.model.Db;
import com.mikhailzaitsevfls.locateme.checking.internet.CheckInternetConnection;
import com.mikhailzaitsevfls.locateme.R;
import com.mikhailzaitsevfls.locateme.login.LoginActivity;
import com.mikhailzaitsevfls.locateme.mainActivity.MainActivity;
import com.mikhailzaitsevfls.locateme.noInternetActivity.NoInternetActivity;

public class RegistrationActivityPresenter implements RegistrationActivityPresenterInterface, PresenterInterfaceWithSecondDetach {

    private RegistrationActivityInterface activity;

    @Override
    public void attachView(RegistrationActivityInterface activity) {
        this.activity = activity;
    }

    @Override
    public void detachView() {
        CheckInternetConnection.cancelChecking(this);
        this.activity = null;
    }

    @Override
    public void isReady() {
    }

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
            case R.id.registration_activity_next_button:
                String respMessage = Validate.validateNameEmailPasswordSecondPassword(
                        activity.getInputName(),
                        activity.getInputEmail(),
                        activity.getInputPassword(),
                        activity.getInputSecondPassword());
                if (!TextUtils.isEmpty(respMessage)){
                    activity.changeMessage(respMessage);
                    activity.showMessage(true);
                    return;//there're errors in input views so we don't react on User clicking
                }else {
                    activity.showMessage(false);
                    activity.setProgressBarVisible(true);
                    Db.newInstance().checkForAccountExistingAndSaveIfNot(activity.getInputName(),activity.getInputEmail(),activity.getInputPassword(), this);
                    //presenter(this) for kind of callback by inputConfirmed method
                }
                break;
            case R.id.registration_activity_go_to_login_button:
                activity.startActivity(LoginActivity.class);
                break;
        }
    }

    @Override
    public void inputConfirmed(boolean isConfirmed) {
        activity.setProgressBarVisible(false);
        if (isConfirmed){
            activity.startActivity(MainActivity.class);
        }else {
            /////////////////////////////////////////////////////something getting wrong
        }
    }


    @Override
    public void detachViewNotFromParent() {
        activity.startActivity(NoInternetActivity.class);
        //////////////////////////////////////////////////////////////////////
        Db.newInstance().deleteDb();
    }
}
