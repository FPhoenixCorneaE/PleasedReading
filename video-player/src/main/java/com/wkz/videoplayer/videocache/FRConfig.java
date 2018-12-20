package com.wkz.videoplayer.videocache;

import com.wkz.videoplayer.videocache.file.FRDiskUsage;
import com.wkz.videoplayer.videocache.file.FRFileNameGenerator;
import com.wkz.videoplayer.videocache.headers.FRHeaderInjector;
import com.wkz.videoplayer.videocache.sourcestorage.FRSourceInfoStorage;

import java.io.File;

/**
 * Configuration for proxy cache.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public class FRConfig {

    public final File cacheRoot;
    public final FRFileNameGenerator fileNameGenerator;
    public final FRDiskUsage diskUsage;
    public final FRSourceInfoStorage sourceInfoStorage;
    public final FRHeaderInjector headerInjector;

    public FRConfig(File cacheRoot, FRFileNameGenerator fileNameGenerator, FRDiskUsage diskUsage, FRSourceInfoStorage sourceInfoStorage, FRHeaderInjector headerInjector) {
        this.cacheRoot = cacheRoot;
        this.fileNameGenerator = fileNameGenerator;
        this.diskUsage = diskUsage;
        this.sourceInfoStorage = sourceInfoStorage;
        this.headerInjector = headerInjector;
    }

    public File generateCacheFile(String url) {
        String name = fileNameGenerator.generate(url);
        return new File(cacheRoot, name);
    }

}
