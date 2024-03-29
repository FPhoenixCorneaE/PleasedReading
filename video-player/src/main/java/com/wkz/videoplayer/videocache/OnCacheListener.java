package com.wkz.videoplayer.videocache;

import java.io.File;

/**
 * Listener for cache availability.
 *
 * @author Egor Makovsky (yahor.makouski@gmail.com)
 * @author Alexey Danilov (danikula@gmail.com).
 */
public interface OnCacheListener {

    void onCacheAvailable(File cacheFile, String url, int percentsAvailable);
}
