package com.wkz.pleasedreading.main.gank;

import com.orhanobut.logger.Logger;
import com.wkz.framework.functions.retrofit.FRHttpError;
import com.wkz.framework.functions.retrofit.FRHttpException;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;


/**
 * 豆瓣图书书评详情
 * Created on 2017/5/16.
 */

public class PRGankVideoSubscriber<T> implements ObservableOnSubscribe<T> {

    /*设置网页抓取响应时间*/
    private static final int TIMEOUT = 10000;
    private PRGankBean.ResultsBean mPrGankBean;
    private String mUrl;

    public PRGankVideoSubscriber(PRGankBean.ResultsBean prGankBean) {
        this.mPrGankBean = prGankBean;
        this.mUrl = prGankBean.getUrl();
        Logger.i(mUrl);
    }

    @Override
    public void subscribe(ObservableEmitter<T> emitter) throws Exception {
        try {
            //开始疯狂的数据抓取啦 这个我就不解释了  大家去看看文档  http://www.open-open.com/jsoup/
            Document document = Jsoup.connect(mUrl).timeout(TIMEOUT).get();
            String html = document.html();
            document = Jsoup.parse(html);

            Logger.i(document.toString());

            Elements scripts = document.getElementsByTag("script");
            Element videoInfo = scripts.get(scripts.size() - 1);
            /*取得JS变量数组*/
            String[] variables = videoInfo.data().split("var");
            Logger.i(Arrays.toString(variables));

            String playAddr = "";
            String cover = "";
            for (String variable : variables) {
                String paramsStr = variable.substring(variable.indexOf(".create(") + 8, variable.indexOf(");"));
                JSONObject paramsJson = new JSONObject(paramsStr);
                Logger.i(paramsJson.toString());
                /*播放地址*/
                if (paramsJson.has("playAddr")) {
                    playAddr = paramsJson.getString("playAddr").replace("https://", "http://");
                    Logger.i(playAddr);
                }
                /*视频封面*/
                if (paramsJson.has("cover")) {
                    cover = paramsJson.getString("cover");
                    Logger.i(cover);
                }
            }

            mPrGankBean.setPlayAddr(playAddr);
            mPrGankBean.setCover(cover);

            emitter.onNext((T) mPrGankBean);
            emitter.onComplete();
        } catch (IOException e) {
            emitter.onError(
                    new FRHttpException(
                            FRHttpError.ERROR_PARSE,
                            FRHttpError.MESSAGE_PARSE,
                            e.getLocalizedMessage()
                    )
            );
        }
    }
}
