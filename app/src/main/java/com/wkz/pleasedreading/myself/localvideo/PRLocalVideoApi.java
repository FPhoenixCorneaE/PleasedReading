package com.wkz.pleasedreading.myself.localvideo;

import android.Manifest;
import androidx.fragment.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.wkz.framework.functions.file.FRFileManager;
import com.wkz.framework.functions.file.model.FRVideoBean;
import com.wkz.framework.functions.retrofit.FRResponseTransformer;
import com.wkz.framework.functions.retrofit.FRRetrofitFactory;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class PRLocalVideoApi extends FRRetrofitFactory {

    private static volatile PRLocalVideoApi mInstance;

    private PRLocalVideoApi() {
    }

    public static PRLocalVideoApi getInstance() {
        if (mInstance == null) {
            synchronized (PRLocalVideoApi.class) {
                if (mInstance == null) {
                    mInstance = new PRLocalVideoApi();
                }
            }
        }
        return mInstance;
    }

    @Override
    public String getBaseUrl() {
        return null;
    }

    /**
     * 获取本地视频
     */
    public void getVideos(FragmentActivity activity, String dir, LifecycleTransformer lifecycleTransformer, Observer<List<FRVideoBean>> observer) {
        Observable.just(dir)
                .compose(new RxPermissions(activity).ensure(Manifest.permission.READ_EXTERNAL_STORAGE))
                .observeOn(Schedulers.io())
                .filter(new Predicate<Boolean>() {
                    @Override
                    public boolean test(Boolean aBoolean) throws Exception {
                        return aBoolean;
                    }
                })
                .flatMap(new Function<Boolean, ObservableSource<List<FRVideoBean>>>() {
                    @Override
                    public ObservableSource<List<FRVideoBean>> apply(Boolean aBoolean) throws Exception {
                        return Observable.just(FRFileManager.getInstance(activity).getVideos(dir));
                    }
                })
                .subscribeOn(Schedulers.io())
                .compose(FRResponseTransformer.handleResult())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .compose(lifecycleTransformer)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
}
