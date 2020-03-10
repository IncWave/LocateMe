package com.mikhailzaitsevfls.locateme.mainActivity;


import com.mikhailzaitsevfls.locateme.interfaces.PresenterInterfaceWithSecondDetach;
import com.mikhailzaitsevfls.locateme.mainActivity.MainActivityInterface;

public interface MainActivityPresenterInterface extends PresenterInterfaceWithSecondDetach {
    void attachView(MainActivityInterface activity);

    void startCheckingDbChanges();

    void stopCheckingDbChanges();

    void startCheckingInternet();

    void stopCheckingInternet();
}
