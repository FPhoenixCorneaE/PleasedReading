package com.wkz.videoplayer.videocache.headers;

import java.util.HashMap;
import java.util.Map;

/**
 * Empty {@link FRHeaderInjector} implementation.
 *
 * @author Lucas Nelaupe (https://github.com/lucas34).
 */
public class FREmptyHeadersInjector implements FRHeaderInjector {

    @Override
    public Map<String, String> addHeaders(String url) {
        return new HashMap<>();
    }

}
