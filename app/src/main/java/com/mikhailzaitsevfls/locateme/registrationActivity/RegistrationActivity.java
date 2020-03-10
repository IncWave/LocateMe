package com.mikhailzaitsevfls.locateme.registrationActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
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

public class RegistrationActivity extends AppCompatActivity implements RegistrationActivityInterface {

    private RegistrationActivityPresenterInterface presenter;

    @BindViews({
            R.id.registration_activity_name,//0
            R.id.registration_activity_email,//1
            R.id.registration_activity_password,//2
            R.id.registration_activity_confirm_password})//3
    public List<MaterialEditText> registrationEditTextsList;

    @BindView(R.id.registration_activity_progress_bar)
    public ContentLoadingProgressBar progressBar;

    @BindView(R.id.registration_activity_next_button)
    public Button nextButton;

    @BindView(R.id.registration_activity_wrong_input_message)
    public TextView wrongInputMessageTextView;

    @BindView(R.id.registration_activity_go_to_login_button)
    public Button goToLoginButton;

    @OnClick({R.id.registration_activity_next_button,R.id.registration_activity_go_to_login_button})
    public void onClick(View view){
        presenter.onClick(view);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ButterKnife.bind(this);
        init();
    }


    void init(){
        presenter = new RegistrationActivityPresenter();
        presenter.attachView(this);
        presenter.isReady();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }


    @Override
    public void startActivity(Class cl) {
        Intent intent = new Intent(this,cl);
        if (cl == NoInternetActivity.class){
            startActivity(intent.putExtra(NoInternetActivity.KEY_NAME,NoInternetActivity.REGISTRATION_ACTIVITY));
        }else {
            startActivity(intent);
        }
        finish();
    }

    @Override
    public String getInputName(){
        return Objects.requireNonNull(registrationEditTextsList.get(0).getText()).toString();
    }


    @Override
    public String getInputEmail(){
        return Objects.requireNonNull(registrationEditTextsList.get(1).getText()).toString();
    }

    @Override
    public String getInputPassword(){
        return Objects.requireNonNull(registrationEditTextsList.get(2).getText()).toString();
    }

    @Override
    public String getInputSecondPassword(){
        return Objects.requireNonNull(registrationEditTextsList.get(3).getText()).toString();
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
    protected void onResume() {
        super.onResume();
        setEnabledButtonGoTo();
        presenter.startCheckingInternet();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.stopCheckingInternet();
    }

    private void setEnabledButtonGoTo(){
        new Handler().postDelayed(() -> {goToLoginButton.setEnabled(true);
        goToLoginButton.setTextColor(getResources().getColor(R.color.colorBlack));},1000 *3);
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

}