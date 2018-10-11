package com.wkz.framework.functions.retrofit;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class FRRxJavaUtils {

    /**
     * 插入观察者
     * 统一线程处理
     */
    @SuppressWarnings("unchecked")
    public static <T> void toObservable(Observable<T> observable, @NonNull Observer<? super T> observer, LifecycleProvider<ActivityEvent> provider) {
        observable
                //延迟订阅
                .delay(200, TimeUnit.MILLISECONDS)
                //异常处理
                .compose((ObservableTransformer<? super T, ? extends T>) FRResponseTransformer.<T>handleResult())
                //网络请求在子线程，所以是在io线程，避免阻塞线程
                .subscribeOn(Schedulers.io())
                //取消请求的时候在io线程，避免阻塞线程
                .unsubscribeOn(Schedulers.io())
                //注意compose方法需要在subscribeOn方法之后使用，因为在测试的过程中发现，
                //将compose方法放在subscribeOn方法之前，如果在被观察者中执行了阻塞方法，
                //比如Thread.sleep()，取消订阅后该阻塞方法不会被中断。
                .compose(provider.<T>bindUntilEvent(ActivityEvent.DESTROY))
                //回调到主线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 对服务器接口传过来的错误信息进行统一处理
     * 免除在Activity的过多的错误判断
     */
    public static FRHttpException handleException(Throwable throwable) {
        FRHttpException frHttpException;
        if (throwable instanceof ConnectException) {
            //网络连接错误
            frHttpException = new FRHttpException(
                    FRHttpError.ERROR_CONNECT,
                    FRHttpError.MESSAGE_CONNECT,
                    throwable.getMessage());
        } else if (throwable instanceof HttpException) {
            //网络连接错误
            frHttpException = new FRHttpException(
                    FRHttpError.ERROR_CONNECT,
                    FRHttpError.MESSAGE_CONNECT,
                    throwable.getMessage());
        } else if (throwable instanceof JsonParseException
                || throwable instanceof JSONException
                || throwable instanceof ParseException) {
            //数据解析错误
            frHttpException = new FRHttpException(
                    FRHttpError.ERROR_PARSE,
                    FRHttpError.MESSAGE_PARSE,
                    throwable.getMessage());
        } else if (throwable instanceof UnknownHostException || throwable instanceof SocketTimeoutException) {
            //服务器错误
            frHttpException = new FRHttpException(
                    FRHttpError.ERROR_SERVER,
                    FRHttpError.MESSAGE_SERVER,
                    throwable.getMessage());
        } else {
            //未知错误
            frHttpException = new FRHttpException(
                    FRHttpError.ERROR_UNKNOWN,
                    FRHttpError.MESSAGE_UNKNOWN,
                    throwable.getMessage());
        }
        return frHttpException;
    }
}
