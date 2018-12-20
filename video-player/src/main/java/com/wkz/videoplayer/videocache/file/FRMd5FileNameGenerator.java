package com.wkz.videoplayer.videocache.file;

import android.text.TextUtils;

import com.wkz.videoplayer.videocache.proxy.FRProxyCacheUtils;

/**
 * Implementation of {@link FRFileNameGenerator} that uses MD5 of url as file name
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public class FRMd5FileNameGenerator implements FRFileNameGenerator {

    private static final int MAX_EXTENSION_LENGTH = 4;

    @Override
    public String generate(String url) {
        String extension = getExtension(url);
        String name = FRProxyCacheUtils.computeMD5(url);
        return TextUtils.isEmpty(extension) ? name : name + "." + extension;
    }

    private String getExtension(String url) {
        int dotIndex = url.lastIndexOf('.');
        int slashIndex = url.lastIndexOf('/');
        return dotIndex != -1 && dotIndex > slashIndex && dotIndex + 2 + MAX_EXTENSION_LENGTH > url.length() ?
                url.substring(dotIndex + 1, url.length()) : "";
    }
}
