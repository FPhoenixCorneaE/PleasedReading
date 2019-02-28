package com.wkz.okgo.converter;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 字符串转换器
 */
public class OkStringConverter implements IOkConverter<String> {

    @Override
    public String convertResponse(Response response) throws Throwable {
        ResponseBody body = response.body();
        if (body == null) return null;
        return body.string();
    }
}
