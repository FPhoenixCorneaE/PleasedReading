package com.wkz.framework.functions.retrofit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

public class FRResponseTransformer {

    /**
     * ObservableTransformer<Upstream, Downstream>
     * 处理返回结果{Upstream}
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> handleResult() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .onErrorResumeNext(new ErrorResumeFunction<T>())
                        .flatMap(new ResponseFunction<T>());
            }
        };
    }


    /**
     * 非服务器产生的异常，比如本地无无网络请求，Json数据解析错误等等。
     *
     * @param <T>
     */
    private static class ErrorResumeFunction<T> implements Function<Throwable, ObservableSource<? extends T>> {

        @Override
        public ObservableSource<? extends T> apply(Throwable throwable) {
            return Observable.error(FRRxJavaUtils.handleException(throwable));
        }
    }

    /**
     * 服务器返回的数据解析
     * 正常服务器返回数据和服务器可能返回的exception
     *
     * @param <T>
     */
    private static class ResponseFunction<T> implements Function<T, ObservableSource<T>> {

        @Override
        public ObservableSource<T> apply(T tResponse) {
            //TODO 在这里可以和服务端约定好所有接口返回的数据结构一样，做统一的处理
//            int code = tResponse.getCode();
//            String message = tResponse.getMsg();
//            if (code == 200) {
//                return Observable.just(tResponse.getData());
//            } else {
//                return Observable.error(new FRHttpException(code, message, message));
//            }
            return Observable.just(tResponse);
        }
    }
}
