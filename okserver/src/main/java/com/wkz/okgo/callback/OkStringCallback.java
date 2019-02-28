package com.wkz.okgo.callback;

import com.wkz.okgo.converter.OkStringConverter;

import okhttp3.Response;

/**
 * 返回字符串类型的数据
 */
public abstract class OkStringCallback extends OkAbsCallback<String> {

    private OkStringConverter convert;

    public OkStringCallback() {
        convert = new OkStringConverter();
    }

    @Override
    public String convertResponse(Response response) throws Throwable {
        String s = convert.convertResponse(response);
        response.close();
        return s;
    }
}
