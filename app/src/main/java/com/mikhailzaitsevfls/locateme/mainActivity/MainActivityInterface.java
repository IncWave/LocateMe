package com.mikhailzaitsevfls.locateme.mainActivity;
import androidx.core.app.NotificationCompat;

import com.mikhailzaitsevfls.locateme.interfaces.PresenterInterfaceWithCallback;

public interface MainActivityInterface {

    void startNewActivity(Class cl);

    void makeToast(String message);

    Object getSysService(String serviceName);

    NotificationCompat.Builder getNotificationBuilder(String whatGeofences, String chanelId);

    boolean checkPermission();

    void requirePermission(PresenterInterfaceWithCallback presenter);

    void dataHasBeenChanged();

    void onlineHasBeenChanged();
}
