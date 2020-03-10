package com.mikhailzaitsevfls.locateme.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mikhailzaitsevfls.locateme.R;
import com.mikhailzaitsevfls.locateme.noInternetActivity.NoInternetActivity;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginActivityInterface {

    private LoginActivityPresenterInterface presenter;

    @BindViews({
            R.id.login_activity_email,//0
            R.id.login_activity_password})//1
    public List<MaterialEditText> editTextsList;

    @BindView(R.id.login_activity_save_me_radio_button)
    public RadioButton onSaveMeRadioButton;


    boolean isChecked = false;
    @OnClick(R.id.login_activity_save_me_radio_button)
    public void onSaveMeRadioButton(){
        if (isChecked){
            onSaveMeRadioButton.setChecked(false);
        }
        isChecked = !isChecked;
    }

    @BindView(R.id.login_activity_progress_bar)
    public ContentLoadingProgressBar progressBar;

    @BindView(R.id.login_activity_next_button)
    public Button nextButton;

    @BindView(R.id.login_activity_wrong_input_message)
    public TextView wrongInputMessageTextView;


    @BindView(R.id.login_activity_go_to_registration_button)
    public Button goToRegisterButton;

    @OnClick({R.id.login_activity_next_button,R.id.login_activity_go_to_registration_button})
    public void onClick(View view){
        presenter.onClick(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        init();
    }

    void init(){
        presenter = new LoginActivityPresenter();
        presenter.attachView(this);
        presenter.isReady();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }


    @Override
    public void startNewActivity(Class cl) {
        Intent intent = new Intent(this,cl);
        if (cl == NoInternetActivity.class){
            startActivity(intent.putExtra(NoInternetActivity.KEY_NAME,NoInternetActivity.LOGIN_ACTIVITY));
        }else {
            startActivity(intent);
        }
        finish();
    }


    @Override
    public String getInputEmail() {
        return Objects.requireNonNull(editTextsList.get(0).getText()).toString();
    }

    @Override
    public String getInputPassword(){
        return Objects.requireNonNull(editTextsList.get(1).getText()).toString();
    }


    @Override
    public void setProgressBarVisible(boolean isVisible) {
        if (isVisible) {
            nextButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }else {
            progressBar.setVisibility(View.GONE);
            nextButton.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void showMessage(boolean shouldMessageBeShown) {
        if (shouldMessageBeShown){
            wrongInputMessageTextView.setVisibility(View.VISIBLE);
        }else {
            wrongInputMessageTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void changeMessage(String message) {
        wrongInputMessageTextView.setText(message);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setEnabledButtonGoTo();
        presenter.startCheckingInternet();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.stopCheckingInternet();
    }

    private void setEnabledButtonGoTo(){
        new Handler().postDelayed(() -> {goToRegisterButton.setEnabled(true);
            goToRegisterButton.setTextColor(getResources().getColor(R.color.colorBlack));},1000 *3);
    }

}