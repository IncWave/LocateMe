package com.mikhailzaitsevfls.locateme.registrationActivity;

public interface RegistrationActivityInterface {
    void startActivity(Class cl);

    String getInputEmail();

    String getInputPassword();

    String getInputName();

    String getInputSecondPassword();

    void setProgressBarVisible(boolean isVisible);

    void showMessage(boolean shouldMessageBeShown);

    void changeMessage(String message);
}
