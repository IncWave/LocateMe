package com.mikhailzaitsevfls.locateme.checking.db;

import com.mikhailzaitsevfls.locateme.interfaces.PresenterInterfaceWithCallback;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class CheckLocalDbChanges {


    //for cancel observation
    private static DisposableObserver<Boolean> dObserver;
    private static PresenterInterfaceWithCallback presenter;

    private static ObservableEmitter<Object> emit;

    public static void cancelChecking(){
        if (!dObserver.isDisposed()){
            dObserver.dispose();
        }
    }

    public static void dataChanged(){
        emit.onNext(true);
    }

    public static void onlineChanged(){
        emit.onNext(false);
    }

    public static void startChecking(PresenterInterfaceWithCallback presenterWith){
        if (presenter == null){
            presenter = presenterWith;
            Observable observable = Observable.create(emitter -> {
                emit = emitter;
                emitter.onNext(true);
            }).subscribeOn(Schedulers.io());
            dObserver = new DisposableObserver<Boolean>() {
                @Override
                public void onNext(Boolean bool) {
                    presenter.callBack(bool);
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            };
            observable.observeOn(AndroidSchedulers.mainThread()).subscribe(dObserver);
        }
    }
}
