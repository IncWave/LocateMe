package com.mikhailzaitsevfls.locateme.startActivity;

import com.mikhailzaitsevfls.locateme.interfaces.PresenterInterface;

public interface StartActivityPresenterInterface extends PresenterInterface {

    void attachView(StartActivityInterface activity);

    void onNextButtonClicked();
}
