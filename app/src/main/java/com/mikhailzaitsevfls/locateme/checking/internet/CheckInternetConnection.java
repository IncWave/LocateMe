package com.mikhailzaitsevfls.locateme.checking.internet;
import com.mikhailzaitsevfls.locateme.interfaces.PresenterInterfaceWithSecondDetach;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class CheckInternetConnection {

    //period of checking the internet connection
    private static final int timeoutMS = 1500;
    //objects needed for checking
    private static Socket socket;
    private static SocketAddress socketAdr;

    //for cancel observation
    private static DisposableObserver dObserver;

    private static PresenterInterfaceWithSecondDetach presenter;

    private CheckInternetConnection() {}


    //if with presenter required for not canceling from not last presenters
    //(only presenter which last has called startChecking will have an opportunity for cancelling checking)
    public static void cancelChecking(PresenterInterfaceWithSecondDetach presenterWithSecondDetach){
        if (presenterWithSecondDetach != null && presenterWithSecondDetach == presenter){
            if (!dObserver.isDisposed()){
                dObserver.dispose();
            }
            try {
                socket.close();
            } catch (Exception ignored) {}
            socket = null;
        }
    }


    //checking the opportunity to connect socket(with no internet it will throw exception)
    public static void startChecking(PresenterInterfaceWithSecondDetach presenterWithSecondDetach, Boolean wannaCheckForNOconnection) {
        presenter = presenterWithSecondDetach;
        Observable observable = Observable.
                create(emitter -> {
                    if (socketAdr == null) {
                        socketAdr = new InetSocketAddress("8.8.8.8", 53);
                    }
                    try {
                        socket = new Socket();
                        //if (bool == true) in case of connection will repeat checking every 1500ms(will wait until no connection)
                        //if(bool == false) in case of connection will stop repeat checking and call detachView
                        socket.connect(socketAdr, timeoutMS);
                        socket.close();
                        if (wannaCheckForNOconnection) {
                            emitter.onComplete();
                            return;
                        } else {/* will do (1) */}
                    } catch (Exception e) {
                        //if (bool == true) in case of NO connection will stop repeat checking and call detachView
                        //if(bool == false) in case of NO connection will repeat checking every 1500ms
                        if (wannaCheckForNOconnection) {
                            emitter.onError(new Exception());
                            return;
                        } else {
                            emitter.onComplete();
                            return;
                        }
                    }

                    emitter.onError(new Exception()); //(1)

                }).subscribeOn(Schedulers.io())//checking in other thread
                .repeatWhen(completed -> completed.delay(5000, TimeUnit.MILLISECONDS));
        dObserver = (DisposableObserver) observable.subscribeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver() {
                    @Override
                    public void onNext(Object o) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        presenter.detachViewNotFromParent();
                        dObserver.dispose();
                    }

                    @Override
                    public void onComplete() {
                        // do nothing
                    }
                });
    }
    }