package com.wkz.okgo.request.base;

import android.text.TextUtils;

import com.wkz.okgo.model.OkHttpHeaders;
import com.wkz.okgo.model.OkHttpParams;
import com.wkz.okgo.utils.OkHttpUtils;
import com.wkz.okgo.utils.OkLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public abstract class OkBodyRequest<T, R extends OkBodyRequest> extends OkRequest<T, R> implements IOkHasBody<R> {
    private static final long serialVersionUID = -6459175248476927501L;

    protected transient MediaType mediaType;        //上传的MIME类型
    protected String content;                       //上传的文本内容
    protected byte[] bs;                            //上传的字节数据
    protected transient File file;                  //单纯的上传一个文件

    protected boolean isMultipart = false;  //是否强制使用 multipart/form-data 表单上传
    protected boolean isSpliceUrl = false;  //是否拼接url参数
    protected RequestBody requestBody;

    public OkBodyRequest(String url) {
        super(url);
    }

    @SuppressWarnings("unchecked")
    @Override
    public R isMultipart(boolean isMultipart) {
        this.isMultipart = isMultipart;
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public R isSpliceUrl(boolean isSpliceUrl) {
        this.isSpliceUrl = isSpliceUrl;
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public R params(String key, File file) {
        params.put(key, file);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public R addFileParams(String key, List<File> files) {
        params.putFileParams(key, files);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public R addFileWrapperParams(String key, List<OkHttpParams.FileWrapper> fileWrappers) {
        params.putFileWrapperParams(key, fileWrappers);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public R params(String key, File file, String fileName) {
        params.put(key, file, fileName);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public R params(String key, File file, String fileName, MediaType contentType) {
        params.put(key, file, fileName, contentType);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public R upRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
        return (R) this;
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    @SuppressWarnings("unchecked")
    @Override
    public R upString(String string) {
        this.content = string;
        this.mediaType = OkHttpParams.MEDIA_TYPE_PLAIN;
        return (R) this;
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     * 该方法用于定制请求content-type
     */
    @SuppressWarnings("unchecked")
    @Override
    public R upString(String string, MediaType mediaType) {
        this.content = string;
        this.mediaType = mediaType;
        return (R) this;
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    @SuppressWarnings("unchecked")
    @Override
    public R upJson(String json) {
        this.content = json;
        this.mediaType = OkHttpParams.MEDIA_TYPE_JSON;
        return (R) this;
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    @SuppressWarnings("unchecked")
    @Override
    public R upJson(JSONObject jsonObject) {
        this.content = jsonObject.toString();
        this.mediaType = OkHttpParams.MEDIA_TYPE_JSON;
        return (R) this;
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    @SuppressWarnings("unchecked")
    @Override
    public R upJson(JSONArray jsonArray) {
        this.content = jsonArray.toString();
        this.mediaType = OkHttpParams.MEDIA_TYPE_JSON;
        return (R) this;
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    @SuppressWarnings("unchecked")
    @Override
    public R upBytes(byte[] bs) {
        this.bs = bs;
        this.mediaType = OkHttpParams.MEDIA_TYPE_STREAM;
        return (R) this;
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    @SuppressWarnings("unchecked")
    @Override
    public R upBytes(byte[] bs, MediaType mediaType) {
        this.bs = bs;
        this.mediaType = mediaType;
        return (R) this;
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    @SuppressWarnings("unchecked")
    @Override
    public R upFile(File file) {
        this.file = file;
        this.mediaType = OkHttpUtils.guessMimeType(file.getName());
        return (R) this;
    }

    /**
     * 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除
     */
    @SuppressWarnings("unchecked")
    @Override
    public R upFile(File file, MediaType mediaType) {
        this.file = file;
        this.mediaType = mediaType;
        return (R) this;
    }

    @Override
    public RequestBody generateRequestBody() {
        if (isSpliceUrl) url = OkHttpUtils.createUrlFromParams(baseUrl, params.urlParamsMap);

        if (requestBody != null)
            return requestBody;                                                //自定义的请求体
        if (content != null && mediaType != null)
            return RequestBody.create(mediaType, content);    //上传字符串数据
        if (bs != null && mediaType != null)
            return RequestBody.create(mediaType, bs);              //上传字节数组
        if (file != null && mediaType != null)
            return RequestBody.create(mediaType, file);          //上传一个文件
        return OkHttpUtils.generateMultipartRequestBody(params, isMultipart);
    }

    protected okhttp3.Request.Builder generateRequestBuilder(RequestBody requestBody) {
        try {
            headers(OkHttpHeaders.HEAD_KEY_CONTENT_LENGTH, String.valueOf(requestBody.contentLength()));
        } catch (IOException e) {
            OkLogger.printStackTrace(e);
        }
        okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder();
        return OkHttpUtils.appendHeaders(requestBuilder, headers);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(mediaType == null ? "" : mediaType.toString());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        String mediaTypeString = (String) in.readObject();
        if (!TextUtils.isEmpty(mediaTypeString)) {
            mediaType = MediaType.parse(mediaTypeString);
        }
    }
}
