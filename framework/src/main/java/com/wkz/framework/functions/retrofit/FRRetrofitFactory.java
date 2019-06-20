package com.wkz.framework.functions.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.wkz.unit.MemoryUnit;
import com.wkz.framework.functions.retrofit.interceptor.FRHeaderInterceptor;
import com.wkz.framework.functions.retrofit.interceptor.FRNetworkInterceptor;
import com.wkz.framework.functions.retrofit.ssl.FRSslSocketUtils;
import com.wkz.framework.functions.retrofit.typeadapter.FRDefaultDoubleAdapter;
import com.wkz.framework.functions.retrofit.typeadapter.FRDefaultFloatAdapter;
import com.wkz.framework.functions.retrofit.typeadapter.FRDefaultIntegerAdapter;
import com.wkz.framework.functions.retrofit.typeadapter.FRDefaultLongAdapter;
import com.wkz.framework.functions.retrofit.typeadapter.FRDefaultStringAdapter;
import com.wkz.utils.FileUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class FRRetrofitFactory {

    private static volatile OkHttpClient mOkHttpClient;
    private static volatile Retrofit.Builder mRetrofitBuilder;

    //加载动态代码块
    static {
        FRSslSocketUtils.SSLParams sslParams = FRSslSocketUtils.getSslSocketFactory();
        mOkHttpClient = new OkHttpClient.Builder()
                //添加请求头拦截器
                .addInterceptor(new FRHeaderInterceptor())
                //打印请求参数和响应数据
                .addInterceptor(new HttpLoggingInterceptor(
                        new HttpLoggingInterceptor.Logger() {
                            @Override
                            public void log(String message) {
                                Logger.i(message);
                            }
                        })
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                //添加网络连接器
                .addNetworkInterceptor(new FRNetworkInterceptor())
                //信任所有证书,不安全有风险
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .hostnameVerifier(FRSslSocketUtils.UnSafeHostnameVerifier)
                //设置连接超时时间
                .connectTimeout(15, TimeUnit.SECONDS)
                //设置请求读写的超时时间
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                //错误重连
                .retryOnConnectionFailure(true)
                //缓存
                .cache(new Cache(FileUtils.getCacheDirectory("FRCache"),
                        100 * MemoryUnit.MB)
                )
                .build();
    }

    /**
     * 获取Retrofit对象
     */
    private Retrofit getRetrofit() {
        if (null == mRetrofitBuilder) {
            synchronized (FRRetrofitFactory.class) {
                if (null == mRetrofitBuilder) {
                    //Retrofit2后使用build设计模式
                    mRetrofitBuilder = new Retrofit.Builder()
                            //设置使用okhttp网络请求
                            .client(mOkHttpClient)
                            //解析方法,添加转化库，默认是Gson
                            .addConverterFactory(GsonConverterFactory.create(buildGson()))
                            //添加回调库，采用RxJava
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
                }
            }
        }
        return mRetrofitBuilder
                //主机地址,服务器路径
                .baseUrl(getBaseUrl())
                .build();
    }

    private Gson buildGson() {
        return new GsonBuilder()
                //如果不设置serializeNulls,序列化时默认忽略NULL
                .serializeNulls()
                //使打印的json字符串更美观，如果不设置，打印出来的字符串不分行
                .setPrettyPrinting()
                //自定义类型适配器
                .registerTypeAdapter(Double.class, new FRDefaultDoubleAdapter())
                .registerTypeAdapter(Float.class, new FRDefaultFloatAdapter())
                .registerTypeAdapter(Integer.class, new FRDefaultIntegerAdapter())
                .registerTypeAdapter(Long.class, new FRDefaultLongAdapter())
                .registerTypeAdapter(String.class, new FRDefaultStringAdapter())
                .create();
    }

    /**
     * 实例化retrofit对象
     */
    protected <T> T createRetrofit(final Class<T> tClass) {
        return getRetrofit().create(tClass);
    }

    public void setObserver(Observable observable, LifecycleTransformer lifecycleTransformer, Observer observer) {
        FRRxJavaUtils.toObservable(observable, lifecycleTransformer, observer);
    }

    public abstract String getBaseUrl();
}
