package com.wkz.videoplayer.videocache.file;

import java.io.File;

/**
 * {@link FRDiskUsage} that uses LRU (Least Recently Used) strategy and trims cache size to max size if needed.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public class FRTotalSizeLruDiskUsage extends FRLruDiskUsage {

    private final long maxSize;

    public FRTotalSizeLruDiskUsage(long maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("Max size must be positive number!");
        }
        this.maxSize = maxSize;
    }

    @Override
    protected boolean accept(File file, long totalSize, int totalCount) {
        return totalSize <= maxSize;
    }
}
