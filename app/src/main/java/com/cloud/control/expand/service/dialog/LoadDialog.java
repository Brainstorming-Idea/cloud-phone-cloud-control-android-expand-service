package com.cloud.control.expand.service.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.cloud.control.expand.service.R;
import com.cloud.control.expand.service.log.KLog;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Author：abin
 * Date：2020/10/20
 * Description： 加载框
 */
public class LoadDialog extends Dialog {

    public LoadDialog(Context context) {
        super(context, R.style.loadDialogTheme);
        initDialog(context);
    }

    private void initDialog(final Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_load_progress_bar, null);
        setContentView(layout);
    }


    public void showLoadDialog() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull String o) {
                try {
                    show();
                } catch (Exception e) {

                }

            }

            @Override
            public void onError(@NonNull Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    public void dismissLoadDialog() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                KLog.d("subscribe ");
                e.onNext("");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                KLog.d("onSubscribe ");
            }

            @Override
            public void onNext(@NonNull String results) {
                try {
                    dismiss();
                } catch (Exception e) {

                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                KLog.d("onError ");
                dismiss();
            }

            @Override
            public void onComplete() {
                KLog.d("onComplete");
            }
        });
    }
}
