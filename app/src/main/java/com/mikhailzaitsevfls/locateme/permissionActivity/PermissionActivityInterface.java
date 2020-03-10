package com.mikhailzaitsevfls.locateme.permissionActivity;

public interface PermissionActivityInterface {

    void startActivity(Class cl);

    boolean checkPermissions();

    void requirePermissions();

    void finishActivity();
}
