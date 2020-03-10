package com.mikhailzaitsevfls.locateme.permissionActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mikhailzaitsevfls.locateme.interfaces.PresenterInterfaceWithCallback;
import com.mikhailzaitsevfls.locateme.checking.permission.CheckPermission;
import com.mikhailzaitsevfls.locateme.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PermissionActivity extends AppCompatActivity implements PermissionActivityInterface {

    int PERMISSION_NUMBER = 1;

    private PermissionActivityPresenterInterface presenter;

    @OnClick(R.id.permission_activity_next_button)
    public void onClick(){
        presenter.onNextButtonClicked();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        ButterKnife.bind(this);
        init();
    }


    private void init(){
        presenter = new PermissionActivityPresenter();
        presenter.attachView(this);
        presenter.isReady();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }


    @Override
    public void startActivity(Class cl){
        startActivity(new Intent(this, cl), null);
        this.finish();
    }


    @Override
    public boolean checkPermissions(){
        return CheckPermission.checkPermission(this);
    }


    @Override
    public void requirePermissions(){
        CheckPermission.requirePermission(this, PERMISSION_NUMBER);
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PresenterInterfaceWithCallback samePresenter = (PresenterInterfaceWithCallback) presenter;
        for (int permission : grantResults){
            if (!(permission == PackageManager.PERMISSION_GRANTED)) {
                samePresenter.callBack(false);
                return;
            }
        }
        if (!presenter.isDetached())
            samePresenter.callBack(true);
    }
}