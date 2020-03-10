package com.mikhailzaitsevfls.locateme.login;

public interface LoginActivityInterface {

    void startNewActivity(Class cl);

    String getInputEmail();

    String getInputPassword();

    void setProgressBarVisible(boolean isVisible);

    void showMessage(boolean shouldMessageBeShown);

    void changeMessage(String wrong_input);
}
