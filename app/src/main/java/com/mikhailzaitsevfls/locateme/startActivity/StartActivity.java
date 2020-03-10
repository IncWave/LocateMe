package com.mikhailzaitsevfls.locateme.startActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.mikhailzaitsevfls.locateme.checking.permission.CheckPermission;
import com.mikhailzaitsevfls.locateme.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartActivity extends AppCompatActivity  implements StartActivityInterface {

    private StartActivityPresenterInterface presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);

        init();
    }

    void init(){
        presenter = new StartActivityPresenter();
        presenter.attachView(this);
        presenter.isReady();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @OnClick(R.id.activity_start_next_button)
    public void onClickNext(){
        presenter.onNextButtonClicked();
    }

    @Override
    public boolean checkPermission(){
        return CheckPermission.checkPermission(this);
    }


    @Override
    public void startActivity(Class cl){
        startActivity(new Intent(this, cl),null);
        this.finish();
    }
}
