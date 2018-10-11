package com.wkz.framework.functions.retrofit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

public class FRResponseTransformer {

    public static <T> ObservableTransformer<FRHttpResponse<T>, T> handleResult() {
        return new ObservableTransformer<FRHttpResponse<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<FRHttpResponse<T>> upstream) {
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
    private static class ErrorResumeFunction<T> implements Function<Throwable, ObservableSource<? extends FRHttpResponse<T>>> {

        @Override
        public ObservableSource<? extends FRHttpResponse<T>> apply(Throwable throwable) {
            return Observable.error(FRRxJavaUtils.handleException(throwable));
        }
    }

    /**
     * 服务器返回的数据解析
     * 正常服务器返回数据和服务器可能返回的exception
     *
     * @param <T>
     */
    private static class ResponseFunction<T> implements Function<FRHttpResponse<T>, ObservableSource<T>> {

        @Override
        public ObservableSource<T> apply(FRHttpResponse<T> tResponse) {
            int code = tResponse.getCode();
            String message = tResponse.getMsg();
            if (code == 200) {
                return Observable.just(tResponse.getData());
            } else {
                return Observable.error(new FRHttpException(code, message, message));
            }
        }
    }
}
