package com.wkz.okgo.request.base;

import com.wkz.okgo.model.OkHttpParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 表示当前请求是否具有请求体
 */
public interface IOkHasBody<R> {

    R isMultipart(boolean isMultipart);

    R isSpliceUrl(boolean isSpliceUrl);

    R upRequestBody(RequestBody requestBody);

    R params(String key, File file);

    R addFileParams(String key, List<File> files);

    R addFileWrapperParams(String key, List<OkHttpParams.FileWrapper> fileWrappers);

    R params(String key, File file, String fileName);

    R params(String key, File file, String fileName, MediaType contentType);

    R upString(String string);

    R upString(String string, MediaType mediaType);

    R upJson(String json);

    R upJson(JSONObject jsonObject);

    R upJson(JSONArray jsonArray);

    R upBytes(byte[] bs);

    R upBytes(byte[] bs, MediaType mediaType);

    R upFile(File file);

    R upFile(File file, MediaType mediaType);
}
