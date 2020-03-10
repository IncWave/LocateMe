package com.mikhailzaitsevfls.locateme.noInternetActivity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mikhailzaitsevfls.locateme.R;

import java.util.Objects;

public class NoInternetActivity extends AppCompatActivity implements NoInternetActivityInterface {

    private NoInternetActivityPresenterInterface presenter;

    public static final String REGISTRATION_ACTIVITY = "registration_activity";
    public static final String PERMISSION_ACTIVITY = "permission_activity";
    public static final String LOGIN_ACTIVITY = "login_activity";
    public static final String START_ACTIVITY = "start_activity";
    public static final String MAIN_ACTIVITY = "main_activity";
    public static final String KEY_NAME = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_connection);

        init(Objects.requireNonNull(getIntent().getExtras()).getString(KEY_NAME));//get prev activity name
    }


    private void init(String prev_activity_name){
        presenter = new NoInternetActivityPresenter();
        presenter.attachView(this);
        presenter.isReady(prev_activity_name);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.startCheckingInternet();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.stopCheckingInternet();
    }

    @Override
    public void startActivity(Class cl){
        startActivity(new Intent(this, cl));
        this.finish();
    }
}


