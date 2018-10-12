package com.wkz.framework.functions.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.wkz.framework.annotations.FRMemoryUnit;
import com.wkz.framework.functions.retrofit.typeadapter.DefaultDoubleAdapter;
import com.wkz.framework.functions.retrofit.typeadapter.DefaultFloatAdapter;
import com.wkz.framework.functions.retrofit.typeadapter.DefaultIntegerAdapter;
import com.wkz.framework.functions.retrofit.typeadapter.DefaultLongAdapter;
import com.wkz.framework.functions.retrofit.typeadapter.DefaultStringAdapter;
import com.wkz.framework.utils.FileUtils;

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
                //设置连接超时时间
                .connectTimeout(15, TimeUnit.SECONDS)
                //设置请求读写的超时时间
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                //错误重连
                .retryOnConnectionFailure(true)
                //缓存
                .cache(new Cache(FileUtils.getCacheDirectory("FRCache"),
                        100 * FRMemoryUnit.MB)
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
                .registerTypeAdapter(Double.class, new DefaultDoubleAdapter())
                .registerTypeAdapter(Float.class, new DefaultFloatAdapter())
                .registerTypeAdapter(Integer.class, new DefaultIntegerAdapter())
                .registerTypeAdapter(Long.class, new DefaultLongAdapter())
                .registerTypeAdapter(String.class, new DefaultStringAdapter())
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
